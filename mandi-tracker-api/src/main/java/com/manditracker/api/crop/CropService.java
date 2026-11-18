
package com.manditracker.api.crop;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CropService {

    private final CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public List<Crop> getActiveCrops() {
        return cropRepository.findByActiveTrueOrderByNameAsc();
    }
}
