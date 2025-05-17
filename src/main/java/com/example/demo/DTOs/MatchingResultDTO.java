package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;


@Data
@NoArgsConstructor
public class MatchingResultDTO {

    @NotBlank
    @NonNull
    private String userId;
    @NotBlank
    @NonNull
    private String offerId;
    @NonNull
    @NotEmpty
    private List<MatchedRequestDTO> assignedMatchedRequests;
    @NonNull
    @NotEmpty
    private List<PointDTO> path;
    @NotBlank
    private int currentNumberOfRequests;
}
