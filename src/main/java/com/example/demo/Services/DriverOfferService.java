package com.example.demo.Services;

import com.example.demo.DTOs.DriverOfferDTO;
import com.example.demo.Enums.GenderType;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.DAOs.DriverOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class DriverOfferService {

    private final DriverOfferRepository driverRepo;
    private final TripValidationService validator;
    private final RouteService routeService;
    private final GenderService genderService;
    private final UserLockRegistry lockRegistry;

    public DriverOfferService(
            DriverOfferRepository driverRepo,
            TripValidationService validator,
            RouteService routeService,
            GenderService genderService,
            UserLockRegistry lockRegistry
    ) {
        this.driverRepo   = driverRepo;
        this.validator    = validator;
        this.routeService = routeService;
        this.genderService= genderService;
        this.lockRegistry = lockRegistry;
    }

    public DriverOffer createDriverOffer(DriverOfferDTO dto) {
        String userId = dto.getUserId();
        Object lock   = lockRegistry.getLock(userId);

        synchronized (lock) {
            try {
                GenderType userGender = genderService.getGender(userId);
                ZonedDateTime start   = dto.getDepartureTime();

                double travelTimeMinutes = routeService
                        .getTravelTimeMinutes(
                                dto.getSourceLatitude().doubleValue(),
                                dto.getSourceLongitude().doubleValue(),
                                dto.getDestinationLatitude().doubleValue(),
                                dto.getDestinationLongitude().doubleValue(),
                                start
                        );
                ZonedDateTime end = start.plusMinutes((long) travelTimeMinutes + dto.getDetourTimeMinutes());

                validator.validateDriverTrip(userId, start, end);

                DriverOffer offer = new DriverOffer();
                offer.setId(UUID.randomUUID().toString());
                offer.setUserId(userId);

                offer.setSourceLatitude(dto.getSourceLatitude());
                offer.setSourceLongitude(dto.getSourceLongitude());
                offer.setSourceAddress(dto.getSourceAddress());

                offer.setDestinationLatitude(dto.getDestinationLatitude());
                offer.setDestinationLongitude(dto.getDestinationLongitude());
                offer.setDestinationAddress(dto.getDestinationAddress());

                offer.setDepartureTime(start);
                offer.setDetourDurationMinutes(dto.getDetourTimeMinutes());
                offer.setCapacity(dto.getCapacity());
                offer.setMaxEstimatedArrivalTime(end);

                offer.setCurrentNumberOfRequests(dto.getCurrentNumberOfRequests());
                offer.setSameGender(dto.isSameGender());
                offer.setUserGender(userGender);

                offer.setCreatedAt(ZonedDateTime.now());
                offer.setUpdatedAt(ZonedDateTime.now());

                return driverRepo.save(offer);
            } finally {
                lockRegistry.cleanup(userId, lock);
            }
        }
    }

    public void updateArrivalTimeAndRequestCount(String offerId, ZonedDateTime arrivalTime, int count) {
        int rows = driverRepo.updateArrivalTimeAndRequestCount(offerId, arrivalTime, count);
        if (rows == 0) {
            throw new EntityNotFoundException("DriverOffer not found: " + offerId);
        }
    }
}
