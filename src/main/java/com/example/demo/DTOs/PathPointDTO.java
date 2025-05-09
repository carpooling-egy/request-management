package com.example.demo.DTOs;

import com.example.demo.Enums.PointType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


@Data
public class PathPointDTO {
    private UUID id;
    private UUID driverOfferId;
    private int pathOrder;
    private PointType type;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String address;
    private ZonedDateTime expectedArrivalTime;
    private UUID riderRequestId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
    // getters & setters...
}
