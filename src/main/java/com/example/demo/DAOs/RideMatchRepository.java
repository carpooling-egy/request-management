package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.RideMatch;
import com.example.demo.Models.EntityClasses.RideMatchId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RideMatchRepository extends JpaRepository<RideMatch, RideMatchId> {
    Optional<RideMatch> findByDriverOfferIdAndRiderRequestId(String driverOfferId, String riderRequestId);

}
