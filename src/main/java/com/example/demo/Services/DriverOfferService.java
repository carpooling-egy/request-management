package com.example.demo.Services;

import com.example.demo.Models.EntityClasses.*;
import com.example.demo.DTOs.DriverOfferDTO;
import com.example.demo.DAOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class DriverOfferService {

    private static final int MINUTES_BEFORE      = 30;
    private static final int MAX_TOTAL_UPCOMING_TRIPS = 30;
    private static final int MAX_TOTAL_TRIPS_IN_DAY    = 5;
    private static final int MAX_TOTAL_REQUESTS_TODAY = 5;

    @Autowired
    private DriverOfferRepository driverOfferRepository;

    @Autowired
    private RiderRequestRepository riderRequestRepository;

    public DriverOffer createDriverOffer(DriverOfferDTO dto) {
        UUID userUuid = dto.getUserUuid();
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime start = dto.getDepartureTime();
        ZonedDateTime end = dto.getEstimatedArrivalTime();

        // 1) end time must be after start time
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End time must be after start time.");
        }

        // 2) 30 min lead
        if (start.isBefore(now.plusMinutes(MINUTES_BEFORE))) {
            throw new IllegalArgumentException(
                    "Departure time must be at least " + MINUTES_BEFORE + " minutes from now."
            );
        }

        // 3a) no overlap with existing driver offers
        if (!driverOfferRepository
                .findOverlappingOffers(userUuid, start, end)
                .isEmpty()
        ) {
            throw new IllegalArgumentException("You have an overlapping driver offer.");
        }

        // 3b) no overlap with existing rider requests
        if (!riderRequestRepository
                .findOverlappingRequests(userUuid, start, end)
                .isEmpty()
        ) {
            throw new IllegalArgumentException("You have an overlapping rider request.");
        }

        // 4) total upcoming trips limit
        int upcomingOffers   = driverOfferRepository
                .countByUserUuidAndDepartureTimeAfter(userUuid, now);
        int upcomingRequests = riderRequestRepository
                .countByUserIdAndEarliestDepartureTimeAfter(userUuid, now);

        if (upcomingOffers + upcomingRequests >= MAX_TOTAL_UPCOMING_TRIPS) {
            throw new IllegalArgumentException(
                    "You cannot have more than " + MAX_TOTAL_UPCOMING_TRIPS + " upcoming trips."
            );
        }

        // 5) no more than 5 requests in a day
        int driverOffersOnDate = driverOfferRepository
                .countDriverOffersOnDate(userUuid, start.toLocalDate());
        int riderRequestsOnDate = riderRequestRepository
                .countRiderRequestsOnDate(userUuid, start.toLocalDate());

        if (driverOffersOnDate + riderRequestsOnDate >= MAX_TOTAL_TRIPS_IN_DAY) {
            throw new IllegalArgumentException(
                    "You cannot have more than " + MAX_TOTAL_TRIPS_IN_DAY + " in a" + start.toLocalDate() + "."
            );
        }


        // 6) no more than 5 requests today
        int todayDriverOffers = driverOfferRepository
                .countTodayDriverOffers(userUuid);

        int todayRiderRequests = riderRequestRepository
                .countTodayRiderRequests(userUuid);

        if (todayDriverOffers + todayRiderRequests >= MAX_TOTAL_REQUESTS_TODAY) {
            throw new IllegalArgumentException(
                    "You cannot have more than " + MAX_TOTAL_REQUESTS_TODAY + " trips today."
            );
        }


        DriverOffer offer = new DriverOffer();

        // generate UUID for new record
        offer.setId(UUID.randomUUID());
        offer.setUserUuid(dto.getUserUuid());

        // source
        offer.setSourceLatitude(dto.getSourceLatitude());
        offer.setSourceLongitude(dto.getSourceLongitude());
        offer.setSourceAddress(dto.getSourceAddress());

        // destination
        offer.setDestinationLatitude(dto.getDestinationLatitude());
        offer.setDestinationLongitude(dto.getDestinationLongitude());
        offer.setDestinationAddress(dto.getDestinationAddress());

        // timing & capacity
        offer.setDepartureTime(dto.getDepartureTime());
        offer.setDetourTimeMinutes(dto.getDetourTimeMinutes());
        offer.setCapacity(dto.getCapacity());

        // car & request count
        offer.setSelectedCarId(dto.getSelectedCarId());
        offer.setCurrentNumberOfRequests(dto.getCurrentNumberOfRequests() != 0
                ? dto.getCurrentNumberOfRequests() : 0);

        // preferences
        offer.setSameGender(dto.isSameGender());
        offer.setAllowsSmoking(dto.isAllowsSmoking());
        offer.setAllowsPets(dto.isAllowsPets());

        // timestamps
        offer.setCreatedAt(ZonedDateTime.now());
        offer.setUpdatedAt(ZonedDateTime.now());

        return driverOfferRepository.save(offer);
    }
}
