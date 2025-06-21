package com.example.demo.Services;

import com.example.demo.DTOs.MatchedRequestDTO;
import com.example.demo.DTOs.MatchingResultDTO;
import com.example.demo.DTOs.PointDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

        // 2) update the estimated arrival time and request count of the driver offer
        updateDriverOffer(dto.getOfferId(), dto.getPath(), dto.getCurrentNumberOfRequests());

        // 3) update the path point table.
        updatePathPoints(dto.getOfferId(), dto.getPath());

        // 4) create and save our RideMatch entity if it doesn't exist. if it already exists, update it
        saveOrUpdateRideMatches(dto);

    }

    private void markRiderRequestsMatched(List<MatchedRequestDTO> matchedRequests) {
        List<String> ids = matchedRequests.stream()
                .map(MatchedRequestDTO::getRequestId)
                .collect(Collectors.toList());
        riderRequestService.markMatchedBatch(ids);
    }

    private void updateDriverOffer(String offerId, List<PointDTO> path, int requestCount) {
        if (path == null || path.isEmpty()) {
            throw new IllegalStateException("No path points for offer " + offerId);
        }
        ZonedDateTime lastTime = ZonedDateTime.parse(path.getLast().getTime());
        driverOfferService.updateArrivalTimeAndRequestCount(offerId, lastTime, requestCount);
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
