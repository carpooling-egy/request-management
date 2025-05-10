package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.time.ZonedDateTime;


@Data
public class RiderRequestDTO extends BaseTripDTO {
    @NonNull
    @NotBlank
    private ZonedDateTime earliestDepartureTime;
    @NonNull
    @NotBlank
    private ZonedDateTime latestArrivalTime;
    @NonNull
    @NotBlank
    private int maxWalkingTimeMinutes;
    @NonNull
    @NotBlank
    private int numberOfRiders;
    private boolean isMatched;
}
