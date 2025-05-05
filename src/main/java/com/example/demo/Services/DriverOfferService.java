package com.example.demo.Services;

import com.example.demo.DTOs.DriverOfferDTO;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.DAOs.DriverOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class DriverOfferService {

    @Autowired
    private DriverOfferRepository driverOfferRepository;

    public DriverOffer createDriverOffer(DriverOfferDTO dto) {
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
