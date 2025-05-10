package com.example.demo.DTOs;

import lombok.Data;

import java.util.List;

@Data
public class MatchingResultDTO {
    public String userId;
    public String offerId;
    public List<MatchedRequestDTO> assignedMatchedRequests;
    public List<PointDTO> path;
    public int currentNumberOfRequests;
}
