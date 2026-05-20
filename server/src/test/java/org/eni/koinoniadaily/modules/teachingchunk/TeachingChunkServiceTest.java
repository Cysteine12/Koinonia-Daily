package org.eni.koinoniadaily.modules.teachingchunk;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.exceptions.ValidationException;
import org.eni.koinoniadaily.modules.teaching.Teaching;
import org.eni.koinoniadaily.modules.teaching.TeachingRepository;
import org.eni.koinoniadaily.modules.teaching.TeachingStatus;
import org.eni.koinoniadaily.modules.teachingchunk.dto.ChunkCandidate;
import org.eni.koinoniadaily.modules.teachingchunk.dto.TeachingChunkRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeachingChunkServiceTest {

  @Mock
  private TeachingRepository teachingRepository;

  @Mock
  private TeachingChunkRepository teachingChunkRepository;

  @Mock
  private TeachingChunkUtil teachingChunkUtil;

  @Mock
  private TeachingChunkMapper teachingChunkMapper;

  @InjectMocks
  private TeachingChunkService service;

  private Teaching makeTeaching(Long id, TeachingStatus status, String message) {
    Teaching teaching = new Teaching();
    teaching.setId(id);
    teaching.setStatus(status);
    teaching.setMessage(message);
    return teaching;
  }

  private TeachingChunkRequest makeRequest(Long teachingId) {
    TeachingChunkRequest request = new TeachingChunkRequest();
    request.setTeachingId(teachingId);
    return request;
  }

  private ChunkCandidate makeCandidate(int index) {
    return new ChunkCandidate(index, "Title " + index, "Content " + index, index * 100);
  }

  private TeachingChunk makeChunkEntity(int index) {
    TeachingChunk chunk = new TeachingChunk();
    chunk.setChunkIndex(index);
    chunk.setContent("Content " + index);
    return chunk;
  }

  // =========================================================================
  // triggerChunking tests
  // =========================================================================

  @Nested
  class TriggerChunkingTests {

    @Test
    void triggerChunking_teachingNotFound_throwsNotFoundException() {
      when(teachingRepository.findById(1L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.triggerChunking(makeRequest(1L)))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Teaching not found");
    }

    @Test
    void triggerChunking_teachingNotPending_throwsValidationException() {
      Teaching chunked = makeTeaching(1L, TeachingStatus.CHUNKED, "Message");
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(chunked));

      assertThatThrownBy(() -> service.triggerChunking(makeRequest(1L)))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("already chunked");
    }

    @Test
    void triggerChunking_embeddingStatus_throwsValidationException() {
      Teaching embedding = makeTeaching(1L, TeachingStatus.EMBEDDING, "Message");
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(embedding));

      assertThatThrownBy(() -> service.triggerChunking(makeRequest(1L)))
          .isInstanceOf(ValidationException.class);
    }

    @Test
    void triggerChunking_emptyChunksGenerated_throwsValidationException() {
      Teaching pending = makeTeaching(1L, TeachingStatus.PENDING, "Short text");
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(pending));
      when(teachingChunkUtil.chunk("Short text")).thenReturn(Collections.emptyList());

      assertThatThrownBy(() -> service.triggerChunking(makeRequest(1L)))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("Chunks empty");
    }

    @Test
    void triggerChunking_success_savesChunksAndUpdatesStatus() {
      Teaching pending = makeTeaching(1L, TeachingStatus.PENDING, "Long message content.");
      List<ChunkCandidate> candidates = List.of(makeCandidate(0), makeCandidate(1));
      List<TeachingChunk> chunks = List.of(makeChunkEntity(0), makeChunkEntity(1));

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(pending));
      when(teachingChunkUtil.chunk("Long message content.")).thenReturn(candidates);
      when(teachingChunkMapper.candidateToEntity(candidates.get(0), pending)).thenReturn(chunks.get(0));
      when(teachingChunkMapper.candidateToEntity(candidates.get(1), pending)).thenReturn(chunks.get(1));
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(chunks);
      when(teachingRepository.save(any(Teaching.class))).thenReturn(pending);

      service.triggerChunking(makeRequest(1L));

      verify(teachingChunkRepository).saveAll(chunks);
      assertThat(pending.getStatus()).isEqualTo(TeachingStatus.CHUNKED);
      verify(teachingRepository).save(pending);
    }

    @Test
    void triggerChunking_success_setsStatusToChunked() {
      Teaching pending = makeTeaching(1L, TeachingStatus.PENDING, "Content.");
      ChunkCandidate candidate = makeCandidate(0);
      TeachingChunk chunk = makeChunkEntity(0);

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(pending));
      when(teachingChunkUtil.chunk("Content.")).thenReturn(List.of(candidate));
      when(teachingChunkMapper.candidateToEntity(candidate, pending)).thenReturn(chunk);
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(List.of(chunk));
      when(teachingRepository.save(any())).thenReturn(pending);

      service.triggerChunking(makeRequest(1L));

      ArgumentCaptor<Teaching> captor = ArgumentCaptor.forClass(Teaching.class);
      verify(teachingRepository).save(captor.capture());
      assertThat(captor.getValue().getStatus()).isEqualTo(TeachingStatus.CHUNKED);
    }

    @Test
    void triggerChunking_callsChunkUtilWithTeachingMessage() {
      String message = "This is the teaching message.";
      Teaching pending = makeTeaching(1L, TeachingStatus.PENDING, message);
      ChunkCandidate candidate = makeCandidate(0);
      TeachingChunk chunk = makeChunkEntity(0);

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(pending));
      when(teachingChunkUtil.chunk(message)).thenReturn(List.of(candidate));
      when(teachingChunkMapper.candidateToEntity(candidate, pending)).thenReturn(chunk);
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(List.of(chunk));
      when(teachingRepository.save(any())).thenReturn(pending);

      service.triggerChunking(makeRequest(1L));

      verify(teachingChunkUtil).chunk(message);
    }
  }

  // =========================================================================
  // triggerRechunking tests
  // =========================================================================

  @Nested
  class TriggerRechunkingTests {

    @Test
    void triggerRechunking_teachingNotFound_throwsNotFoundException() {
      when(teachingRepository.findById(1L)).thenReturn(Optional.empty());

      assertThatThrownBy(() -> service.triggerRechunking(makeRequest(1L)))
          .isInstanceOf(NotFoundException.class)
          .hasMessageContaining("Teaching not found");
    }

    @Test
    void triggerRechunking_teachingIsEmbedding_throwsValidationException() {
      Teaching embedding = makeTeaching(1L, TeachingStatus.EMBEDDING, "Message");
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(embedding));

      assertThatThrownBy(() -> service.triggerRechunking(makeRequest(1L)))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("currently undergoing embedding");
    }

    @Test
    void triggerRechunking_emptyCandidates_throwsValidationException() {
      Teaching chunked = makeTeaching(1L, TeachingStatus.CHUNKED, "Message");
      when(teachingRepository.findById(1L)).thenReturn(Optional.of(chunked));
      when(teachingChunkUtil.chunk("Message")).thenReturn(Collections.emptyList());

      assertThatThrownBy(() -> service.triggerRechunking(makeRequest(1L)))
          .isInstanceOf(ValidationException.class)
          .hasMessageContaining("No chunks generated");
    }

    @Test
    void triggerRechunking_success_deletesOldChunksAndSavesNew() {
      Teaching chunked = makeTeaching(1L, TeachingStatus.CHUNKED, "Message");
      ChunkCandidate candidate = makeCandidate(0);
      TeachingChunk chunk = makeChunkEntity(0);

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(chunked));
      when(teachingChunkUtil.chunk("Message")).thenReturn(List.of(candidate));
      when(teachingChunkMapper.candidateToEntity(candidate, chunked)).thenReturn(chunk);
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(List.of(chunk));
      when(teachingRepository.save(any())).thenReturn(chunked);

      service.triggerRechunking(makeRequest(1L));

      verify(teachingChunkRepository).deleteByTeachingId(1L);
      verify(teachingChunkRepository).saveAll(List.of(chunk));
    }

    @Test
    void triggerRechunking_success_setsStatusToChunked() {
      Teaching failed = makeTeaching(1L, TeachingStatus.FAILED, "Message");
      ChunkCandidate candidate = makeCandidate(0);
      TeachingChunk chunk = makeChunkEntity(0);

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(failed));
      when(teachingChunkUtil.chunk("Message")).thenReturn(List.of(candidate));
      when(teachingChunkMapper.candidateToEntity(candidate, failed)).thenReturn(chunk);
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(List.of(chunk));
      when(teachingRepository.save(any())).thenReturn(failed);

      service.triggerRechunking(makeRequest(1L));

      assertThat(failed.getStatus()).isEqualTo(TeachingStatus.CHUNKED);
    }

    @Test
    void triggerRechunking_deletionHappensBeforeSavingNew() {
      Teaching chunked = makeTeaching(1L, TeachingStatus.CHUNKED, "Message");
      ChunkCandidate candidate = makeCandidate(0);
      TeachingChunk chunk = makeChunkEntity(0);

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(chunked));
      when(teachingChunkUtil.chunk("Message")).thenReturn(List.of(candidate));
      when(teachingChunkMapper.candidateToEntity(candidate, chunked)).thenReturn(chunk);
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(List.of(chunk));
      when(teachingRepository.save(any())).thenReturn(chunked);

      service.triggerRechunking(makeRequest(1L));

      // Verify order: deleteByTeachingId before saveAll
      var inOrder = inOrder(teachingChunkRepository);
      inOrder.verify(teachingChunkRepository).deleteByTeachingId(1L);
      inOrder.verify(teachingChunkRepository).saveAll(anyList());
    }

    @Test
    void triggerRechunking_pendingStatus_isAllowed() {
      Teaching pending = makeTeaching(1L, TeachingStatus.PENDING, "Message");
      ChunkCandidate candidate = makeCandidate(0);
      TeachingChunk chunk = makeChunkEntity(0);

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(pending));
      when(teachingChunkUtil.chunk("Message")).thenReturn(List.of(candidate));
      when(teachingChunkMapper.candidateToEntity(candidate, pending)).thenReturn(chunk);
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(List.of(chunk));
      when(teachingRepository.save(any())).thenReturn(pending);

      // Should not throw - PENDING is allowed for rechunking
      service.triggerRechunking(makeRequest(1L));

      verify(teachingChunkRepository).saveAll(anyList());
    }

    @Test
    void triggerRechunking_embeddedStatus_isAllowed() {
      Teaching embedded = makeTeaching(1L, TeachingStatus.EMBEDDED, "Message");
      ChunkCandidate candidate = makeCandidate(0);
      TeachingChunk chunk = makeChunkEntity(0);

      when(teachingRepository.findById(1L)).thenReturn(Optional.of(embedded));
      when(teachingChunkUtil.chunk("Message")).thenReturn(List.of(candidate));
      when(teachingChunkMapper.candidateToEntity(candidate, embedded)).thenReturn(chunk);
      when(teachingChunkRepository.saveAll(anyList())).thenReturn(List.of(chunk));
      when(teachingRepository.save(any())).thenReturn(embedded);

      service.triggerRechunking(makeRequest(1L));

      verify(teachingChunkRepository).saveAll(anyList());
    }
  }
}