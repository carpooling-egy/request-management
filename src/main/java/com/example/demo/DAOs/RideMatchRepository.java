package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.RideMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface RideMatchRepository extends JpaRepository<RideMatch, UUID> {
    List<RideMatch> findByDriverOfferId(UUID driverOfferId);
}
