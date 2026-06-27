package org.eni.koinoniadaily.modules.search.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class SearchClickRequest {

    @NotNull(message = "teachingId is required")
    @Positive(message = "teachingId must be greater than zero")
    private Long teachingId;
}
