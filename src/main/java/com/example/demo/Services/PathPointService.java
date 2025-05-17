// PathPointService.java
package com.example.demo.Services;

import com.example.demo.DAOs.PathPointRepository;
import com.example.demo.DTOs.PointDTO;
import com.example.demo.Enums.PointType;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.Models.EntityClasses.PathPoint;
import com.example.demo.DTOs.CoordinateDTO;
import com.example.demo.Models.EntityClasses.RiderRequest;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PathPointService {
    private final PathPointRepository ppRepo;

    public PathPointService(PathPointRepository ppRepo) {
        this.ppRepo = ppRepo;
    }

    @Transactional
    public void replacePathForOffer(String offerId, List<PointDTO> path) {
        // Delete any existing PathPoints for this offer
        ppRepo.deleteByDriverOfferId(offerId);
        DriverOffer offer = new DriverOffer();
        offer.setId(offerId);
        // exclude first and last point the pickup and dropoff of the driver
        for (int i = 1; i < path.size()-1; i++) {
            PointDTO pd = path.get(i);

            PathPoint pp = new PathPoint();
            pp.setId(UUID.randomUUID().toString());
            pp.setDriverOffer(offer);
            pp.setPathOrder(i);
            pp.setType(PointType.valueOf(pd.getPointType().toLowerCase()));
            pp.setWalkingDurationMinutes(pd.getWalkingDurationMinutes());

            CoordinateDTO c = pd.getPoint();
            pp.setLatitude(BigDecimal.valueOf(c.getLat()));
            pp.setLongitude(BigDecimal.valueOf(c.getLng()));
            pp.setExpectedArrivalTime(ZonedDateTime.parse(pd.getTime()));

            // only set a riderRequest when ownerType == "rider"
            if ("rider".equalsIgnoreCase(pd.getOwnerType())) {
                RiderRequest riderRequest = new RiderRequest();
                riderRequest.setId(pd.getOwnerID());
                pp.setRiderRequest(riderRequest);
            }
            ppRepo.save(pp);
        }
    }

    public PathPoint findPoint(String offerId, String requestId, String pointType) {
        return ppRepo.findByDriverOfferIdAndRiderRequestIdAndType(
                        offerId, requestId, PointType.valueOf(pointType.toLowerCase()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "PathPoint not found for " + offerId + "/" + requestId + "/" + pointType));
    }

    public String findPointId(String offerId, String requestId, String pointType) {
        return findPoint(offerId, requestId, pointType).getId();
    }
}
