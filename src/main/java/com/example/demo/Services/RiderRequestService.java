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

    @Autowired
    private RiderRequestRepository riderRequestRepository;

    public RiderRequest createRiderRequest(RiderRequestDTO dto) {
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
/// - No overlaps trips (driver-rider)
/// - The user can't make trip for tomorrow then after 0ne day then 2 then 3 then 4 ....... (limit the number of total comming trips )
/// - Checking the latest arrival time is more than or equal the expected time for the trip --need api
/// - the start time of the ride-driver request must be after 30 min