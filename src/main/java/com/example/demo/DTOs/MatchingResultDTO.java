package com.example.demo.DTOs;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
public class MatchingResultDTO {

    @NotBlank
    private String userId;
    @NotBlank
    private String offerId;
    @NotEmpty
    private List<MatchedRequestDTO> assignedMatchedRequests;
    @NotEmpty
    private List<PointDTO> path;
    @Min(0)
    private int currentNumberOfRequests;
}
