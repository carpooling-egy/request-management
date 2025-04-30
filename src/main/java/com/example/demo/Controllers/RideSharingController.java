package com.example.demo.Controllers;


import com.example.demo.DTOs.DriverOfferDTO;
import com.example.demo.DTOs.RiderRequestDTO;
import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.Models.EntityClasses.RiderRequest;
import com.example.demo.Services.DriverOfferService;
import com.example.demo.Services.RiderRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

        import java.net.URI;

@RestController
@RequestMapping("/api")
public class RideSharingController {

    @Autowired
    private DriverOfferService driverOfferService;

    @Autowired
    private RiderRequestService riderRequestService;

    // Endpoint to add a driver offer
    @PostMapping("/driver-offers")
    public ResponseEntity<DriverOffer> createDriverOffer(@RequestBody DriverOfferDTO dto) {
        DriverOffer savedOffer = driverOfferService.createDriverOffer(dto);
        return ResponseEntity.created(URI.create("/api/driver-offers/" + savedOffer.getId())).body(savedOffer);
    }

    // Endpoint to add a rider request
    @PostMapping("/rider-requests")
    public ResponseEntity<RiderRequest> createRiderRequest(@RequestBody RiderRequestDTO dto) {
        RiderRequest savedRequest = riderRequestService.createRiderRequest(dto);
        return ResponseEntity.created(URI.create("/api/rider-requests/" + savedRequest.getId())).body(savedRequest);
    }
}
