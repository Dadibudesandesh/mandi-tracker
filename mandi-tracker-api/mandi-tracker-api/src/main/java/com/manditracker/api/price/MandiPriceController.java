
package com.manditracker.api.price;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/prices")
@CrossOrigin(origins = "http://localhost:5173")
public class MandiPriceController {

    private final MandiPriceService priceService;

    public MandiPriceController(MandiPriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping
    public List<MandiPrice> getPrices(
            @RequestParam(required = false) Long cropId,
            @RequestParam(required = false) Long mandiId,
            @RequestParam(required = false) String district,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return priceService.searchPrices(
                cropId, mandiId, district, date
        );
    }
}
