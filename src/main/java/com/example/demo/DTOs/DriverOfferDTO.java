package com.example.demo.DTOs;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class DriverOfferDTO extends BaseTripDTO {
    @Future
    private ZonedDateTime departureTime;
    private ZonedDateTime maxEstimatedArrivalTime;
    @Min(0)
    private int detourTimeMinutes;
    @Min(1)
    private int capacity;
    private int currentNumberOfRequests;
}
