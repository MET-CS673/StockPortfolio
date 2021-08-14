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
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
public class MarketDataServiceImpl implements MarketDataService {

    private static final String BASE_URL = "https://cloud.iexapis.com/";
    private static final String VERSION = "stable/";
    private static final String TOKEN = "&token=";
    private final RestTemplate restTemplate;
    private final String apiKey;
    private final QuoteRepository quoteRepository;
    private final CompanyService companyService;
    private final FluentLogger log = FluentLoggerFactory.getLogger(MarketDataServiceImpl.class);

    public MarketDataServiceImpl(IexCloudConfig iexCloudConfig, RestTemplate restTemplate,
                                 QuoteRepository quoteRepository, CompanyService companyService) {
        this.restTemplate = restTemplate;
        this.apiKey = iexCloudConfig.getApiKey();
        this.quoteRepository = quoteRepository;
        this.companyService = companyService;
    }

    @Override
    public boolean isUSMarketOpen() {
        log.info().log("Checking if US Market is open");
        // Use a well known symbol to check if market is open.
        String symbol = "GS";
        String field = "isUSMarketOpen";
        String endpointPath = String.format("stock/%s/quote/%s?token=", symbol, field);

        Boolean isUSMarketOpen = restTemplate.getForObject(
                BASE_URL + VERSION + endpointPath + apiKey, Boolean.class);

       return isUSMarketOpen != null && isUSMarketOpen;
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

        List<Quote> quotes = new ArrayList<>();
        if (quoteRoot != null) {
            Map<String, StockQuote> stocks = quoteRoot.getStocks();
            stocks.forEach((key, value) -> {
                Quote quoteToBeUpdated = getExistingQuote(value);
                quoteRepository.save(quoteToBeUpdated);
                quotes.add(quoteToBeUpdated);
            });
        }

        return quotes;
    }

    // If the symbol already exists, avoid polluting the database by updating the existing quote.
    // If the symbol is not yet in the database return the new quote so it can be added to the database.
    private Quote getExistingQuote(StockQuote stockQuote) {
        Quote quote = stockQuote.getQuote();
        String symbol = quote.getSymbol();

        Quote existingQuote = quoteRepository.findQuoteBySymbol(symbol);
        if (existingQuote != null) {
            existingQuote.setMarketCap(quote.getMarketCap());
            existingQuote.setLatestPrice(quote.getLatestPrice());
            return existingQuote;
        }

        return quote;
    }

    @Override
    public List<Company> doGetCompanies(Set<String> symbols) {

        // We don't need to retrieve Company data that we already have so befory making the request, remove existing
        // symbols from the set.
        Set<String> newSymbols = new HashSet<String>();
        for (String symbol : symbols) {
            
            if ( !companyService.contains(symbol) ) {

                newSymbols.add(symbol);
            }
        }

        // If there are no new companies in this set, return early an empty list
        if ( newSymbols.isEmpty() ) return new ArrayList<>();

        String symbolFilter = String.join(",", newSymbols);
        String endpointPath = "stock/market/batch";
        String queryParams = String.format("?symbols=%s&types=company&filter=symbol,sector,companyName", symbolFilter);

        CompanyRoot companyRoot = restTemplate.getForObject(
                BASE_URL + VERSION + endpointPath + queryParams + TOKEN + apiKey, CompanyRoot.class);

        List<Company> companies = new ArrayList<>();
        if (companyRoot != null) {
            Map<String, StockSector> companyData = companyRoot.getCompanies();
            companyData.forEach((key, value) -> {
                companies.add(value.getCompany());
            });
        }

        return companies;
    }
}
