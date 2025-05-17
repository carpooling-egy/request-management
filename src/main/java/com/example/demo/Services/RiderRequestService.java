package com.example.demo.Services;

import com.example.demo.DTOs.RiderRequestDTO;
import com.example.demo.Enums.GenderType;
import com.example.demo.Models.EntityClasses.RiderRequest;
import com.example.demo.DAOs.RiderRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class RiderRequestService {

    private final RiderRequestRepository riderRepo;
    private final TripValidationService validator;
    private final GenderService genderService;

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
        // Now using String IDs
        String userId = dto.getUserId();
        ZonedDateTime start = dto.getEarliestDepartureTime();
        ZonedDateTime end   = dto.getLatestArrivalTime();

        GenderType userGender = genderService.getGender(userId);

        // run all validations
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
        req.setAllowsSmoking(dto.isAllowsSmoking());
        req.setAllowsPets(dto.isAllowsPets());
        req.setUserGender(userGender);

        req.setMatched(false);
        req.setCreatedAt(ZonedDateTime.now());
        req.setUpdatedAt(ZonedDateTime.now());

        return riderRepo.save(req);
    }

    @Transactional
    public void markMatched(String requestId) {
        RiderRequest rr = riderRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("RiderRequest not found: " + requestId));
        rr.setMatched(true);
        riderRepo.save(rr);
    }

    public RiderRequest findById(String requestId) {
        return riderRepo.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("RiderRequest not found: " + requestId));
    }
}
