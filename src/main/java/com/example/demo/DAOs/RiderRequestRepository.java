package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.RiderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface RiderRequestRepository extends JpaRepository<RiderRequest, UUID> {
    List<RiderRequest> findByUserUuid(UUID userUuid);
}
