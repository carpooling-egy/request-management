package com.example.demo.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


@Data
public class RiderRequestDTO {
    private UUID id;
    private UUID userId;
    private BigDecimal sourceLatitude;
    private BigDecimal sourceLongitude;
    private String sourceAddress;
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;
    private String destinationAddress;
    private ZonedDateTime earliestDepartureTime;
    private ZonedDateTime latestArrivalTime;
    private int maxWalkingTimeMinutes;
    private int numberOfRiders;
    private boolean sameGender;
    private boolean allowsSmoking;
    private boolean allowsPets;
    private boolean isMatched;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    // getters & setters...
}

