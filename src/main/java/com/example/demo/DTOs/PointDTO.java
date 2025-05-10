package com.example.demo.DTOs;

import lombok.Data;


/**
 * Represents a geographic coordinate with latitude and longitude.
 */

/**
 * New DTO to replace the old PathPointDTO, matching the Go PointDTO structure.
 */
@Data
public class PointDTO {
    /**
     * Type of the owner: e.g., "driver" or "rider".
     */
    public String ownerType;

    /**
     * ID of the owner (driverOfferId or riderRequestId depending on ownerType).
     */
    public String ownerID;

    /**
     * The geographic point (latitude/longitude).
     */
    public CoordinateDTO point;

    /**
     * Expected or actual time at this point, as ISO-8601 string or ZonedDateTime.
     */
    public String time;

    /**
     * Point type: e.g., "pickup" or "dropoff".
     */
    public String pointType;
}
