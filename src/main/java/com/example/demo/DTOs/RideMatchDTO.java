package com.example.demo.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class RideMatchDTO {
    private UUID id;
    private Long driverOfferId;
    private int pickupOrder;
    private UUID riderRequestId;
    private BigDecimal pickupLatitude;
    private BigDecimal pickupLongitude;
    private String pickupAddress;
    private ZonedDateTime pickupTime;
    private BigDecimal dropoffLatitude;
    private BigDecimal dropoffLongitude;
    private String dropoffAddress;
    private ZonedDateTime dropoffTime;
    private boolean sameGender;
    private boolean allowsSmoking;
    private boolean allowsPets;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
