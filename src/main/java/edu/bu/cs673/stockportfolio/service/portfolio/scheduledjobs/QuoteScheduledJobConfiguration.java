package edu.bu.cs673.stockportfolio.service.portfolio.scheduledjobs;

import edu.bu.cs673.stockportfolio.service.portfolio.QuoteService;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class QuoteScheduledJobConfiguration implements JobConfiguration {

    private static final FluentLogger LOGGER = FluentLoggerFactory.getLogger(QuoteScheduledJobConfiguration.class);
    private final QuoteService quoteService;

    public QuoteScheduledJobConfiguration(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    /**
     * Configures the schedule for getting updated market quotes from IEX Cloud. The task is started and stopped by
     * the MarketDataScheduler whenever the US Market is opens and closes. If the US Market is open, the product will
     * fetch new quotes at a rate defined by this configuration. Otherwise, the schedule will be stopped until the
     * market is open again.
     */
    @Override
    @Scheduled(fixedRate = 60 * 1000, initialDelay = 60 * 1000)
    public void scheduleByFixedRate() {
        LOGGER.info().log("Configuring the quote job schedule");
        quoteService.getLatestPrices();
    }
}
