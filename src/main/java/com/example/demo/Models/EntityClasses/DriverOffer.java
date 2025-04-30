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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    @Column(name = "departure_datetime", nullable = false)
    private ZonedDateTime departureDatetime;

    @Column(name = "detour_time_minutes")
    private int detourTimeMinutes = 0;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "external_car_id")
    private UUID externalCarId;

    @Column(name = "same_gender", nullable = false)
    private boolean sameGender = false;

    @Column(name = "allows_smoking", nullable = false)
    private boolean allowsSmoking = true;

    @Column(name = "allows_pets", nullable = false)
    private boolean allowsPets = true;

    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    // Getters and setters...
}
