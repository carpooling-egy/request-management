package com.example.demo.Services;

import com.example.demo.DTOs.DriverOfferDTO;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.DAOs.DriverOfferRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class DriverOfferService {

    private final DriverOfferRepository driverRepo;
    private final TripValidationService validator;
    private final RouteService routeService;

    public DriverOfferService(
            DriverOfferRepository driverRepo,
            TripValidationService validator,
            RouteService routeService
    ) {
        this.driverRepo = driverRepo;
        this.validator  = validator;
        this.routeService = routeService;
    }

    public DriverOffer createDriverOffer(DriverOfferDTO dto) {
        UUID userUuid = dto.getUserUuid();
        ZonedDateTime start = dto.getDepartureTime();
        double estimatedArrivalTime = routeService
                .getTravelTimeMinutes(
                        dto.getSourceLatitude().doubleValue(),
                        dto.getSourceLongitude().doubleValue(),
                        dto.getDestinationLatitude().doubleValue(),
                        dto.getDestinationLongitude().doubleValue(),
                        start
                );
        ZonedDateTime end   = dto.getDepartureTime().plusMinutes((long) estimatedArrivalTime + dto.getDetourTimeMinutes());

        // run all validations
        validator.validateDriverTrip(userUuid, start, end);

        // map DTO → Entity
        DriverOffer offer = new DriverOffer();
        offer.setId(UUID.randomUUID());
        offer.setUserId(userUuid);

        offer.setSourceLatitude(dto.getSourceLatitude());
        offer.setSourceLongitude(dto.getSourceLongitude());
        offer.setSourceAddress(dto.getSourceAddress());

        offer.setDestinationLatitude(dto.getDestinationLatitude());
        offer.setDestinationLongitude(dto.getDestinationLongitude());
        offer.setDestinationAddress(dto.getDestinationAddress());

        offer.setDepartureTime(start);
        offer.setDetourDurationMinutes(dto.getDetourTimeMinutes());
        offer.setCapacity(dto.getCapacity());
        offer.setSelectedCarId(dto.getSelectedCarId());
        offer.setMaxEstimatedArrivalTime(end);

        offer.setCurrentNumberOfRequests( // i think should be 0
                dto.getCurrentNumberOfRequests()
        );
        offer.setSameGender(dto.isSameGender());
        offer.setAllowsSmoking(dto.isAllowsSmoking());
        offer.setAllowsPets(dto.isAllowsPets());
        offer.setUserGender(dto.getUserGender());// need api

        offer.setCreatedAt(ZonedDateTime.now());
        offer.setUpdatedAt(ZonedDateTime.now());

        return driverRepo.save(offer);
    }
}
