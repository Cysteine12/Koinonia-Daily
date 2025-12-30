package org.eni.koinoniadaily.modules.teaching;

import org.eni.koinoniadaily.exceptions.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeachingService {
  
  private final TeachingRepository teachingRepository;
  private final TeachingMapper teachingMapper;

  public Page<Teaching> getTeachings(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "taughtAt"));
    
    return teachingRepository.findAll(pageable);
  }

  public Teaching getTeachingById(Long id) {

    return teachingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Teaching not found"));
  }

  @Transactional
  public Teaching createTeaching(TeachingDto dto) {

    Teaching teaching = teachingMapper.toEntity(dto);
                          
    return teachingRepository.save(teaching);
  }

  @Transactional
  public Teaching updateTeaching(Long id, TeachingDto dto) {

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
