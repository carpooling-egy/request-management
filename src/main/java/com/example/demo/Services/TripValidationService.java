package com.example.demo.Services;

import com.example.demo.DAOs.DriverOfferRepository;
import com.example.demo.DAOs.RiderRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class TripValidationService {

    private final RiderRequestRepository riderRepo;
    private final DriverOfferRepository driverRepo;

    private final int minLeadMinutes;
    private final int maxUpcomingTrips;
    private final int maxTripsPerDay;
    private final int maxTripsToday;

    public TripValidationService(
            RiderRequestRepository riderRepo,
            DriverOfferRepository driverRepo,
            @Value("${trip.minLeadTimeMinutes}") int minLeadMinutes,
            @Value("${trip.maxUpcomingTrips}") int maxUpcomingTrips,
            @Value("${trip.maxTripsPerDay}") int maxTripsPerDay,
            @Value("${trip.maxTripsToday}") int maxTripsToday
    ) {
        this.riderRepo         = riderRepo;
        this.driverRepo        = driverRepo;
        this.minLeadMinutes    = minLeadMinutes;
        this.maxUpcomingTrips  = maxUpcomingTrips;
        this.maxTripsPerDay    = maxTripsPerDay;
        this.maxTripsToday     = maxTripsToday;
    }

    public void validateRiderTrip(UUID userId, ZonedDateTime start, ZonedDateTime end) {
        validateTimeOrder(start, end);
        validateLeadTime(start);
        validateNoOverlap(userId, start, end);
        validateUpcomingLimit(userId, start);
        validateDailyLimit(userId, start.toLocalDate());
        validateTodayLimit(userId);
    }

    public void validateDriverTrip(UUID userId, ZonedDateTime start, ZonedDateTime end) {
        validateTimeOrder(start, end);
        validateLeadTime(start);
        validateNoOverlap(userId, start, end);
        validateUpcomingLimit(userId, start);
        validateDailyLimit(userId, start.toLocalDate());
        validateTodayLimit(userId);
    }

    private void validateTimeOrder(ZonedDateTime start, ZonedDateTime end) {
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }
    }

    private void validateLeadTime(ZonedDateTime start) {
        if (start.isBefore(ZonedDateTime.now().plusMinutes(minLeadMinutes))) {
            throw new IllegalArgumentException(
                    "Trips must be scheduled at least " + minLeadMinutes + " minutes in advance."
            );
        }
    }

    private void validateNoOverlap(UUID userId, ZonedDateTime start, ZonedDateTime end) {
        var overlappingRiders = riderRepo.findOverlappingRequests(userId, start, end);
        var overlappingDrivers = driverRepo.findOverlappingOffers(userId, start, end);

        if (!overlappingRiders.isEmpty()) {
            throw new IllegalArgumentException("You have an overlapping rider request.");
        }
        if (!overlappingDrivers.isEmpty()) {
            throw new IllegalArgumentException("You have an overlapping driver offer.");
        }
    }

    private void validateUpcomingLimit(UUID userId, ZonedDateTime start) {
        int upcomingRiders  = riderRepo.countByUserIdAndEarliestDepartureTimeAfter(userId, ZonedDateTime.now());
        int upcomingDrivers = driverRepo.countByUserUuidAndDepartureTimeAfter(userId, ZonedDateTime.now());
        if (upcomingRiders + upcomingDrivers >= maxUpcomingTrips) {
            throw new IllegalArgumentException(
                    "Cannot exceed " + maxUpcomingTrips + " upcoming trips total."
            );
        }
    }

    private void validateDailyLimit(UUID userId, LocalDate date) {
        int ridersOnDate  = riderRepo.countRiderRequestsOnDate(userId, date);
        int driversOnDate = driverRepo.countDriverOffersOnDate(userId, date);
        if (ridersOnDate + driversOnDate >= maxTripsPerDay) {
            throw new IllegalArgumentException(
                    "Cannot schedule more than " + maxTripsPerDay + " trips on " + date + "."
            );
        }
    }

    private void validateTodayLimit(UUID userId) {
        int ridersToday  = riderRepo.countTodayRiderRequests(userId);
        int driversToday = driverRepo.countTodayDriverOffers(userId);
        if (ridersToday + driversToday >= maxTripsToday) {
            throw new IllegalArgumentException(
                    "Cannot create more than " + maxTripsToday + " trips today."
            );
        }
    }
}
