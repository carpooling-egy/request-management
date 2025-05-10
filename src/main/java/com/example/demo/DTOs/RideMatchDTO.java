package com.example.demo.DTOs;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class RideMatchDTO {
    private String driverOfferId;
    private String riderRequestId;
    private String pickupPointId;
    private String dropoffPointId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
