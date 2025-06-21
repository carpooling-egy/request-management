package com.example.demo.Services;

import com.example.demo.DTOs.RiderRequestDTO;
import com.example.demo.Enums.GenderType;
import com.example.demo.Models.EntityClasses.RiderRequest;
import com.example.demo.DAOs.RiderRequestRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RiderRequestService {

    private final RiderRequestRepository riderRepo;
    private final TripValidationService validator;
    private final GenderService genderService;

    /**
     * Per-user locks to serialize request creation and avoid overlapping races.
     */
    private final ConcurrentHashMap<String, Object> userLocks = new ConcurrentHashMap<>();

    public RiderRequestService(
            RiderRequestRepository riderRepo,
            TripValidationService validator,
            GenderService genderService
    ) {
        this.riderRepo     = riderRepo;
        this.validator     = validator;
        this.genderService = genderService;
    }

    public RiderRequest createRiderRequest(RiderRequestDTO dto) {
        String userId = dto.getUserId();
        ZonedDateTime start = dto.getEarliestDepartureTime();
        ZonedDateTime end   = dto.getLatestArrivalTime();

        // Acquire or create the per-user lock
        Object lock = userLocks.computeIfAbsent(userId, k -> new Object());

        synchronized (lock) {
            try {
                GenderType userGender = genderService.getGender(userId);

                // run all validations under the lock
                validator.validateRiderTrip(
                        userId,
                        start,
                        end,
                        dto.getSourceLatitude().doubleValue(),
                        dto.getSourceLongitude().doubleValue(),
                        dto.getDestinationLatitude().doubleValue(),
                        dto.getDestinationLongitude().doubleValue()
                );

                // map DTO → Entity
                RiderRequest req = new RiderRequest();
                req.setId(UUID.randomUUID().toString());
                req.setUserId(userId);

                req.setSourceLatitude(dto.getSourceLatitude());
                req.setSourceLongitude(dto.getSourceLongitude());
                req.setSourceAddress(dto.getSourceAddress());

                req.setDestinationLatitude(dto.getDestinationLatitude());
                req.setDestinationLongitude(dto.getDestinationLongitude());
                req.setDestinationAddress(dto.getDestinationAddress());

                req.setEarliestDepartureTime(start);
                req.setLatestArrivalTime(end);

                req.setMaxWalkingDurationMinutes(dto.getMaxWalkingTimeMinutes());
                req.setNumberOfRiders(dto.getNumberOfRiders());

                req.setSameGender(dto.isSameGender());
                req.setUserGender(userGender);

                req.setMatched(false);
                req.setCreatedAt(ZonedDateTime.now());
                req.setUpdatedAt(ZonedDateTime.now());

                return riderRepo.save(req);

            } finally {
                // Clean up lock to prevent unbounded growth
                userLocks.compute(userId, (key, existing) -> existing == lock ? null : existing);
            }
        }
    }

    public void markMatchedBatch(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new IllegalArgumentException("Cannot mark empty list of requests as matched");
        }
        int updatedCount = riderRepo.markMatchedByIds(ids);
        if (updatedCount != ids.size()) {
            throw new IllegalStateException("Expected to update " + ids.size() + " requests, but updated only " + updatedCount);
        }
    }
}
