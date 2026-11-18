
package com.manditracker.api.price;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DataGovMandiResponse(
        List<MandiRecord> records,
        Integer total,
        Integer count,
        Integer limit,
        Integer offset
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record MandiRecord(
            String state,
            String district,
            String market,
            String commodity,
            String variety,
            String grade,

            @JsonProperty("arrival_date")
            String arrivalDate,

            @JsonProperty("min_price")
            String minPrice,

            @JsonProperty("max_price")
            String maxPrice,

            @JsonProperty("modal_price")
            String modalPrice
    ) {}
}
