package org.eni.koinoniadaily.modules.recommendation;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.eni.koinoniadaily.modules.recommendation.dto.RecommendationResponse;
import org.eni.koinoniadaily.utils.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Validated
public class RecommendationController {

  private final RecommendationService recommendationService;

  @GetMapping("/{teachingId}")
  public ResponseEntity<SuccessResponse<List<RecommendationResponse>>> getRecommendations(
      @PathVariable @Positive Long teachingId,
      @RequestParam(defaultValue = "10") @Positive @Max(50) int size
  ) {
    List<RecommendationResponse> response = recommendationService.getRecommendations(teachingId, size);

    return ResponseEntity.ok(SuccessResponse.data(response));
  }
}
