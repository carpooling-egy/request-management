package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public abstract class BaseTripDTO {

    private String id;
    @NotBlank
    private String userId;
    @NotNull
    private BigDecimal sourceLatitude;
    @NotNull
    private BigDecimal sourceLongitude;
    @NotBlank
    private String sourceAddress;
    @NotNull
    private BigDecimal destinationLatitude;
    @NotNull
    private BigDecimal destinationLongitude;
    @NotBlank
    private String destinationAddress;

    // common preferences
    private boolean sameGender = false;
    
    // audit
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;
}
