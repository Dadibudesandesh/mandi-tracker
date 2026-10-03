
package com.manditracker.api.crop;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/crops")
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    @GetMapping
    public List<Crop> getCrops() {
        return cropService.getActiveCrops();
    }
}
