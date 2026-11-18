package com.manditracker.api.ingestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "mandi.import", name = "enabled", havingValue = "true")
public class ImportScheduler {
    private static final Logger log = LoggerFactory.getLogger(ImportScheduler.class);
    private final PriceImportService importService;
    public ImportScheduler(PriceImportService importService) { this.importService = importService; }

    @Scheduled(cron = "${mandi.import.cron:0 0 16 * * *}", zone = "Asia/Kolkata")
    public void importDailyPrices() {
        try { importService.importAvailablePrices(); }
        catch (Exception ex) { log.error("Scheduled mandi price import failed", ex); }
    }
}
