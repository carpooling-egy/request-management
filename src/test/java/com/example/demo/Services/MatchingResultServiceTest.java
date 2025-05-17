//package com.example.demo.Services;
//
//import com.example.demo.DTOs.MatchedRequestDTO;
//import com.example.demo.DTOs.MatchingResultDTO;
//import com.example.demo.DTOs.PointDTO;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.time.ZonedDateTime;
//import java.util.List;
//
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class MatchingResultServiceTest {
//
//    @Mock RiderRequestService riderRequestService;
//    @Mock RideMatchService   rideMatchService;
//    @Mock DriverOfferService driverOfferService;
//    @Mock PathPointService   pathPointService;
//
//    @InjectMocks
//    MatchingResultService matchingResultService;
//
//    private MatchingResultDTO dto;
//
//    @BeforeEach
//    void setUp() {
//        // Build a sample MatchingResultDTO with two matched requests
//        MatchedRequestDTO mr1 = new MatchedRequestDTO();
//        mr1.setUserId("user-1");
//        mr1.setRequestId("req-1");
//        MatchedRequestDTO mr2 = new MatchedRequestDTO();
//        mr2.setUserId("user-2");
//        mr2.setRequestId("req-2");
//
//        // Build a simple path: pickup then dropoff
//        PointDTO p1 = new PointDTO();
//        p1.setOwnerType("driver");
//        p1.setOwnerID("offer-123");
//        p1.setPointType("pickup");
//        p1.setPoint(new com.example.demo.DTOs.CoordinateDTO() {{
//            setLat(10);
//            setLng(20);
//        }});
//        p1.setTime("2025-05-17T08:00:00Z");
//
//        PointDTO p2 = new PointDTO();
//        p2.setOwnerType("rider");
//        p2.setOwnerID("req-1");
//        p2.setPointType("dropoff");
//        p2.setPoint(new com.example.demo.DTOs.CoordinateDTO() {{
//            setLat(10);
//            setLng(20);
//        }});
//        p2.setTime("2025-05-17T08:15:00Z");
//
//        dto = new MatchingResultDTO();
//        dto.setOfferId("offer-123");
//        dto.setAssignedMatchedRequests(List.of(mr1, mr2));
//        dto.setPath(List.of(p1, p2));
//        dto.setCurrentNumberOfRequests(2);
//
//        // Stub PathPointService to return IDs for pickup/dropoff
//        when(pathPointService.findPointId("offer-123", "req-1", "pickup"))
//                .thenReturn("pp-1");
//        when(pathPointService.findPointId("offer-123", "req-1", "dropoff"))
//                .thenReturn("pp-2");
//        when(pathPointService.findPointId("offer-123", "req-2", "pickup"))
//                .thenReturn("pp-3");
//        when(pathPointService.findPointId("offer-123", "req-2", "dropoff"))
//                .thenReturn("pp-4");
//    }
//
//    @Test
//    void processMatchingResult_happyPath_invokesAllServices() {
//        // WHEN
//        matchingResultService.processMatchingResult(dto);
//
//        // THEN
//        // 1) RiderRequestService.markMatched for each
//        verify(riderRequestService).markMatched("req-1");
//        verify(riderRequestService).markMatched("req-2");
//
//        // 2) DriverOfferService.updateEstimatedArrivalTime with last point time
//        verify(driverOfferService).updateEstimatedArrivalTime(
//                eq("offer-123"),
//                eq(ZonedDateTime.parse("2025-05-17T08:15:00Z"))
//        );
//
//        // 3) DriverOfferService.updateCurrentNumberOfRequests
//        verify(driverOfferService).updateCurrentNumberOfRequests("offer-123", 2);
//
//        // 4) PathPointService.replacePathForOffer
//        verify(pathPointService).replacePathForOffer("offer-123", dto.getPath());
//
//        // 5) RideMatchService.createOrUpdateMatch for each matched
//        verify(rideMatchService).createOrUpdateMatch("offer-123", "req-1", "pp-1", "pp-2");
//        verify(rideMatchService).createOrUpdateMatch("offer-123", "req-2", "pp-3", "pp-4");
//    }
//
//    @Test
//    void processMatchingResult_noPath_throws() {
//        dto.setPath(List.of());  // empty path
//
//        var ex = org.junit.jupiter.api.Assertions.assertThrows(
//                IllegalStateException.class,
//                () -> matchingResultService.processMatchingResult(dto)
//        );
//        org.junit.jupiter.api.Assertions.assertTrue(
//                ex.getMessage().contains("No path points for offer")
//        );
//
//        // none of the other services should be called
//        verifyNoInteractions(riderRequestService,
//                rideMatchService,
//                driverOfferService,
//                pathPointService);
//    }
//}


      ///////////////////////////////////////////////////////////
//MatchedRequestDTO matched1 = new MatchedRequestDTO();
//                    matched1.setUserId("u1");
//                    matched1.setRequestId("rr1");
//
//MatchedRequestDTO matched2 = new MatchedRequestDTO();
//                    matched2.setUserId("u2");
//                    matched2.setRequestId("rr2");
//
//// --- Coordinates from do1 / rr1 / rr2 ---
//// Driver start (do1.source)
//CoordinateDTO driverStartCoord = new CoordinateDTO();
//                    driverStartCoord.setLat(37.7749);
//                    driverStartCoord.setLng(-122.4194);
//// Driver end   (do1.destination)
//CoordinateDTO driverEndCoord = new CoordinateDTO();
//                    driverEndCoord.setLat(34.0522);
//                    driverEndCoord.setLng(-118.2437);
//
//// Rider1 pickup/dropoff
//CoordinateDTO r1Pick = new CoordinateDTO(); r1Pick.setLat(37.7749); r1Pick.setLng(-122.4194);
//CoordinateDTO r1Drop = new CoordinateDTO(); r1Drop.setLat(34.0522); r1Drop.setLng(-118.2437);
//
//// Rider2 pickup/dropoff
//CoordinateDTO r2Pick = new CoordinateDTO(); r2Pick.setLat(40.7128); r2Pick.setLng(-74.0060);
//CoordinateDTO r2Drop = new CoordinateDTO(); r2Drop.setLat(42.3601); r2Drop.setLng(-71.0589);
//
//// --- Build PathPoints ---
//// 1) Driver departure
//PointDTO driverStart = new PointDTO();
//                    driverStart.setOwnerType("driver");
//                    driverStart.setOwnerID("do1");
//                    driverStart.setPoint(driverStartCoord);
//                    driverStart.setTime("2025-05-17T08:30:00Z");
//                    driverStart.setPointType("pickup");
//                    driverStart.setWalkingDurationMinutes(0);
//
//// 2) rr1 pickup
//PointDTO p1 = new PointDTO();
//                    p1.setOwnerType("rider");
//                    p1.setOwnerID("rr1");
//                    p1.setPoint(r1Pick);
//                    p1.setTime("2025-05-17T08:35:00Z");
//                    p1.setPointType("pickup");
//                    p1.setWalkingDurationMinutes(3);
//
//// 3) rr1 dropoff
//PointDTO p2 = new PointDTO();
//                    p2.setOwnerType("rider");
//                    p2.setOwnerID("rr1");
//                    p2.setPoint(r1Drop);
//                    p2.setTime("2025-05-17T12:30:00Z");
//                    p2.setPointType("dropoff");
//                    p2.setWalkingDurationMinutes(0);
//
//// 4) rr2 pickup
//PointDTO p3 = new PointDTO();
//                    p3.setOwnerType("rider");
//                    p3.setOwnerID("rr2");
//                    p3.setPoint(r2Pick);
//                    p3.setTime("2025-05-17T12:45:00Z");
//                    p3.setPointType("pickup");
//                    p3.setWalkingDurationMinutes(5);
//
//// 5) rr2 dropoff
//PointDTO p4 = new PointDTO();
//                    p4.setOwnerType("rider");
//                    p4.setOwnerID("rr2");
//                    p4.setPoint(r2Drop);
//                    p4.setTime("2025-05-17T13:15:00Z");
//                    p4.setPointType("dropoff");
//                    p4.setWalkingDurationMinutes(0);
//
//// 6) Driver arrival
//PointDTO driverEnd = new PointDTO();
//                    driverEnd.setOwnerType("driver");
//                    driverEnd.setOwnerID("do1");
//                    driverEnd.setPoint(driverEndCoord);
//                    driverEnd.setTime("2025-06-17T12:30:00Z");
//                    driverEnd.setPointType("dropoff");
//                    driverEnd.setWalkingDurationMinutes(0);
//
//// --- Assemble MatchingResultDTO ---
//
//                    dto.setUserId("d1");                      // driver user_id
//                    dto.setOfferId("do1");                    // driver offer id
//                    dto.setAssignedMatchedRequests(List.of(matched1, matched2));
//        dto.setPath(List.of(driverStart, p1, p2, p3, p4, driverEnd));
//        dto.setCurrentNumberOfRequests(2);
//
//// --- Invoke service ------------------------------------------------
//
