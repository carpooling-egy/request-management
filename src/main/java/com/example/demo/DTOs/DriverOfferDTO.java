package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Data
public class DriverOfferDTO extends BaseTripDTO {
    @NonNull
    @NotBlank
    private ZonedDateTime departureTime;
    private ZonedDateTime maxEstimatedArrivalTime;
    @NonNull
    @NotBlank
    private int detourTimeMinutes;
    @NonNull
    @NotBlank
    private int capacity;
    private int currentNumberOfRequests;
}
