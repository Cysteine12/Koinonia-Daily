package org.eni.koinoniadaily.modules.transcript;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptPageResponse;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptRequest;
import org.eni.koinoniadaily.modules.transcript.dto.TranscriptResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TranscriptService {
  

  private final TranscriptRepository transcriptRepository;
  private final TranscriptMapper transcriptMapper;
  private static final String UPDATED_AT = "updatedAt";

  public PageResponse<TranscriptPageResponse> getTranscripts(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));

    Page<TranscriptPageResponse> transcripts = transcriptRepository.findAllBy(pageable)
                                                .map(transcriptMapper::toDto);

    return PageResponse.from(transcripts);
  }

  public PageResponse<TranscriptPageResponse> searchTranscripts(String query, int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, UPDATED_AT));
    
    Page<TranscriptPageResponse> transcripts = transcriptRepository.findByTitleContainingIgnoreCase(query, pageable)
                                            .map(transcriptMapper::toDto);

    return PageResponse.from(transcripts);
  }

  public TranscriptResponse getTranscriptById(Long id) {

    Transcript transcript = transcriptRepository.findById(id)
                              .orElseThrow(() -> new NotFoundException("Transcript not found"));

    return transcriptMapper.toDto(transcript);
  }

  @Transactional
  public TranscriptResponse createTranscript(TranscriptRequest request) {
    
    Transcript transcript = transcriptRepository.save(transcriptMapper.toEntity(request));

    return transcriptMapper.toDto(transcript);
  }

  @Transactional
  public TranscriptResponse updateTranscript(Long id, TranscriptRequest request) {

    Transcript transcript = transcriptRepository.findById(id)
                              .orElseThrow(() -> new NotFoundException("Transcript not found"));

    transcript.setTitle(request.getTitle());
    transcript.setMessage(request.getMessage());

    return transcriptMapper.toDto(transcript);
  }

  @Transactional
  public void deleteTranscript(Long id) {

    Transcript transcript = transcriptRepository.findById(id)
                              .orElseThrow(() -> new NotFoundException("Transcript not found"));

    transcriptRepository.delete(transcript);
  }
}
