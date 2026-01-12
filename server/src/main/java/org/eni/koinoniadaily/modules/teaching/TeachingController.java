package org.eni.koinoniadaily.modules.teaching;

import org.eni.koinoniadaily.modules.teaching.dto.TeachingPageResponse;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingRequest;
import org.eni.koinoniadaily.modules.teaching.dto.TeachingResponse;
import org.eni.koinoniadaily.utils.PageResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teachings")
@RequiredArgsConstructor
@Validated
public class TeachingController {
  
  private final TeachingService teachingService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<TeachingPageResponse>>> getTeachings(
      @RequestParam(defaultValue = "0") @NotNull @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @NotNull @Positive int size
  ) {
    PageResponse<TeachingPageResponse> response = teachingService.getTeachings(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/search")
  public ResponseEntity<SuccessResponse<PageResponse<TeachingPageResponse>>> searchTeachings(
      @RequestParam @NotBlank String q,
      @RequestParam(defaultValue = "0") @NotNull @PositiveOrZero int page,
      @RequestParam(defaultValue = "50") @NotNull @Positive int size
  ) {
    PageResponse<TeachingPageResponse> response = teachingService.searchTeachings(q, page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<TeachingResponse>> getTeachingById(
      @PathVariable @NotNull @Positive Long id
  ) {
    TeachingResponse response = teachingService.getTeachingById(id);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<TeachingResponse>> createTeaching(
      @RequestBody @Valid TeachingRequest request
  ) {
    TeachingResponse response = teachingService.createTeaching(request);

    return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of(response, "Teaching created successfully"));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<TeachingResponse>> updateTeaching(
      @PathVariable @NotNull @Positive Long id, 
      @RequestBody @Valid TeachingRequest request
  ) {
    TeachingResponse response = teachingService.updateTeaching(id, request);

    return ResponseEntity.ok(SuccessResponse.of(response, "Teaching updated successfully"));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> deleteTeaching(
      @PathVariable @NotNull @Positive Long id
  ) {
    
    teachingService.deleteTeachingById(id);

    return ResponseEntity.ok(SuccessResponse.message("Teaching deleted successfully"));
  }
}
