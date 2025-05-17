// RideMatch.java
package com.example.demo.Models.EntityClasses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(name = "ride_matches")
@IdClass(RideMatchId.class)
public class RideMatch {

    @Id
    @Column(name = "driver_offer_id", length = 50)
    private String driverOfferId;

    @Id
    @Column(name = "rider_request_id", length = 50)
    private String riderRequestId;

    // back to DriverOffer
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("driverOfferId")
    @JoinColumn(name = "driver_offer_id", nullable = false)
    private DriverOffer driverOffer;

    // back to RiderRequest
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId("riderRequestId")
    @JoinColumn(name = "rider_request_id", nullable = false, unique = true)
    private RiderRequest riderRequest;

    @OneToOne
    @JoinColumn(name = "pickup_point_id", nullable = false)
    private PathPoint pickupPoint;

    @OneToOne
    @JoinColumn(name = "dropoff_point_id", nullable = false)
    private PathPoint dropoffPoint;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();
}
