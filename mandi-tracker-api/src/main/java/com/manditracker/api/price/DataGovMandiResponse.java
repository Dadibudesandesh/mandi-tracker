package com.manditracker.api.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataGovMandiResponse(
        List<MandiRecord> records,
        Integer total,
        Integer count,
        String limit,
        String offset,
        String status,
        String message
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MandiRecord(
            String state,
            String district,
            String market,
            String commodity,
            String variety,
            String grade,
            @JsonProperty("arrival_date") String arrivalDate,
            @JsonProperty("min_price") BigDecimal minPrice,
            @JsonProperty("max_price") BigDecimal maxPrice,
            @JsonProperty("modal_price") BigDecimal modalPrice
    ) {}
}
