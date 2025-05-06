package com.example.demo.DAOs;

import com.example.demo.Models.EntityClasses.DriverOffer;
import com.example.demo.Models.EntityClasses.RiderRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.UUID;
import java.util.List;

public interface DriverOfferRepository extends JpaRepository<DriverOffer, UUID> {
    int countByUserUuidAndDepartureTimeAfter(UUID userUuid, ZonedDateTime now);

    @Query("""
      SELECT d
        FROM DriverOffer d
       WHERE d.userUuid = :userUuid
         AND d.departureTime <= :newLatest
         AND d.estimatedArrivalTime >= :newEarliest
    """)
    List<DriverOffer> findOverlappingOffers(
            @Param("userUuid") UUID userUuid,
            @Param("newEarliest")    ZonedDateTime newEarliest,
            @Param("newLatest")      ZonedDateTime newLatest
    );

    @Query("SELECT COUNT(d) FROM DriverOffer d WHERE d.userUuid = :userId AND CAST(d.createdAt AS date) = CURRENT_DATE")
    int countTodayDriverOffers(@Param("userId") UUID userId);

    @Query("SELECT COUNT(d) FROM DriverOffer d " +
            "WHERE d.userUuid = :userId " +
            "AND FUNCTION('DATE', d.departureTime) = :targetDate")
    int countDriverOffersOnDate(@Param("userId") UUID userId,
                                @Param("targetDate") LocalDate targetDate);
}