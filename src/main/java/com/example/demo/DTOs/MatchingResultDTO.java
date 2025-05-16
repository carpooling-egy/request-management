package com.example.demo.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class MatchingResultDTO {
    private String userId;
    private String offerId;
    private List<MatchedRequestDTO> assignedMatchedRequests;
    private List<PointDTO> path;
    private int currentNumberOfRequests;
}
