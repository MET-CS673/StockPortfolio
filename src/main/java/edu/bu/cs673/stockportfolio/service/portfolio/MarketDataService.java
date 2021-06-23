package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.security.Root;
import edu.bu.cs673.stockportfolio.domain.security.Security;
import edu.bu.cs673.stockportfolio.service.utilities.IexCloudConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketDataService {

    private final String BASE_URL = "https://cloud.iexapis.com/";
    private final String VERSION = "stable/";
    private final String TOKEN = "&token=";
    private final IexCloudConfig iexCloudConfig;
    private final RestTemplate restTemplate;

    public MarketDataService(IexCloudConfig iexCloudConfig, RestTemplate restTemplate) {
        this.iexCloudConfig = iexCloudConfig;
        this.restTemplate = restTemplate;
    }

    public void run() {
        String symbol = "aapl";
        String endpointPath = String.format("stock/%s/quote", symbol);
        String queryParams = "?filter=symbol,isUSMarketOpen,iexRealtimePrice,latestPrice";
        String key = iexCloudConfig.getApiKey();

        Security security = restTemplate.getForObject(BASE_URL + VERSION + endpointPath + queryParams + TOKEN + key, Security.class);
    }

    public void runBatch() {
        String symbols = "aapl,fb,tsla";
        String endpointPath = "stock/market/batch";
        String queryParams = String.format("?symbols=%s&types=quote&filter=symbol,latestPrice", symbols);
        String key = iexCloudConfig.getApiKey();

        System.out.println("\n\n\n");
        System.out.println(BASE_URL + VERSION + endpointPath + queryParams + TOKEN + key);

        Root root = restTemplate.getForObject(BASE_URL + VERSION + endpointPath + queryParams + TOKEN + key, Root.class);

    }
}
