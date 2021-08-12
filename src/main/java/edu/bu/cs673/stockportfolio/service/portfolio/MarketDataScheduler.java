package edu.bu.cs673.stockportfolio.service.portfolio;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class MarketDataScheduler {

    private final MarketDataServiceImpl marketDataServiceImpl;
    private final QuoteServiceScheduler quoteServiceScheduler;
    private boolean isUSMarketOpen;

    public MarketDataScheduler(MarketDataServiceImpl marketDataServiceImpl,
                               QuoteServiceScheduler quoteServiceScheduler) {
        this.marketDataServiceImpl = marketDataServiceImpl;
        this.quoteServiceScheduler = quoteServiceScheduler;
    }

    /**
     * Checks whether or not the US Market is open on 1 minute intervals.
     */
    @Scheduled(fixedRate = 60000)
    public void checkIfUSMarketIsOpen() {
        isUSMarketOpen = marketDataServiceImpl.isUSMarketOpen();

        if (isUSMarketOpen) {
            quoteServiceScheduler.schedule();
        }
    }

    public boolean isUSMarketOpen() {
        return isUSMarketOpen;
    }
}
