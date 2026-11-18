package com.manditracker.api.ingestion;

import com.manditracker.api.crop.Crop;
import com.manditracker.api.crop.CropRepository;
import com.manditracker.api.mandi.Mandi;
import com.manditracker.api.mandi.MandiRepository;
import com.manditracker.api.price.DataGovMandiResponse;
import com.manditracker.api.price.MandiPrice;
import com.manditracker.api.price.MandiPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Optional;

@Service
public class PriceImportService {
    private static final Logger log = LoggerFactory.getLogger(PriceImportService.class);
    private static final DateTimeFormatter SOURCE_DATE = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final GovernmentPriceClient client;
    private final CropRepository cropRepository;
    private final MandiRepository mandiRepository;
    private final MandiPriceRepository priceRepository;

    public PriceImportService(GovernmentPriceClient client, CropRepository cropRepository,
            MandiRepository mandiRepository, MandiPriceRepository priceRepository) {
        this.client = client;
        this.cropRepository = cropRepository;
        this.mandiRepository = mandiRepository;
        this.priceRepository = priceRepository;
    }

    @Transactional
    public ImportResult importAvailablePrices() {
        int offset = 0, fetched = 0, inserted = 0, updated = 0, skipped = 0;
        Integer total = null;
        do {
            DataGovMandiResponse page = client.fetchPage(offset);
            if (!"ok".equalsIgnoreCase(page.status())) {
                throw new IllegalStateException("data.gov.in response status was not ok: " + page.status());
            }
            if (total == null) total = page.total() == null ? 0 : page.total();
            for (DataGovMandiResponse.MandiRecord record : page.records()) {
                fetched++;
                if (!isUsable(record)) { skipped++; continue; }
                Optional<Crop> crop = findCrop(record.commodity());
                if (crop.isEmpty()) { skipped++; continue; }
                LocalDate priceDate = LocalDate.parse(record.arrivalDate().trim(), SOURCE_DATE);
                Mandi mandi = findOrCreateMandi(record);
                String sourceId = sourceId(record, priceDate);
                MandiPrice price = priceRepository.findBySourceRecordId(sourceId).orElseGet(MandiPrice::new);
                boolean isNew = price.getId() == null;
                price.setCrop(crop.get());
                price.setMandi(mandi);
                price.setCommodityName(record.commodity().trim());
                price.setVariety(clean(record.variety()));
                price.setGrade(clean(record.grade()));
                price.setPriceDate(priceDate);
                price.setMinPrice(record.minPrice());
                price.setMaxPrice(record.maxPrice());
                price.setModalPrice(record.modalPrice());
                price.setUnit(crop.get().getUnit());
                price.setSource("data.gov.in");
                price.setSourceRecordId(sourceId);
                priceRepository.save(price);
                if (isNew) inserted++; else updated++;
            }
            int returned = page.records().size();
            if (returned == 0) break;
            offset += returned;
        } while (offset < total);
        ImportResult result = new ImportResult(fetched, inserted, updated, skipped);
        log.info("data.gov.in import completed: {}", result);
        return result;
    }

    private Optional<Crop> findCrop(String commodity) {
        String name = commodity.trim();
        Optional<Crop> exact = cropRepository.findByNameIgnoreCaseAndActiveTrue(name);
        if (exact.isPresent()) return exact;
        String normalized = name.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9]", "");
        if (normalized.startsWith("onion")) return cropRepository.findByNameIgnoreCaseAndActiveTrue("Onion");
        if (normalized.startsWith("maize")) return cropRepository.findByNameIgnoreCaseAndActiveTrue("Maize");
        if (normalized.startsWith("soybean")) return cropRepository.findByNameIgnoreCaseAndActiveTrue("Soybean");
        if (normalized.startsWith("sugarcane")) return cropRepository.findByNameIgnoreCaseAndActiveTrue("Sugarcane");
        if (normalized.startsWith("wheat")) return cropRepository.findByNameIgnoreCaseAndActiveTrue("Wheat");
        if (normalized.startsWith("tomato")) return cropRepository.findByNameIgnoreCaseAndActiveTrue("Tomato");
        if (normalized.startsWith("grape")) return cropRepository.findByNameIgnoreCaseAndActiveTrue("Grapes");
        return Optional.empty();
    }

    private Mandi findOrCreateMandi(DataGovMandiResponse.MandiRecord record) {
        String market = record.market().trim();
        String district = record.district().trim();
        return mandiRepository.findFirstByNameIgnoreCaseAndDistrictIgnoreCaseAndStateIgnoreCase(
                market, district, record.state().trim()).orElseGet(() -> {
                    Mandi mandi = new Mandi();
                    mandi.setName(market);
                    mandi.setDistrict(district);
                    mandi.setState(record.state().trim());
                    mandi.setActive(true);
                    return mandiRepository.save(mandi);
                });
    }

    private boolean isUsable(DataGovMandiResponse.MandiRecord r) {
        return r != null && has(r.state()) && has(r.district()) && has(r.market())
                && has(r.commodity()) && has(r.arrivalDate())
                && r.minPrice() != null && r.maxPrice() != null && r.modalPrice() != null;
    }
    private boolean has(String s) { return s != null && !s.isBlank(); }
    private String clean(String s) { return s == null || s.isBlank() ? null : s.trim(); }

    private String sourceId(DataGovMandiResponse.MandiRecord r, LocalDate date) {
        String identity = String.join("|", r.state().trim(), r.district().trim(), r.market().trim(),
                r.commodity().trim(), clean(r.variety()) == null ? "" : clean(r.variety()),
                clean(r.grade()) == null ? "" : clean(r.grade()), date.toString());
        try {
            byte[] digest = MessageDigest.getInstance("SHA-256").digest(identity.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(digest);
        } catch (Exception e) { throw new IllegalStateException("Unable to create source record key", e); }
    }
}
