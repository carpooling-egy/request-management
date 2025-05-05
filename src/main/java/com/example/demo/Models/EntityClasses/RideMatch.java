package com.example.demo.Models.EntityClasses;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.ZonedDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ride_matches",
        uniqueConstraints = @UniqueConstraint(columnNames = {"driver_offer_id","rider_request_id"}))
public class RideMatch {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID")
    private UUID pk;       // surrogate key for JPA, not exposed externally

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_offer_id", nullable = false)
    private DriverOffer driverOffer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_request_id", nullable = false)
    private RiderRequest riderRequest;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // getters & setters...
}
