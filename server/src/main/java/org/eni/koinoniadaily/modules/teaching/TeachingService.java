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
  public Teaching getTeachingById(Long id) {

    historyService.createHistory(id);

    return teachingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Teaching not found"));
  }

  @Transactional
  public Teaching createTeaching(TeachingRequest dto) {

    Teaching teaching = teachingMapper.toEntity(dto);
                          
    return teachingRepository.save(teaching);
  }

  @Transactional
  public Teaching updateTeaching(Long id, TeachingRequest dto) {

    return teachingRepository.findById(id)
            .map(teaching -> {
              teaching = teachingMapper.updateToEntity(teaching, dto);
              
              return teachingRepository.save(teaching);
            })
            .orElseThrow(() -> new NotFoundException("Teaching not found"));
  }

  @Transactional
  public void deleteTeachingById(Long id) {
    
    if (!teachingRepository.existsById(id)) {
      throw new NotFoundException("Teaching not found");
    }
    
    teachingRepository.deleteById(id);
  }
}
