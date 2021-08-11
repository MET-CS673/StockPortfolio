package edu.bu.cs673.stockportfolio.domain.investment.quote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The JPA Repository for Quote data.
 */
public interface QuoteRepository extends JpaRepository<Quote, Long> {

    /**
     * Finds all quotes by a given symbol.
     * 
     * @param symbol the ticker symbol.
     * @return the list of quotes.
     */
    List<Quote> findQuotesBySymbol(String symbol);

    /**
     * Finds a quote by a given symbol.
     * 
     * @param symbol the ticker symbol.
     * @return the quote.
     */
    Quote findQuoteBySymbol(String symbol);
}
