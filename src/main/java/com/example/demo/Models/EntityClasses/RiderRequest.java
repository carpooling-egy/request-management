// RiderRequest.java
package com.example.demo.Models.EntityClasses;

import com.example.demo.Enums.GenderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "rider_requests")
public class RiderRequest {

    @Id
    @Column(length = 50)
    private String id;

    @Column(name = "user_id", nullable = false, length = 50)
    private String userId;

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

    @Column(name = "earliest_departure_time", nullable = false)
    private ZonedDateTime earliestDepartureTime;

    @Column(name = "latest_arrival_time", nullable = false)
    private ZonedDateTime latestArrivalTime;

    @Column(name = "max_walking_duration_minutes")
    private int maxWalkingDurationMinutes = 5;

    @Column(name = "number_of_riders", nullable = false)
    private int numberOfRiders = 1;

    // Boolean preferences
    @Column(name = "same_gender", nullable = false)
    private boolean sameGender = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private GenderType userGender;

    @Column(name = "is_matched")
    private boolean isMatched = false;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // 1:1 back ref to PathPoint
    @OneToOne(mappedBy = "riderRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private PathPoint pathPoint;

    // 1:1 back ref to RideMatch
    @OneToOne(mappedBy = "riderRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private RideMatch rideMatch;
}
