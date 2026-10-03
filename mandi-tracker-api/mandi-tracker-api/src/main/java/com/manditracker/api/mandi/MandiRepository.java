
package com.manditracker.api.mandi;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MandiRepository extends JpaRepository<Mandi, Long> {

    List<Mandi> findByActiveTrueOrderByDistrictAscNameAsc();

    List<Mandi> findByDistrictIgnoreCaseAndActiveTrueOrderByNameAsc(
            String district
    );
}
