
package com.manditracker.api.price;

import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class MandiPriceService {

    private final MandiPriceRepository priceRepository;

    public MandiPriceService(MandiPriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }

    public List<MandiPrice> searchPrices(
            Long cropId,
            Long mandiId,
            String district,
            LocalDate date
    ) {
        String normalizedDistrict =
                (district == null || district.isBlank())
                        ? null
                        : district.trim();

        return priceRepository.searchPrices(
                cropId,
                mandiId,
                normalizedDistrict,
                date
        );
    }
}
