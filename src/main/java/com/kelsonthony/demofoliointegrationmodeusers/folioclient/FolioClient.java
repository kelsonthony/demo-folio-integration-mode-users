package com.kelsonthony.demofoliointegrationmodeusers.folioclient;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class FolioClient {
    @Value("${folio.api.token}")
    private String apiToken;

    @Value("${folio.api.base-url}")
    private String apiBaseUrl;

    public String getApiToken() {
        return apiToken;
    }

    public String getApiBaseUrl() {
        return apiBaseUrl;
    }
}
