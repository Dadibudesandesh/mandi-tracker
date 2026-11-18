
package com.manditracker.api.mandi;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mandis")
@CrossOrigin(origins = "http://localhost:5173")
public class MandiController {

    private final MandiService mandiService;

    public MandiController(MandiService mandiService) {
        this.mandiService = mandiService;
    }

    @GetMapping
    public List<Mandi> getMandis(
            @RequestParam(required = false) String district
    ) {
        if (district != null && !district.isBlank()) {
            return mandiService.getMandisByDistrict(district.trim());
        }

        return mandiService.getAllActiveMandis();
    }
}
