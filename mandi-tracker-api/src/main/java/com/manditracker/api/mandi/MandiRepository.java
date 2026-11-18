package com.manditracker.api.mandi;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface MandiRepository extends JpaRepository<Mandi, Long> {
    List<Mandi> findByActiveTrueOrderByDistrictAscNameAsc();
    List<Mandi> findByDistrictIgnoreCaseAndActiveTrueOrderByNameAsc(String district);
    Optional<Mandi> findFirstByNameIgnoreCaseAndDistrictIgnoreCaseAndStateIgnoreCase(String name, String district, String state);
}
