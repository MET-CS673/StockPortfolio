package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.security.quotes.QuoteRoot;
import edu.bu.cs673.stockportfolio.domain.security.sectors.SectorRoot;
import edu.bu.cs673.stockportfolio.service.utilities.IexCloudConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final String BASE_URL = "https://cloud.iexapis.com/";
    private final String VERSION = "stable/";
    private final String TOKEN = "&token=";
    private final IexCloudConfig iexCloudConfig;
    private final RestTemplate restTemplate;
    private final String apiKey;

    public MarketDataServiceImpl(IexCloudConfig iexCloudConfig, RestTemplate restTemplate) {
        this.iexCloudConfig = iexCloudConfig;
        this.restTemplate = restTemplate;
        this.apiKey = iexCloudConfig.getApiKey();
    }

    @Override
    public void doGetQuotes() {
        String symbols = "aapl,fb,tsla";
        String endpointPath = "stock/market/batch";
        String queryParams = String.format("?symbols=%s&types=quote&filter=symbol,latestPrice", symbols);

        QuoteRoot quoteRoot = restTemplate.getForObject(
                BASE_URL + VERSION + endpointPath + queryParams + TOKEN + apiKey, QuoteRoot.class);
    }

    @Override
    public void doGetSector() {
        String symbols = "aapl,fb,tsla";
        String endpointPath = "stock/market/batch";
        String queryParams = String.format("?symbols=%s&types=company&filter=symbol,sector", symbols);

        SectorRoot sectorRoot = restTemplate.getForObject(
                BASE_URL + VERSION + endpointPath + queryParams + TOKEN + apiKey, SectorRoot.class);
    }
}
