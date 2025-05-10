package com.example.demo.DTOs;

import lombok.Data;

@Data
public class MatchedRequestDTO {
    public String userId;
    public String requestId;
    public PointDTO pickupPoint;
    public PointDTO dropoffPoint;
}
