package edu.bu.cs673.stockportfolio.domain.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "quote"
})
public class Stock {

    @JsonProperty("quote")
    private Quote quote;

    public Stock() {
    }

    public Stock(Quote quote) {
        this.quote = quote;
    }

    @JsonProperty("quote")
    public Quote getQuote() {
        return quote;
    }

    @JsonProperty("quote")
    public void setQuote(Quote quote) {
        this.quote = quote;
    }
}
