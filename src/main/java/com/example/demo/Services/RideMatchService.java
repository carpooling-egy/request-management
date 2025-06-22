package com.example.demo.Services;

import com.example.demo.DAOs.RideMatchRepository;
import com.example.demo.Models.EntityClasses.*;
import org.springframework.stereotype.Service;

@Service
public class RideMatchService {
    private final RideMatchRepository matchRepo;

    public RideMatchService(RideMatchRepository matchRepo) {
        this.matchRepo = matchRepo;
    }

    public void createOrUpdateMatch(
            String offerId,
            String requestId,
            String pickupPointId,
            String dropoffPointId
    ) {
        RideMatchId key = new RideMatchId(offerId, requestId);
        RideMatch rm = matchRepo.findById(key)
                .orElseGet(() -> {
                    RideMatch m = new RideMatch();
                    m.setDriverOfferId(offerId);
                    m.setRiderRequestId(requestId);
                    return m;
                });
        PathPoint pickupPoint = new PathPoint();
        pickupPoint.setId(pickupPointId);
        rm.setPickupPoint(pickupPoint);

        PathPoint dropoffPoint = new PathPoint();
        dropoffPoint.setId(dropoffPointId);
        rm.setDropoffPoint(dropoffPoint);
        // need to add the driver offer
        DriverOffer driverOffer = new DriverOffer();
        driverOffer.setId(offerId);
        rm.setDriverOffer(driverOffer);
        // need to add the rider request
        RiderRequest riderRequest = new RiderRequest();
        riderRequest.setId(requestId);
        rm.setRiderRequest(riderRequest);
        matchRepo.save(rm);
    }
}
