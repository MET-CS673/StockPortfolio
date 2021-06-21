package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.security.Security;
import edu.bu.cs673.stockportfolio.service.utilities.IexCloudConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketDataService {

    private final String BASE_URL = "https://cloud.iexapis.com/stable/stock/";
    private final String QUERY_STRING = "/quote?filter=symbol,isUSMarketOpen,iexRealtimePrice,latestPrice";
    private final String TOKEN = "&token=";
    private final IexCloudConfig iexCloudConfig;
    private final RestTemplate restTemplate;

    public MarketDataService(IexCloudConfig iexCloudConfig, RestTemplate restTemplate) {
        this.iexCloudConfig = iexCloudConfig;
        this.restTemplate = restTemplate;
    }

    public void run() {
        String key = iexCloudConfig.getApiKey();

        Security security = restTemplate.getForObject(BASE_URL + "aapl" + QUERY_STRING + TOKEN + key, Security.class);
    }
}
