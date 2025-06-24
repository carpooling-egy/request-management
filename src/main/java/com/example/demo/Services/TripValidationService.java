package com.example.demo.Services;

import com.example.demo.DAOs.DriverOfferRepository;
import com.example.demo.DAOs.RiderRequestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Service
public class TripValidationService {

    private final RiderRequestRepository riderRepo;
    private final DriverOfferRepository driverRepo;
    private final RouteService routeService;

    private final int minLeadMinutes;
    private final int maxUpcomingTrips;
    private final int maxTripsPerDay;
    private final int maxTripsToday;

    public TripValidationService(
            RiderRequestRepository riderRepo,
            DriverOfferRepository driverRepo,
            RouteService routeService,
            @Value("${trip.min-lead-time-minutes}") int minLeadMinutes,
            @Value("${trip.max-upcoming-trips}") int maxUpcomingTrips,
            @Value("${trip.max-trips-per-day}") int maxTripsPerDay,
            @Value("${trip.max-trips-today}") int maxTripsToday
    ) {
        this.riderRepo        = riderRepo;
        this.driverRepo       = driverRepo;
        this.routeService     = routeService;
        this.minLeadMinutes   = minLeadMinutes;
        this.maxUpcomingTrips = maxUpcomingTrips;
        this.maxTripsPerDay   = maxTripsPerDay;
        this.maxTripsToday    = maxTripsToday;
    }

    public void validateRiderTrip(
            String userId,
            ZonedDateTime start,
            ZonedDateTime end,
            double srcLat,
            double srcLon,
            double dstLat,
            double dstLon
    ) {
        validateTravelTime(start, end, srcLat, srcLon, dstLat, dstLon);
        validateTimeOrder(start, end);
        validateLeadTime(start);
        validateNoOverlap(userId, start, end);
        validateUpcomingLimit(userId);
        validateDailyLimit(userId, start.toLocalDate());
        validateTodayLimit(userId);
    }

    public void validateDriverTrip(String userId, ZonedDateTime start, ZonedDateTime end) {
        validateTimeOrder(start, end);
        validateLeadTime(start);
        validateNoOverlap(userId, start, end);
        validateUpcomingLimit(userId);
        validateDailyLimit(userId, start.toLocalDate());
        validateTodayLimit(userId);
    }

    private void validateTravelTime(
            ZonedDateTime departure,
            ZonedDateTime latestArrival,
            double srcLat,
            double srcLon,
            double dstLat,
            double dstLon
    ) {
        double travelMinutes = routeService.getTravelTimeMinutes(
                srcLat, srcLon, dstLat, dstLon, departure
        );
        ZonedDateTime minAllowedArrival = departure.plusMinutes((long) Math.ceil(travelMinutes));
        if (latestArrival.isBefore(minAllowedArrival)) {
            throw new IllegalArgumentException(
                    String.format(
                            "Latest arrival must be ≥ departure + travel time (%.0f min): earliest allowed %s",
                            travelMinutes, minAllowedArrival
                    )
            );
        }
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

    private void validateNoOverlap(String userId, ZonedDateTime start, ZonedDateTime end) {
        var overlappingRiders  = riderRepo.findOverlappingRequests(userId, start, end);
        var overlappingDrivers = driverRepo.findOverlappingOffers(userId, start, end);

        if (!overlappingRiders.isEmpty()) {
            throw new IllegalArgumentException("You have an overlapping rider request.");
        }
        if (!overlappingDrivers.isEmpty()) {
            throw new IllegalArgumentException("You have an overlapping driver offer.");
        }
    }

    private void validateUpcomingLimit(String userId) {
        int upcomingRiders  = riderRepo.countByUserIdAndEarliestDepartureTimeAfter(userId, ZonedDateTime.now());
        int upcomingDrivers = driverRepo.countByUserIdAndDepartureTimeAfter(userId, ZonedDateTime.now());
        if (upcomingRiders + upcomingDrivers >= maxUpcomingTrips) {
            throw new IllegalArgumentException(
                    "Cannot exceed " + maxUpcomingTrips + " upcoming trips total."
            );
        }
    }

    private void validateDailyLimit(String userId, LocalDate date) {
        int ridersOnDate  = riderRepo.countRiderRequestsOnDate(userId, date);
        int driversOnDate = driverRepo.countDriverOffersOnDate(userId, date);
        if (ridersOnDate + driversOnDate >= maxTripsPerDay) {
            throw new IllegalArgumentException(
                    "Cannot schedule more than " + maxTripsPerDay + " trips on " + date + "."
            );
        }
    }

    private void validateTodayLimit(String userId) {
        int ridersToday  = riderRepo.countTodayRiderRequests(userId);
        int driversToday = driverRepo.countTodayDriverOffers(userId);
        if (ridersToday + driversToday >= maxTripsToday) {
            throw new IllegalArgumentException(
                    "Cannot create more than " + maxTripsToday + " trips today."
            );
        }
    }
}
