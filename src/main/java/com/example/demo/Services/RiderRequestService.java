package com.example.demo.Services;


import com.example.demo.DTOs.RiderRequestDTO;
import com.example.demo.Models.EntityClasses.*;
import com.example.demo.DAOs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.time.ZonedDateTime;

@Service
public class RiderRequestService {

    @Autowired
    private RiderRequestRepository riderRequestRepository;

    @Autowired
    private DriverOfferRepository driverOfferRepository;

    public RiderRequest createRiderRequest(RiderRequestDTO dto) {
        RiderRequest request = new RiderRequest();
        request.setId(UUID.randomUUID());
        request.setUserUuid(dto.getUserUuid());
        request.setSourceLatitude(dto.getSourceLatitude());
        request.setSourceLongitude(dto.getSourceLongitude());
        request.setSourceAddress(dto.getSourceAddress());
        request.setDestinationLatitude(dto.getDestinationLatitude());
        request.setDestinationLongitude(dto.getDestinationLongitude());
        request.setDestinationAddress(dto.getDestinationAddress());
        request.setEarliestDepartureDatetime(dto.getEarliestDepartureDatetime());
        request.setLatestArrivalDatetime(dto.getLatestArrivalDatetime());
        request.setMaxWalkingTimeMinutes(dto.getMaxWalkingTimeMinutes());
        request.setNumberOfRiders(dto.getNumberOfRiders());
        request.setCreatedAt(ZonedDateTime.now());

        return riderRequestRepository.save(request);
    }
}
