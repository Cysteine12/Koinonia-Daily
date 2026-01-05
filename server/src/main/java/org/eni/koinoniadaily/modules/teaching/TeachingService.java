package org.eni.koinoniadaily.modules.teaching;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.eni.koinoniadaily.modules.history.HistoryService;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingRequest;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingResponse;
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
public class TeachingService {
  
  private final TeachingRepository teachingRepository;
  private final TeachingMapper teachingMapper;
  private final HistoryService historyService;
  private static final String TAUGHT_AT = "taughtAt";

  public PageResponse<TeachingResponse> getTeachings(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, TAUGHT_AT));
    
    Page<TeachingResponse> teachings = teachingRepository.findAll(pageable)
                            .map(teachingMapper::toDto);

    return PageResponse.from(teachings);
  }

  @Transactional
  public TeachingResponse getTeachingById(Long id) {

    historyService.createOrUpdateHistory(id);

    return teachingRepository.findById(id)
            .map(teachingMapper::toDto)
            .orElseThrow(() -> new NotFoundException("Teaching not found"));
  }

  @Transactional
  public TeachingResponse createTeaching(TeachingRequest request) {

    Teaching teaching = teachingMapper.toEntity(request);
                          
    Teaching savedTeaching = teachingRepository.save(teaching);

    return teachingMapper.toDto(savedTeaching);
  }

  @Transactional
  public TeachingResponse updateTeaching(Long id, TeachingRequest request) {

    Teaching teaching = teachingRepository.findById(id)
                          .orElseThrow(() -> new NotFoundException("Teaching not found"));

    teachingMapper.updateToEntity(teaching, request);

    return teachingMapper.toDto(teaching);
  }

  @Transactional
  public void deleteTeachingById(Long id) {
    
    if (!teachingRepository.existsById(id)) {
      throw new NotFoundException("Teaching not found");
    }
    
    teachingRepository.deleteById(id);
  }
}
