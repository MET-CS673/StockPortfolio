package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.investment.quote.Quote;
import edu.bu.cs673.stockportfolio.domain.investment.quote.QuoteRepository;
import edu.bu.cs673.stockportfolio.domain.investment.quote.QuoteRoot;
import edu.bu.cs673.stockportfolio.domain.investment.quote.StockQuote;
import edu.bu.cs673.stockportfolio.domain.investment.sector.Company;
import edu.bu.cs673.stockportfolio.domain.investment.sector.CompanyRepository;
import edu.bu.cs673.stockportfolio.domain.investment.sector.CompanyRoot;
import edu.bu.cs673.stockportfolio.domain.investment.sector.StockSector;
import edu.bu.cs673.stockportfolio.service.company.CompanyService;
import edu.bu.cs673.stockportfolio.service.utilities.IexCloudConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class MarketDataServiceImpl implements MarketDataService {

    private final String BASE_URL = "https://cloud.iexapis.com/";
    private final String VERSION = "stable/";
    private final String TOKEN = "&token=";
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final QuoteRepository quoteRepository;
    private final CompanyService companyService;

    public MarketDataServiceImpl(IexCloudConfig iexCloudConfig, RestTemplate restTemplate,
                                 QuoteRepository quoteRepository, CompanyService companyService) {
        this.restTemplate = restTemplate;
        this.apiKey = iexCloudConfig.getApiKey();
        this.quoteRepository = quoteRepository;
        this.companyService = companyService;
    }

    @Override
    public List<Quote> doGetQuotes(Set<String> symbols) {

        // Convert the Set of Strings to a String for batch IEX request
        String symbolFilter = String.join(",", symbols);
        String endpointPath = "stock/market/batch";
        String queryParams = String.format(
                "?symbols=%s&types=quote&filter=" +
                        "companyName," +
                        "symbol," +
                        "latestPrice," +
                        "marketCap",
                symbolFilter);

        QuoteRoot quoteRoot = restTemplate.getForObject(
                BASE_URL + VERSION + endpointPath + queryParams + TOKEN + apiKey, QuoteRoot.class);

        Map<String, StockQuote> stocks = quoteRoot.getStocks();
        List<Quote> quotes = new ArrayList<>();
        stocks.forEach((key, value) -> {
            quotes.add(value.getQuote());
            quoteRepository.save(value.getQuote());
        });

        return quotes;
    }

    @Override
    public void addNewCompanies(Set<String> symbols) {
        String symbolFilter = String.join(",", symbols);
        String endpointPath = "stock/market/batch";
        String queryParams = String.format("?symbols=%s&types=company&filter=symbol,sector,companyName", symbolFilter);

        CompanyRoot companyRoot = restTemplate.getForObject(
                BASE_URL + VERSION + endpointPath + queryParams + TOKEN + apiKey, CompanyRoot.class);

        Map<String, StockSector> companyData = companyRoot.getCompanies();
        companyData.forEach((key, value) -> {
            companyService.add(value.getCompany());
        });
    }
}
