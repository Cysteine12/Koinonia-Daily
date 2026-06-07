package org.eni.koinoniadaily.modules.teachingchunk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.EmbeddingStatus;
import org.eni.koinoniadaily.modules.teachingchunk.dto.ChunkCandidate;
import org.eni.koinoniadaily.modules.teachingchunk.dto.TeachingChunkRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeachingChunkService {

  private final TeachingRepository teachingRepository;
  private final TeachingChunkRepository teachingChunkRepository;
  private final TeachingChunkUtil teachingChunkUtil;
  private final TeachingChunkMapper teachingChunkMapper;

  @Transactional
  public void triggerChunking(TeachingChunkRequest request) {

    log.info("Chunking triggered for teaching: {}", request.getTeachingId());

    Teaching teaching = teachingRepository.findById(request.getTeachingId())
        .orElseThrow(() -> new NotFoundException("Teaching not found"));

    if (teaching.getEmbeddingStatus() != EmbeddingStatus.PENDING) {
      throw new ValidationException("Teaching already chunked");
    }

    List<ChunkCandidate> chunkCandidates = teachingChunkUtil.chunk(teaching.getMessage());

    List<TeachingChunk> chunks =
        chunkCandidates.stream()
            .map(chunkCandidate -> teachingChunkMapper.candidateToEntity(chunkCandidate, teaching))
            .toList();

    if (chunks.isEmpty()) {
      throw new ValidationException("Chunks empty. Chunking failed to process");
    }

    teachingChunkRepository.saveAll(chunks);

    teaching.setEmbeddingStatus(EmbeddingStatus.CHUNKED);
    teachingRepository.save(teaching);

    log.info("Chunking successful for teaching: {}. Generated {} chunks.",
        teaching.getId(), chunks.size());
  }

  @Transactional
  public void triggerRechunking(TeachingChunkRequest request) {

    log.info("Re-chunking initiated for teaching: {}", request.getTeachingId());

    Teaching teaching = teachingRepository.findById(request.getTeachingId())
        .orElseThrow(() -> new NotFoundException("Teaching not found"));

    if (teaching.getEmbeddingStatus() == EmbeddingStatus.EMBEDDING) {
      throw new ValidationException("Teaching currently undergoing embedding");
    }

    // 1. Generate new chunks first (fail-fast before deletion)
    List<ChunkCandidate> candidates = teachingChunkUtil.chunk(teaching.getMessage());

    if (candidates.isEmpty()) {
      throw new ValidationException("No chunks generated from teaching message");
    }

    // 2. Perform destructive cleanup
    // Because of DB-level ON DELETE CASCADE, this cleans up chunk_embeddings too
    teachingChunkRepository.deleteByTeachingId(teaching.getId());

    // 3. Map to entities
    List<TeachingChunk> chunks = candidates.stream()
        .map(candidate -> teachingChunkMapper.candidateToEntity(candidate, teaching))
        .toList();

    // 4. Persist new chunks
    teachingChunkRepository.saveAll(chunks);

    // 5. Update teaching status
    teaching.setEmbeddingStatus(EmbeddingStatus.CHUNKED);
    teachingRepository.save(teaching);

    log.info("Re-chunking successful for teaching: {}. Generated {} chunks.",
        teaching.getId(), chunks.size());
  }
}
