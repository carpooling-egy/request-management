package com.example.demo.Services;

import com.example.demo.DTOs.MatchedRequestDTO;
import com.example.demo.DTOs.MatchingResultDTO;
import com.example.demo.DAOs.RiderRequestRepository;
import com.example.demo.DAOs.RideMatchRepository;
import com.example.demo.DTOs.PointDTO;
import com.example.demo.Models.EntityClasses.RideMatch;
import com.example.demo.Models.EntityClasses.RiderRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchingResultService {


    private final RiderRequestService riderRequestService;
    private final RideMatchService rideMatchService;
    private final DriverOfferService driverOfferService;
    private final PathPointService pathPointService;

    /**
     * Called whenever a new matching result arrives.
     */
    @Transactional
    public void processMatchingResult(MatchingResultDTO dto) {
        // 1) mark each matched rider_request as matched
        markRiderRequestsMatched(dto.getAssignedMatchedRequests());

        // 2) update the estimated arrival time of the driver offer which is the time of the last point
        updateDriverOfferArrival(dto.getOfferId(), dto.getPath());

        // 3) update the number of requests in the driver offer
        updateDriverOfferRequestCount(dto.getOfferId(), dto.getCurrentNumberOfRequests());

        // 4) update the path point table.
        updatePathPoints(dto.getOfferId(), dto.getPath());

        // 5) create and save our RideMatch entity if it doesn't exist. if it already exists, update it
        saveOrUpdateRideMatches(dto);

    }

    private void markRiderRequestsMatched(List<MatchedRequestDTO> matchedRequests) {
        matchedRequests.forEach(mr ->
                riderRequestService.markMatched(mr.getRequestId())
        );
    }


    private void updateDriverOfferArrival(String offerId, List<PointDTO> path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalStateException("No path points for offer " + offerId);
        }

        ZonedDateTime lastTime = ZonedDateTime.parse(path.getLast().getTime());
        driverOfferService.updateEstimatedArrivalTime(offerId, lastTime);
    }

    private void updateDriverOfferRequestCount(String offerId, int count) {
        driverOfferService.updateCurrentNumberOfRequests(offerId, count);
    }

    private void updatePathPoints(String offerId, List<PointDTO> path) {
        pathPointService.replacePathForOffer(offerId, path);
    }

    private void saveOrUpdateRideMatches(MatchingResultDTO dto) {
        dto.getAssignedMatchedRequests().forEach(mr -> {
            String offerId   = dto.getOfferId();
            String requestId = mr.getRequestId();
            String pickupId  = pathPointService.findPointId(offerId, requestId, "pickup");
            String dropoffId = pathPointService.findPointId(offerId, requestId, "dropoff");
            rideMatchService.createOrUpdateMatch(offerId, requestId, pickupId, dropoffId);
        });
    }
}
