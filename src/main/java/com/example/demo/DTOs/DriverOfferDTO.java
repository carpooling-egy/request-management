package com.example.demo.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class DriverOfferDTO {
    private UUID id;
    private UUID userUuid;
    private BigDecimal sourceLatitude;
    private BigDecimal sourceLongitude;
    private String sourceAddress;
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;
    private String destinationAddress;
    private ZonedDateTime departureTime;
    private ZonedDateTime estimatedArrivalTime;
    private int detourTimeMinutes;
    private int capacity;
    private UUID selectedCarId;
    private int currentNumberOfRequests;
    private boolean sameGender;
    private boolean allowsSmoking;
    private boolean allowsPets;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    // getters & setters...
}
