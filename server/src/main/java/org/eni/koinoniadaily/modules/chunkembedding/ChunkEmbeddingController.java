package org.eni.koinoniadaily.modules.chunkembedding;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.modules.chunkembedding.dto.ChunkEmbeddingRequest;
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
@RequestMapping("/api/v1/chunk-embeddings")
@RequiredArgsConstructor
@Validated
public class ChunkEmbeddingController {

  private final ChunkEmbeddingService chunkEmbeddingService;

  @PostMapping("/trigger")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> triggerEmbedding(
      @RequestBody @Valid ChunkEmbeddingRequest request
  ) {
    chunkEmbeddingService.triggerEmbedding(request);

    return ResponseEntity.status(HttpStatus.ACCEPTED)
        .body(SuccessResponse.message("Teachings embedding triggered successfully"));
  }
}
