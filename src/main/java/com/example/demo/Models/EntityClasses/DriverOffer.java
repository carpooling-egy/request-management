package com.example.demo.Models.EntityClasses;

import com.example.demo.Enums.GenderType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "driver_offers")
public class DriverOffer {

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

    @Column(name = "departure_time", nullable = false)
    private ZonedDateTime departureTime;

    @Column(name = "max_estimated_arrival_time", nullable = false)
    private ZonedDateTime maxEstimatedArrivalTime;

    @Column(name = "estimated_arrival_time")
    private ZonedDateTime estimatedArrivalTime;

    @Column(name = "detour_duration_minutes")
    private int detourDurationMinutes = 0;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "current_number_of_requests", nullable = false)
    private int currentNumberOfRequests = 0;

    // Boolean preferences
    @Column(name = "same_gender", nullable = false)
    private boolean sameGender = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_gender", nullable = false)
    private GenderType userGender;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // 1:N to PathPoint
    @OneToMany(mappedBy = "driverOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PathPoint> pathPoints = new ArrayList<>();

    // 1:N to RideMatch
    @OneToMany(mappedBy = "driverOffer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RideMatch> rideMatches = new ArrayList<>();
}