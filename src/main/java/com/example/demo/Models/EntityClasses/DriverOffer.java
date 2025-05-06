package com.example.demo.Models.EntityClasses;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "driver_offers")
public class DriverOffer {

    @Id
    @Column(columnDefinition = "UUID")
    private UUID id;

    @Column(name = "user_uuid", nullable = false, columnDefinition = "UUID")
    private UUID userUuid;

    // source
    @Column(name = "source_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal sourceLatitude;

    @Column(name = "source_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal sourceLongitude;

    @Column(name = "source_address")
    private String sourceAddress;

    // destination
    @Column(name = "destination_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal destinationLatitude;

    @Column(name = "destination_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal destinationLongitude;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "departure_time", nullable = false)
    private ZonedDateTime departureTime;

    @Column(name = "estimated_arrival_time", nullable = false)
    private ZonedDateTime estimatedArrivalTime;

    @Column(name = "detour_time_minutes")
    private int detourTimeMinutes = 0;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "selected_car_id", columnDefinition = "UUID")
    private UUID selectedCarId;

    @Column(name = "current_number_of_requests", nullable = false)
    private int currentNumberOfRequests = 0;

    // Boolean preferences
    @Column(name = "same_gender", nullable = false)
    private boolean sameGender = false;

    @Column(name = "allows_smoking", nullable = false)
    private boolean allowsSmoking = true;

    @Column(name = "allows_pets", nullable = false)
    private boolean allowsPets = true;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // getters & setters...
}
