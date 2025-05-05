package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.PathPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface RidePathRepository extends JpaRepository<PathPoint, UUID> {
    List<PathPoint> findByDriverOfferIdOrderByPathOrderAsc(UUID driverOfferId);
}
