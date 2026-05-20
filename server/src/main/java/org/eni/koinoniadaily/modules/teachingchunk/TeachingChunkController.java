package org.eni.koinoniadaily.modules.teachingchunk;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.modules.teachingchunk.dto.TeachingChunkRequest;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/teaching-chunks")
@RequiredArgsConstructor
@Validated
public class TeachingChunkController {

  private final TeachingChunkService teachingChunkService;

  @PostMapping("/trigger")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> triggerChunking(
      @RequestBody @Valid TeachingChunkRequest request
  ) {

    teachingChunkService.triggerChunking(request);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(SuccessResponse.message("Teaching chunked successfully"));
  }

  @PostMapping("/trigger/rechunk")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> triggerRechunking(
      @RequestBody @Valid TeachingChunkRequest request
  ) {

    teachingChunkService.triggerRechunking(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.message("Teaching rechunked successfully"));
  }
}
