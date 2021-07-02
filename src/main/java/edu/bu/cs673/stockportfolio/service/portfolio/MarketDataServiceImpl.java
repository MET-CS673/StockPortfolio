package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.investment.quote.QuoteRepository;
import edu.bu.cs673.stockportfolio.domain.investment.quote.QuoteRoot;
import edu.bu.cs673.stockportfolio.domain.investment.quote.StockQuote;
import edu.bu.cs673.stockportfolio.domain.investment.sector.SectorRoot;
import edu.bu.cs673.stockportfolio.service.utilities.IexCloudConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final String BASE_URL = "https://cloud.iexapis.com/";
    private final String VERSION = "stable/";
    private final String TOKEN = "&token=";
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final QuoteRepository quoteRepository;

    public MarketDataServiceImpl(IexCloudConfig iexCloudConfig, RestTemplate restTemplate,
                                 QuoteRepository quoteRepository) {
        this.restTemplate = restTemplate;
        this.apiKey = iexCloudConfig.getApiKey();
        this.quoteRepository = quoteRepository;
    }

    @Override
    public void doGetQuotes() {
        String symbols = "aapl,fb,tsla";
        String endpointPath = "stock/market/batch";
        String queryParams = String.format("?symbols=%s&types=quote&filter=symbol,latestPrice,marketCap", symbols);

        QuoteRoot quoteRoot = restTemplate.getForObject(
                BASE_URL + VERSION + endpointPath + queryParams + TOKEN + apiKey, QuoteRoot.class);

        Map<String, StockQuote> stocks = quoteRoot.getStocks();
        stocks.forEach((key, value) -> {
            quoteRepository.save(value.getQuote());
        });
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
