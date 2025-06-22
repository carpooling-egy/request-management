package com.example.demo.DAOs;
import com.example.demo.Enums.PointType;
import com.example.demo.Models.EntityClasses.PathPoint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

// in PathPointRepository.java
public interface PathPointRepository extends JpaRepository<PathPoint, String> {
    List<PathPoint> findByDriverOfferId(String driverOfferId);
    Optional<PathPoint> findByDriverOfferIdAndRiderRequestIdAndType(
            String driverOfferId, String riderRequestId, PointType type);
    void deleteByDriverOfferId(String driverOfferId);
}
