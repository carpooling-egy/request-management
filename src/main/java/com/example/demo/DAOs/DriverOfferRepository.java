package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.DriverOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

public interface DriverOfferRepository extends JpaRepository<DriverOffer, String> {

    int countByUserIdAndDepartureTimeAfter(String userId, ZonedDateTime now);

    @Query("""
      SELECT d
        FROM DriverOffer d
       WHERE d.userId = :userId
         AND d.departureTime <= :newLatest
         AND d.maxEstimatedArrivalTime >= :newEarliest
    """)
    List<DriverOffer> findOverlappingOffers(
            @Param("userId") String userId,
            @Param("newEarliest") ZonedDateTime newEarliest,
            @Param("newLatest") ZonedDateTime newLatest
    );

    @Query("SELECT COUNT(d) FROM DriverOffer d WHERE d.userId = :userId AND CAST(d.createdAt AS date) = CURRENT_DATE")
    int countTodayDriverOffers(@Param("userId") String userId);

    @Query("SELECT COUNT(d) FROM DriverOffer d " +
            "WHERE d.userId = :userId " +
            "AND FUNCTION('DATE', d.departureTime) = :targetDate")
    int countDriverOffersOnDate(
            @Param("userId") String userId,
            @Param("targetDate") LocalDate targetDate
    );
}
