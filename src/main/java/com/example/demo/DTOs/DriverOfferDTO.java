package com.example.demo.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class DriverOfferDTO extends BaseTripDTO {
    @NonNull
    @NotBlank
    private ZonedDateTime departureTime;
    private ZonedDateTime maxEstimatedArrivalTime;
    @NotBlank
    private int detourTimeMinutes;
    @NotBlank
    private int capacity;
    private int currentNumberOfRequests;
}
