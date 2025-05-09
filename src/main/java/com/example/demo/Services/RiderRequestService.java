package com.example.demo.Services;

import com.example.demo.DTOs.RiderRequestDTO;
import com.example.demo.Models.EntityClasses.RiderRequest;
import com.example.demo.DAOs.RiderRequestRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class RiderRequestService {

    private final RiderRequestRepository riderRepo;
    private final TripValidationService validator;

    public RiderRequestService(
            RiderRequestRepository riderRepo,
            TripValidationService validator
    ) {
        this.riderRepo  = riderRepo;
        this.validator  = validator;
    }

    public RiderRequest createRiderRequest(RiderRequestDTO dto) {
        UUID userId = dto.getUserId();
        ZonedDateTime start = dto.getEarliestDepartureTime();
        ZonedDateTime end   = dto.getLatestArrivalTime();

        // run all validations
        validator.validateRiderTrip(
                userId, start, end,
                dto.getSourceLatitude().doubleValue(),
                dto.getSourceLongitude().doubleValue(),
                dto.getDestinationLatitude().doubleValue(),
                dto.getDestinationLongitude().doubleValue()
        );

        // map DTO → Entity
        RiderRequest req = new RiderRequest();
        req.setId(UUID.randomUUID());
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
        req.setAllowsSmoking(dto.isAllowsSmoking());
        req.setAllowsPets(dto.isAllowsPets());
        req.setUserGender(dto.getUserGender()); // need api

        req.setMatched(false);
        req.setCreatedAt(ZonedDateTime.now());
        req.setUpdatedAt(ZonedDateTime.now());

        return riderRepo.save(req);
    }
}

