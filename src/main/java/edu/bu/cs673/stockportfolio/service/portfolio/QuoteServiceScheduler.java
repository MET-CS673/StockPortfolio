package edu.bu.cs673.stockportfolio.service.portfolio;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**********************************************************************************************************************
 *
 *
 *********************************************************************************************************************/
@Service
@EnableScheduling
public class QuoteServiceScheduler {

    private final QuoteService quoteService;
    private boolean isScheduled;

    public QuoteServiceScheduler(QuoteService quoteService) {
        this.quoteService = quoteService;
        this.isScheduled = false;
    }

    @Scheduled(fixedRate = 60000)
    public void schedule() {
        quoteService.getLatestPrices();
        isScheduled = true;
    }

    public boolean isScheduled() {
        return isScheduled;
    }
}
