package com.example.demo.DTOs;

import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
public class RideMatchDTO {
    private UUID driverOfferId;
    private UUID riderRequestId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    // getters & setters...
}
