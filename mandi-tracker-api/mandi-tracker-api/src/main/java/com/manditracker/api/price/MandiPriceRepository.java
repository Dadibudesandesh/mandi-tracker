
package com.manditracker.api.price;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MandiPriceRepository
        extends JpaRepository<MandiPrice, Long> {


    @Query("""
    SELECT p FROM MandiPrice p
    WHERE (:cropId IS NULL OR p.crop.id = :cropId)
      AND (:mandiId IS NULL OR p.mandi.id = :mandiId)
      AND (:district IS NULL OR LOWER(p.mandi.district) = LOWER(CAST(:district AS string)))
      AND (:priceDate IS NULL OR p.priceDate = :priceDate)
    ORDER BY p.priceDate DESC, p.crop.name ASC
    """)
    List<MandiPrice> searchPrices(
            @Param("cropId") Long cropId,
            @Param("mandiId") Long mandiId,
            @Param("district") String district,
            @Param("priceDate") LocalDate priceDate
    );

}
