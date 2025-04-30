package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.RidePath;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface RidePathRepository extends JpaRepository<RidePath, UUID> {
    List<RidePath> findByDriverOfferIdOrderByPathOrderAsc(Long driverOfferId);
}
