package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public abstract class BaseTripDTO {

    private String id;
    @NonNull
    @NotBlank
    private String userId;
    @NonNull
    @NotBlank
    private BigDecimal sourceLatitude;
    @NonNull
    @NotBlank
    private BigDecimal sourceLongitude;
    @NonNull
    @NotBlank
    private String sourceAddress;
    @NonNull
    @NotBlank
    private BigDecimal destinationLatitude;
    @NonNull
    @NotBlank
    private BigDecimal destinationLongitude;
    @NonNull
    @NotBlank
    private String destinationAddress;

    // common preferences
    @NotBlank
    private boolean sameGender;
    @NotBlank
    private boolean allowsSmoking;
    @NotBlank
    private boolean allowsPets;
    //private GenderType userGender;

    // audit
    @NonNull
    @NotBlank
    private ZonedDateTime createdAt;
    @NonNull
    @NotBlank
    private ZonedDateTime updatedAt;
}
