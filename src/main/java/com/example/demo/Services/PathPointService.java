// PathPointService.java
package com.example.demo.Services;

import com.example.demo.DAOs.PathPointRepository;
import com.example.demo.DTOs.PointDTO;
import com.example.demo.Enums.PointType;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.Models.EntityClasses.PathPoint;
import com.example.demo.DTOs.CoordinateDTO;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PathPointService {
    private final PathPointRepository ppRepo;
    private final DriverOfferService driverOfferService;
    private final RiderRequestService riderRequestService;

    public PathPointService(PathPointRepository ppRepo, DriverOfferService driverOfferService, RiderRequestService riderRequestService) {
        this.ppRepo = ppRepo;
        this.driverOfferService = driverOfferService;
        this.riderRequestService = riderRequestService;
    }

    @Transactional
    public void replacePathForOffer(String offerId, List<PointDTO> path) {
        // Delete any existing PathPoints for this offer
        ppRepo.deleteByDriverOfferId(offerId);
        DriverOffer offer = driverOfferService.findById(offerId);
        for (int i = 1; i < path.size()-1; i++) {
            PointDTO pd = path.get(i);

            PathPoint pp = new PathPoint();
            pp.setId(UUID.randomUUID().toString());
            pp.setDriverOffer(offer);
            pp.setPathOrder(i);
            pp.setType(PointType.valueOf(pd.getPointType().toUpperCase()));
            pp.setWalkingDurationMinutes(pd.getWalkingDurationMinutes());

            CoordinateDTO c = pd.getPoint();
            pp.setLatitude(BigDecimal.valueOf(c.getLat()));
            pp.setLongitude(BigDecimal.valueOf(c.getLng()));
            pp.setExpectedArrivalTime(ZonedDateTime.parse(pd.getTime()));

            // only set a riderRequest when ownerType == "rider"
            if ("rider".equalsIgnoreCase(pd.getOwnerType())) {
                pp.setRiderRequest(riderRequestService.findById(pd.getOwnerID()));
            }
            offer.addPathPoint(pp); // where should i make .save()?
            // need to add it in the driver offer list put clear it first
            // need to add it in the rider request
            ppRepo.save(pp);
        }
    }

    public PathPoint findPoint(String offerId, String requestId, String pointType) {
        return ppRepo.findByDriverOfferIdAndRiderRequestIdAndType(
                        offerId, requestId, PointType.valueOf(pointType.toUpperCase()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "PathPoint not found for " + offerId + "/" + requestId + "/" + pointType));
    }

    public String findPointId(String offerId, String requestId, String pointType) {
        return findPoint(offerId, requestId, pointType).getId();
    }
}
