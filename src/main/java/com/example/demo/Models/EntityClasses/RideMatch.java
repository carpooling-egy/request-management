package com.example.demo.Models.EntityClasses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "ride_matches")
@IdClass(RideMatchId.class)
public class RideMatch {

    @Id
    @Column(name = "driver_offer_id")
    private UUID driverOfferId;

    @Id
    @Column(name = "rider_request_id")
    private UUID riderRequestId;

    @OneToOne
    @JoinColumn(name = "pickup_point_id", nullable = false)
    private PathPoint pickupPoint;

    @OneToOne
    @JoinColumn(name = "dropoff_point_id", nullable = false)
    private PathPoint dropoffPoint;

    @Column(name = "created_at")
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    // Getters and setters omitted for brevity
}


