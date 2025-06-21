package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.RiderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;

public interface RiderRequestRepository extends JpaRepository<RiderRequest, String> {

    int countByUserIdAndEarliestDepartureTimeAfter(String userId, ZonedDateTime now);

    @Query("""
      SELECT r
        FROM RiderRequest r
       WHERE r.userId = :userId
         AND r.earliestDepartureTime <= :newLatest
         AND r.latestArrivalTime   >= :newEarliest
    """)
    List<RiderRequest> findOverlappingRequests(
            @Param("userId")      String userId,
            @Param("newEarliest") ZonedDateTime newEarliest,
            @Param("newLatest")   ZonedDateTime newLatest
    );

    @Query("SELECT COUNT(r) FROM RiderRequest r WHERE r.userId = :userId AND CAST(r.createdAt AS date) = CURRENT_DATE")
    int countTodayRiderRequests(@Param("userId") String userId);

    @Query("SELECT COUNT(r) FROM RiderRequest r " +
            "WHERE r.userId = :userId " +
            "AND FUNCTION('DATE', r.earliestDepartureTime) = :targetDate")
    int countRiderRequestsOnDate(
            @Param("userId") String userId,
            @Param("targetDate") LocalDate targetDate
    );

    @Modifying
    @Query("UPDATE RiderRequest r SET r.isMatched = true WHERE r.id IN :ids")
    int markMatchedByIds(@Param("ids") Collection<String> ids);

}
