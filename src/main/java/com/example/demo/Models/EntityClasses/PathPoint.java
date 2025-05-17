// PathPoint.java
package com.example.demo.Models.EntityClasses;

import com.example.demo.Enums.PointType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Getter
@Setter
@Table(
        name = "path_point",
        uniqueConstraints = @UniqueConstraint(columnNames = {"driver_offer_id", "path_order"})
)
public class PathPoint {

    @Id
    @Column(length = 50)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_offer_id", nullable = false)
    private DriverOffer driverOffer;

    @Column(name = "path_order", nullable = false)
    private int pathOrder;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PointType type;

    @Column(name = "walking_duration_minutes")
    private int walkingDurationMinutes = 0;

    @Column(name = "latitude", precision = 10, scale = 8)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 11, scale = 8)
    private BigDecimal longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "expected_arrival_time", nullable = false)
    private ZonedDateTime expectedArrivalTime;

    // 1:1 to RiderRequest (enforced unique)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rider_request_id", nullable = false, unique = true)
    private RiderRequest riderRequest;

    @Column(name = "created_at", updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt = ZonedDateTime.now();
}
