package com.example.demo.Services;

import com.example.demo.DAOs.RideMatchRepository;
import com.example.demo.Models.EntityClasses.RideMatch;
import com.example.demo.Models.EntityClasses.RideMatchId;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
public class RideMatchService {
    private final RideMatchRepository matchRepo;
    private final PathPointService pathPointService;

    public RideMatchService(RideMatchRepository matchRepo, PathPointService pathPointService) {
        this.matchRepo = matchRepo;
        this.pathPointService = pathPointService;
    }

    @Transactional
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
        rm.setPickupPoint(pathPointService.findPoint(offerId, requestId, pickupPointId));
        rm.setDropoffPoint(pathPointService.findPoint(offerId, requestId, dropoffPointId));
        // need to add it in the driver offer list
        // need to add it in the rider request
        // need to add the driver offer
        // need to add the rider request
        matchRepo.save(rm);
    }
}
