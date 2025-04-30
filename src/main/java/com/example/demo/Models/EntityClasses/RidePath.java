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
@Table(name = "ride_paths", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"driver_offer_id", "path_order"})
})
public class RidePath {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "driver_offer_id", nullable = false)
    private DriverOffer driverOffer;

    @Column(name = "path_order", nullable = false)
    private int pathOrder;

    @Column(name = "location_type", nullable = false)
    private String locationType; // source, pickup, dropoff, destination

    @Column(name = "latitude", nullable = false, precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "expected_arrival_time", nullable = false)
    private ZonedDateTime expectedArrivalTime;

    @ManyToOne
    @JoinColumn(name = "rider_request_id")
    private RiderRequest riderRequest;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // Getters and setters...
}
