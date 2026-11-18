package com.manditracker.api.ingestion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "mandi.import", name = "run-on-startup", havingValue = "true")
public class ImportStartupRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(ImportStartupRunner.class);
    private final PriceImportService importService;
    public ImportStartupRunner(PriceImportService importService) { this.importService = importService; }
    @Override public void run(String... args) {
        try { log.info("One-time mandi import result: {}", importService.importAvailablePrices()); }
        catch (Exception ex) { log.error("One-time mandi import failed", ex); }
    }
}





