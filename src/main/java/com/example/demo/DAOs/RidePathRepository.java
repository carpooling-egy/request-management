package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.PathPoint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RidePathRepository extends JpaRepository<PathPoint, String> {
}
