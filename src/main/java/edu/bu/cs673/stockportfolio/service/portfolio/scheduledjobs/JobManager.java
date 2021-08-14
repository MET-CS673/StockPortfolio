package edu.bu.cs673.stockportfolio.service.portfolio.scheduledjobs;

import edu.bu.cs673.stockportfolio.service.portfolio.MarketDataServiceImpl;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class JobManager {

    private static final FluentLogger LOGGER = FluentLoggerFactory.getLogger(JobManager.class);
    private static final String QUOTE_SCHEDULED_TASK = "quoteSchedulerConfiguration";
    private final MarketDataServiceImpl marketDataServiceImpl;
    private final JobWorker jobWorker;
    private final QuoteScheduledJobConfiguration quoteScheduledJobConfiguration;
    private boolean isQuoteJobRunning;

    public JobManager(MarketDataServiceImpl marketDataServiceImpl,
                      JobWorker jobWorker,
                      QuoteScheduledJobConfiguration quoteScheduledJobConfiguration) {
        this.marketDataServiceImpl = marketDataServiceImpl;
        this.jobWorker = jobWorker;
        this.quoteScheduledJobConfiguration = quoteScheduledJobConfiguration;
        isQuoteJobRunning = false;
    }

    /**
     * Configures the schedule for checking if the US Market is open and start the task.
     */
    public void startSchedule() {
        boolean isMarketOpen = isUSMarketOpen();

        if (isMarketOpen && !isQuoteJobRunning) {
            LOGGER.info().log("US market is open. Start real-time price updates");
            jobWorker.startJob(quoteScheduledJobConfiguration, QUOTE_SCHEDULED_TASK);
            isQuoteJobRunning = true;
        }

        if (!isMarketOpen) {
            LOGGER.info().log("US market is closed. Stop real-time price updates");
            jobWorker.stopJob(quoteScheduledJobConfiguration, QUOTE_SCHEDULED_TASK);
            isQuoteJobRunning = false;
        }
    }

    public boolean isUSMarketOpen() {
        return marketDataServiceImpl.isUSMarketOpen();
    }
}