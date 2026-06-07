package org.eni.koinoniadaily.modules.teachingembedding;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.modules.teachingembedding.dto.TeachingEmbeddingRequest;
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
@RequestMapping("/api/v1/teaching-embeddings")
@RequiredArgsConstructor
@Validated
@PreAuthorize("hasAuthority('ADMIN')")
public class TeachingEmbeddingController {

  private final TeachingEmbeddingService teachingEmbeddingService;

  @PostMapping("/trigger")
  public ResponseEntity<SuccessResponse<Void>> triggerEmbedding(
      @RequestBody @Valid TeachingEmbeddingRequest request
      ) {

    teachingEmbeddingService.triggerEmbedding(request);

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(SuccessResponse.message("Teaching embedded successfully"));
  }
}
