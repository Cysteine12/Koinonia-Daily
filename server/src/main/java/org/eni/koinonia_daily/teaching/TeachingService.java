package org.eni.koinonia_daily.teaching;

import java.util.List;

import org.eni.koinonia_daily.utils.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeachingService {
  
  private final TeachingRepository teachingRepository;
  private final TeachingMapper teachingMapper;

  public List<Teaching> findAll() {
    return teachingRepository.findAll();
  }

  public Teaching findById(Long id) {
    return teachingRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Teaching not found"));
  }

  @Transactional
  public Teaching create(TeachingDto dto) {
    Teaching teaching = teachingMapper.toEntity(dto);
                          
    return teachingRepository.save(teaching);
  }

  @Transactional
  public Teaching update(Long id, TeachingDto dto) {
    return teachingRepository.findById(id)
            .map(teaching -> {
              teaching = teachingMapper.updateToEntity(teaching, dto);
              
              return teachingRepository.save(teaching);
            })
            .orElseThrow(() -> new NotFoundException("Teaching not found"));
  }

  @Transactional
  public void delete(Long id) {
    if (!teachingRepository.existsById(id)) {
      throw new NotFoundException("Teaching not found");
    }
    
    teachingRepository.deleteById(id);
  }
}
