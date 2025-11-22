package org.eni.koinonia_daily.modules.teaching;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teachings")
@RequiredArgsConstructor
public class TeachingController {
  
  private final TeachingService teachingService;

  @GetMapping
  public ResponseEntity<List<Teaching>> getTeachings() {
    List<Teaching> teachings = teachingService.findAll();
    return ResponseEntity.ok(teachings);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Teaching> getTeaching(@PathVariable Long id) {
    Teaching teaching = teachingService.findById(id);
    return ResponseEntity.ok(teaching);
  }

  @PostMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public ResponseEntity<Teaching> createTeaching(@Valid @RequestBody TeachingDto dto) {
    Teaching createdTeaching = teachingService.create(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(createdTeaching);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Teaching> updateTeaching(@Valid @PathVariable Long id, @RequestBody TeachingDto dto) {
    Teaching updatedTeaching = teachingService.update(id, dto);
    return ResponseEntity.ok(updatedTeaching);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTeaching(@PathVariable Long id) {
    teachingService.delete(id);

    return ResponseEntity.noContent().build();
  }
}
