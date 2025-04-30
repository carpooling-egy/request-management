package com.example.demo.Services;

import com.example.demo.DTOs.DriverOfferDTO;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.DAOs.DriverOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class DriverOfferService {

    @Autowired
    private DriverOfferRepository driverOfferRepository;

    public DriverOffer createDriverOffer(DriverOfferDTO dto) {
        DriverOffer offer = new DriverOffer();
        offer.setUserUuid(dto.getUserUuid());
        offer.setSourceLatitude(dto.getSourceLatitude());
        offer.setSourceLongitude(dto.getSourceLongitude());
        offer.setSourceAddress(dto.getSourceAddress());
        offer.setDestinationLatitude(dto.getDestinationLatitude());
        offer.setDestinationLongitude(dto.getDestinationLongitude());
        offer.setDestinationAddress(dto.getDestinationAddress());
        offer.setDepartureDatetime(dto.getDepartureDatetime());
        offer.setDetourTimeMinutes(dto.getDetourTimeMinutes());
        offer.setCapacity(dto.getCapacity());
        offer.setExternalCarId(dto.getExternalCarId());
        offer.setSameGender(dto.isSameGender());
        offer.setAllowsSmoking(dto.isAllowsSmoking());
        offer.setAllowsPets(dto.isAllowsPets());
        offer.setCreatedAt(ZonedDateTime.now());

        return driverOfferRepository.save(offer);
    }
}
