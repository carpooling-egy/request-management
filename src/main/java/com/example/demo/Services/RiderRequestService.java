package com.example.demo.Services;


import com.example.demo.DTOs.RiderRequestDTO;
import com.example.demo.Models.EntityClasses.*;
import com.example.demo.DAOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;


@Service
public class RiderRequestService {

    private static final int MINUTES_BEFORE      = 30;
    private static final int MAX_TOTAL_UPCOMING_TRIPS = 30;
    private static final int MAX_TOTAL_TRIPS_IN_DAY    = 5;
    private static final int MAX_TOTAL_REQUESTS_TODAY = 5;


    @Autowired
    private RiderRequestRepository riderRequestRepository;

    @Autowired
    private DriverOfferRepository driverOfferRepository;

    public RiderRequest createRiderRequest(RiderRequestDTO dto) {

        UUID userId = dto.getUserId();
        ZonedDateTime now      = ZonedDateTime.now();
        ZonedDateTime start    = dto.getEarliestDepartureTime();
        ZonedDateTime end      = dto.getLatestArrivalTime();

        // 1) end time must be after start time
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        // 2) 30 min lead
        if (start.isBefore(now.plusMinutes(MINUTES_BEFORE))) {
            throw new IllegalArgumentException(
                    "Earliest departure must be at least " + MINUTES_BEFORE + " minutes from now."
            );
        }

        // 3a) no overlap with existing rider requests
        if (!riderRequestRepository
                .findOverlappingRequests(userId, start, end)
                .isEmpty()
        ) {
            throw new IllegalArgumentException("You have an overlapping rider request.");
        }

        // 3b) no overlap with existing driver offers
        if (!driverOfferRepository
                .findOverlappingOffers(userId, start, end)
                .isEmpty()
        ) {
            throw new IllegalArgumentException("You have an overlapping driver offer.");
        }

        // 4) total upcoming trips limit
        int upcomingRequests = riderRequestRepository
                .countByUserIdAndEarliestDepartureTimeAfter(userId, now);
        int upcomingOffers   = driverOfferRepository
                .countByUserUuidAndDepartureTimeAfter(userId, now);

        if (upcomingRequests + upcomingOffers >= MAX_TOTAL_UPCOMING_TRIPS) {
            throw new IllegalArgumentException(
                    "You cannot have more than " + MAX_TOTAL_UPCOMING_TRIPS + " upcoming trips."
            );
        }

        // 5) no more than 5 requests in a day
        int driverOffersOnDate = driverOfferRepository
                .countDriverOffersOnDate(userId, start.toLocalDate());
        int riderRequestsOnDate = riderRequestRepository
                .countRiderRequestsOnDate(userId, start.toLocalDate());

        if (driverOffersOnDate + riderRequestsOnDate >= MAX_TOTAL_TRIPS_IN_DAY) {
            throw new IllegalArgumentException(
                    "You cannot have more than " + MAX_TOTAL_TRIPS_IN_DAY + " in a" + start.toLocalDate() + "."
            );
        }


        // 6) no more than 5 requests today
        int todayDriverOffers = driverOfferRepository
                .countTodayDriverOffers(userId);

        int todayRiderRequests = riderRequestRepository
                .countTodayRiderRequests(userId);

        if (todayDriverOffers + todayRiderRequests >= MAX_TOTAL_REQUESTS_TODAY) {
            throw new IllegalArgumentException(
                    "You cannot have more than " + MAX_TOTAL_REQUESTS_TODAY + " trips today."
            );
        }


        RiderRequest request = new RiderRequest();

        // generate UUID for new record
        request.setId(UUID.randomUUID());
        request.setUserId(dto.getUserId());

        // source
        request.setSourceLatitude(dto.getSourceLatitude());
        request.setSourceLongitude(dto.getSourceLongitude());
        request.setSourceAddress(dto.getSourceAddress());

        // destination
        request.setDestinationLatitude(dto.getDestinationLatitude());
        request.setDestinationLongitude(dto.getDestinationLongitude());
        request.setDestinationAddress(dto.getDestinationAddress());

        // times
        request.setEarliestDepartureTime(dto.getEarliestDepartureTime());
        request.setLatestArrivalTime(dto.getLatestArrivalTime());

        // preferences
        request.setMaxWalkingTimeMinutes(dto.getMaxWalkingTimeMinutes());
        request.setNumberOfRiders(dto.getNumberOfRiders());
        request.setSameGender(dto.isSameGender());
        request.setAllowsSmoking(dto.isAllowsSmoking());
        request.setAllowsPets(dto.isAllowsPets());

        // match flag & timestamps
        request.setMatched(false);
        request.setCreatedAt(ZonedDateTime.now());
        request.setUpdatedAt(ZonedDateTime.now());

        return riderRequestRepository.save(request);
    }
}



///TODO: - In one day no more than 4 trips
/// - Checking the latest arrival time is more than or equal the expected time for the trip --need api
