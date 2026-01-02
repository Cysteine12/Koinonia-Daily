package org.eni.koinoniadaily.modules.teaching;

import org.eni.koinoniadaily.modules.teaching.dto.TeachingRequest;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teachings")
@RequiredArgsConstructor
public class TeachingController {
  
  private final TeachingService teachingService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<TeachingResponse>>> getTeachings(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "50") int size
  ) {

    PageResponse<TeachingResponse> teachings = teachingService.getTeachings(page, size);

    return ResponseEntity.ok(SuccessResponse.data(teachings));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<Teaching>> getTeachingById(@PathVariable Long id) {

    Teaching teaching = teachingService.getTeachingById(id);

    return ResponseEntity.ok(SuccessResponse.data(teaching));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Teaching>> createTeaching(@Valid @RequestBody TeachingRequest dto) {

    Teaching createdTeaching = teachingService.createTeaching(dto);

    return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.data(createdTeaching));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Teaching>> updateTeaching(@PathVariable Long id, @Valid @RequestBody TeachingRequest dto) {

    Teaching updatedTeaching = teachingService.updateTeaching(id, dto);

    return ResponseEntity.ok(SuccessResponse.data(updatedTeaching));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> deleteTeaching(@PathVariable Long id) {
    
    teachingService.deleteTeachingById(id);

    return ResponseEntity.ok(SuccessResponse.message("Teaching deleted successfully"));
  }
}
