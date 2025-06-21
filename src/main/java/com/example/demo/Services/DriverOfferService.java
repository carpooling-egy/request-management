package com.example.demo.Services;

import com.example.demo.DTOs.DriverOfferDTO;
import com.example.demo.Enums.GenderType;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.DAOs.DriverOfferRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DriverOfferService {

    private final DriverOfferRepository driverRepo;
    private final TripValidationService validator;
    private final RouteService routeService;
    private final GenderService genderService;

    /**
     * A map of per-user locks. We synchronize on one lock object per userId
     * to prevent overlapping offers being created concurrently.
     */
    private final ConcurrentHashMap<String, Object> userLocks = new ConcurrentHashMap<>();

    public DriverOfferService(
            DriverOfferRepository driverRepo,
            TripValidationService validator,
            RouteService routeService,
            GenderService genderService
    ) {
        this.driverRepo = driverRepo;
        this.validator  = validator;
        this.routeService = routeService;
        this.genderService = genderService;
    }

    public DriverOffer createDriverOffer(DriverOfferDTO dto) {
        String userId = dto.getUserId();
        // Acquire (or create) the lock object for this user
        Object lock = userLocks.computeIfAbsent(userId, k -> new Object());

        synchronized (lock) {
            try {
                // 1. Perform all validations under the lock
                GenderType userGender = genderService.getGender(userId);
                ZonedDateTime start = dto.getDepartureTime();

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

                // 2. Map DTO → Entity and save
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
                // Optional: clean up the lock if no longer needed to prevent memory leaks
                userLocks.compute(userId, (key, existingLock) -> {
                    // only remove if it’s the same lock object (i.e. no one else added a new one)
                    return (existingLock == lock) ? null : existingLock;
                });
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
