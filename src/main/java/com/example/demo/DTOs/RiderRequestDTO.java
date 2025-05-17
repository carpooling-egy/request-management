package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.ZonedDateTime;


@Data
@NoArgsConstructor
public class RiderRequestDTO extends BaseTripDTO {
    @NonNull
    @NotBlank
    private ZonedDateTime earliestDepartureTime;
    @NonNull
    @NotBlank
    private ZonedDateTime latestArrivalTime;
    @NotBlank
    private int maxWalkingTimeMinutes;
    @NotBlank
    private int numberOfRiders;
    private boolean isMatched;
}
