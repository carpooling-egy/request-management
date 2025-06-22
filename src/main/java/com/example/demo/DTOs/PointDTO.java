package com.example.demo.DTOs;

import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Represents a geographic coordinate with latitude and longitude.
 */

/**
 * New DTO to replace the old PathPointDTO, matching the Go PointDTO structure.
 */
@Data
@NoArgsConstructor
public class PointDTO {
    /**
     * Type of the owner: e.g., "driver" or "rider".
     */
    private String ownerType;

    /**
     * ID of the owner (driverOfferId or riderRequestId depending on ownerType).
     */
    private String ownerID;

    /**
     * The geographic point (latitude/longitude).
     */
    private CoordinateDTO point;

    /**
     * Expected or actual time at this point, as ISO-8601 string or ZonedDateTime.
     */
    private String time;

    /**
     * Point type: e.g., "pickup" or "dropoff".
     */
    private String pointType;

    private int walkingDurationMinutes;
}
