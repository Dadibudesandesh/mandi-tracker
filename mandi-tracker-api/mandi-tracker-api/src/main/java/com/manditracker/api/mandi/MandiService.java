
package com.manditracker.api.mandi;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MandiService {

    private final MandiRepository mandiRepository;

    public MandiService(MandiRepository mandiRepository) {
        this.mandiRepository = mandiRepository;
    }

    public List<Mandi> getAllActiveMandis() {
        return mandiRepository.findByActiveTrueOrderByDistrictAscNameAsc();
    }

    public List<Mandi> getMandisByDistrict(String district) {
        return mandiRepository
                .findByDistrictIgnoreCaseAndActiveTrueOrderByNameAsc(
                        district
                );
    }
}
