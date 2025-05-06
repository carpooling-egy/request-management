package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.RiderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.List;

public interface RiderRequestRepository extends JpaRepository<RiderRequest, UUID> {
    int countByUserIdAndEarliestDepartureTimeAfter(UUID userId, ZonedDateTime now);

    @Query("""
      SELECT r
        FROM RiderRequest r
       WHERE r.userId = :userId
         AND r.earliestDepartureTime <= :newLatest
         AND r.latestArrivalTime   >= :newEarliest
    """)
    List<RiderRequest> findOverlappingRequests(
            @Param("userId")      UUID userId,
            @Param("newEarliest") ZonedDateTime newEarliest,
            @Param("newLatest")   ZonedDateTime newLatest
    );


    @Query("SELECT COUNT(r) FROM RiderRequest r WHERE r.userId = :userId AND CAST(r.createdAt AS date) = CURRENT_DATE")
    int countTodayRiderRequests(@Param("userId") UUID userId);

    @Query("SELECT COUNT(r) FROM RiderRequest r " +
            "WHERE r.userId = :userId " +
            "AND FUNCTION('DATE', r.earliestDepartureTime) = :targetDate")
    int countRiderRequestsOnDate(@Param("userId") UUID userId,
                                 @Param("targetDate") LocalDate targetDate);

}
