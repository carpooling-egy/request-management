package com.example.demo.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


@Data
public class PathPointDTO {
    private UUID id;
    private UUID driverOfferId;
    private int pathOrder;
    private String locationType;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private ZonedDateTime expectedArrivalTime;
    private UUID riderRequestId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    // getters & setters...
}
