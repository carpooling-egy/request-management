package com.example.demo.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


@Data
public class RiderRequestDTO {
    private UUID id;
    private UUID userUuid;
    private BigDecimal sourceLatitude;
    private BigDecimal sourceLongitude;
    private String sourceAddress;
    private BigDecimal destinationLatitude;
    private BigDecimal destinationLongitude;
    private String destinationAddress;
    private ZonedDateTime earliestDepartureDatetime;
    private ZonedDateTime latestArrivalDatetime;
    private int maxWalkingTimeMinutes;
    private int numberOfRiders;
    private Long driverOfferId;
    private ZonedDateTime createdAt;
}
