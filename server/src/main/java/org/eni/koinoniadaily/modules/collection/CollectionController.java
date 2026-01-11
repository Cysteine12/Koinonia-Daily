package org.eni.koinoniadaily.modules.collection;

import org.eni.koinoniadaily.modules.collection.dto.CollectionPageResponse;
import org.eni.koinoniadaily.modules.collection.dto.CollectionRequest;
import org.eni.koinoniadaily.modules.collection.dto.CollectionResponse;
import org.eni.koinoniadaily.modules.collection.dto.CollectionTeachingRequest;
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
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/collections")
@RequiredArgsConstructor
@Validated
public class CollectionController {
  
  private final CollectionService collectionService;

  @GetMapping
  public ResponseEntity<SuccessResponse<PageResponse<CollectionPageResponse>>> getCollections(
      @RequestParam @NotNull @PositiveOrZero int page,
      @RequestParam @NotNull @Positive int size
  ) {
    PageResponse<CollectionPageResponse> response = collectionService.getCollections(page, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @GetMapping("/{id}")
  public ResponseEntity<SuccessResponse<CollectionResponse>> getCollectionById(
      @PathVariable @NotNull @Positive Long id
  ) {
    CollectionResponse response = collectionService.getCollectionById(id);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<CollectionResponse>> createCollection(
      @RequestBody @Valid CollectionRequest request
  ) {
    CollectionResponse response = collectionService.createCollection(request);

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(SuccessResponse.of(response, "Collection created successfully"));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<CollectionResponse>> updateCollection(
    @PathVariable @NotNull @Positive Long id,
    @RequestBody @Valid CollectionRequest request
  ) {
    CollectionResponse response = collectionService.updateCollection(id, request);

    return ResponseEntity.ok(SuccessResponse.of(response, "Collection updated successfully"));
  }

  @PostMapping("/{id}/teachings")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> addCollectionTeaching(
    @PathVariable @NotNull @Positive Long id,
    @RequestBody @Valid CollectionTeachingRequest request
  ) {
    collectionService.addCollectionTeaching(id, request);

    return ResponseEntity.ok(SuccessResponse.message("Teaching added successfully"));
  }

  @DeleteMapping("/{id}/teachings/{teachingId}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> removeCollectionTeaching(
    @PathVariable @NotNull @Positive Long id,
    @PathVariable @NotNull @Positive Long teachingId
  ) {
    collectionService.removeCollectionTeaching(id, teachingId);

    return ResponseEntity.ok(SuccessResponse.message("Teaching removed successfully"));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<SuccessResponse<Void>> deleteCollection(
    @PathVariable @NotNull @Positive Long id
  ) {
    collectionService.deleteCollection(id);

    return ResponseEntity.ok(SuccessResponse.message("Collection deleted successfully"));
  }
}
