package com.manditracker.api.ingestion;

import com.manditracker.api.price.DataGovMandiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@Component
public class GovernmentPriceClient {
    private final RestClient restClient;
    private final String apiKey;
    private final String resourceId;
    private final String state;
    private final String district;
    private final int pageSize;

    public GovernmentPriceClient(RestClient.Builder builder,
            @Value("${mandi.import.api-key:}") String apiKey,
            @Value("${mandi.import.resource-id:9ef84268-d588-465a-a308-a864a43d0070}") String resourceId,
            @Value("${mandi.import.state:Maharashtra}") String state,
            @Value("${mandi.import.district:}") String district,
            @Value("${mandi.import.page-size:10}") int pageSize) {
        this.restClient = builder.baseUrl("https://api.data.gov.in").build();
        this.apiKey = apiKey;
        this.resourceId = resourceId;
        this.state = state;
        this.district = district;
        this.pageSize = pageSize;
    }

    public DataGovMandiResponse fetchPage(int offset) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("DATA_GOV_IN_API_KEY is not configured.");
        }
        UriComponentsBuilder uri = UriComponentsBuilder
                .fromPath("/resource/" + resourceId)
                .queryParam("api-key", apiKey)
                .queryParam("format", "json")
                .queryParam("limit", pageSize)
                .queryParam("offset", offset)
                .queryParam("filters[state.keyword]", state);
        if (district != null && !district.isBlank()) {
            uri.queryParam("filters[district]", district.trim());
        }
        URI requestUri = uri.encode().build().toUri();
        DataGovMandiResponse response = restClient.get().uri(requestUri)
                .retrieve().body(DataGovMandiResponse.class);
        if (response == null || response.records() == null) {
            throw new IllegalStateException("data.gov.in returned an empty or invalid response.");
        }
        return response;
    }
}
