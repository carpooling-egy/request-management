package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.RideMatch;
import com.example.demo.Models.EntityClasses.RideMatchId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideMatchRepository extends JpaRepository<RideMatch, RideMatchId> {
}
