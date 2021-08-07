package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.investment.quote.Quote;
import edu.bu.cs673.stockportfolio.domain.investment.quote.QuoteRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class QuoteService {

    private final QuoteRepository quoteRepository;
    private final MarketDataServiceImpl marketDataServiceImpl;

    public QuoteService(QuoteRepository quoteRepository, MarketDataServiceImpl marketDataServiceImpl) {
        this.quoteRepository = quoteRepository;
        this.marketDataServiceImpl = marketDataServiceImpl;
    }

    /**
     * Creates a ScheduledExecutorService to run a task every 1 minute. The task will GET the latestPrice for each
     * symbol in the Portfolio from IEXCloud.
     */
    public void getLatestPrices() {
        BlockingQueue<List<Quote>> results = new LinkedBlockingDeque<>();

        Runnable task = () -> {

            List<Quote> existingQuotes = quoteRepository.findAll();

            // Package the existing quotes into a set for the marketDataServiceImpl
            Set<String> allSymbols = new HashSet<>();
            existingQuotes.forEach(quoteToBeUpdated -> {
                allSymbols.add(String.join(",", quoteToBeUpdated.getSymbol()));
            });

            // GET new quotes from IEX Cloud
            List<Quote> quotes = marketDataServiceImpl.doGetQuotes(allSymbols);

            quotes.forEach(quote -> {
                existingQuotes.forEach(existingQuote -> {
                    if (existingQuote.getSymbol().equals(quote.getSymbol())) {

                        // Pull the existing quote into the persistence context
                        Optional<Quote> optionalQuote = quoteRepository.findById(existingQuote.getId());

                        if (optionalQuote.isPresent()) {
                            Quote quoteToBeUpdated = optionalQuote.get();
                            quoteToBeUpdated.setLatestPrice(quote.getLatestPrice());
                        }
                    }
                });
            });

            results.add(existingQuotes);
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(task,0, 1, TimeUnit.MINUTES);
    }
}
