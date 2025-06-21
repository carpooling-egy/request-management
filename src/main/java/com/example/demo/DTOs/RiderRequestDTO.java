package com.example.demo.DTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;


@Data
@NoArgsConstructor
public class RiderRequestDTO extends BaseTripDTO {
    @Future
    private ZonedDateTime earliestDepartureTime;
    @Future
    private ZonedDateTime latestArrivalTime;
    @Min(0)
    private int maxWalkingTimeMinutes;
    @Min(1)
    private int numberOfRiders;
    private boolean isMatched;
}
