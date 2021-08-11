package edu.bu.cs673.stockportfolio.domain.investment.quote;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "quote"
})
public class StockQuote {

    @JsonProperty("quote")
    private Quote quote;

    /**
     * This constructor creates a new blank Stock Quote instance.
     */
    public StockQuote() {
    }

    /**
     * This constructor creates a new StockQuote instance, with a quote given.
     * 
     * @param quote the quote.
     */
    public StockQuote(Quote quote) {
        this.quote = quote;
    }

    /**
     * Returns the quote.
     * 
     * @return the quote.
     */
    @JsonProperty("quote")
    public Quote getQuote() {
        return quote;
    }

    /**
     * Sets the quote.
     * 
     * @param quote
     */
    @JsonProperty("quote")
    public void setQuote(Quote quote) {
        this.quote = quote;
    }
}
