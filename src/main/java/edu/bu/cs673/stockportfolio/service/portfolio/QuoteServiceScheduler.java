package edu.bu.cs673.stockportfolio.service.portfolio;

import org.springframework.stereotype.Service;

@Service
public class QuoteServiceScheduler {

    private final QuoteService quoteService;

    public QuoteServiceScheduler(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    public void schedule() {
        quoteService.getLatestPrices();
    }
}
