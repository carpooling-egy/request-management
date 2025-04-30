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
@Table(name = "ride_matches", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"driver_offer_id", "rider_request_id"})
})
public class RideMatch {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "driver_offer_id", nullable = false)
    private DriverOffer driverOffer;

    @Column(name = "pickup_order", nullable = false)
    private int pickupOrder;

    @ManyToOne
    @JoinColumn(name = "rider_request_id", nullable = false)
    private RiderRequest riderRequest;

    @Column(name = "pickup_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal pickupLongitude;

    @Column(name = "pickup_address")
    private String pickupAddress;

    @Column(name = "pickup_time", nullable = false)
    private ZonedDateTime pickupTime;

    @Column(name = "dropoff_latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal dropoffLatitude;

    @Column(name = "dropoff_longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal dropoffLongitude;

    @Column(name = "dropoff_address")
    private String dropoffAddress;

    @Column(name = "dropoff_time", nullable = false)
    private ZonedDateTime dropoffTime;

    @Column(name = "same_gender", nullable = false)
    private boolean sameGender = false;

    @Column(name = "allows_smoking", nullable = false)
    private boolean allowsSmoking = true;

    @Column(name = "allows_pets", nullable = false)
    private boolean allowsPets = true;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // Getters and setters...
}
