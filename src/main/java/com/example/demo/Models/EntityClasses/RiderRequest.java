package com.example.demo.Models.EntityClasses;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;


@Getter
@Setter
@Entity
@Table(name = "rider_requests")
public class RiderRequest {

    @Id
    private UUID id;

    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "source_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal sourceLatitude;

    @Column(name = "source_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal sourceLongitude;

    @Column(name = "source_address")
    private String sourceAddress;

    @Column(name = "destination_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal destinationLatitude;

    @Column(name = "destination_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal destinationLongitude;

    @Column(name = "destination_address")
    private String destinationAddress;

    @Column(name = "earliest_departure_datetime", nullable = false)
    private ZonedDateTime earliestDepartureDatetime;

    @Column(name = "latest_arrival_datetime")
    private ZonedDateTime latestArrivalDatetime;

    @Column(name = "max_walking_time_minutes")
    private int maxWalkingTimeMinutes = 5;

    @Column(name = "number_of_riders", nullable = false)
    private int numberOfRiders = 1;

    @ManyToOne
    @JoinColumn(name = "driver_offer_id")
    private DriverOffer driverOffer;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    // Getters and setters...
}
