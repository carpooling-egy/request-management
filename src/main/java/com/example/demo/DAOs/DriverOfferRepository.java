package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.DriverOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface DriverOfferRepository extends JpaRepository<DriverOffer, UUID> {
    List<DriverOffer> findByUserUuid(UUID userUuid);
}

