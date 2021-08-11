package edu.bu.cs673.stockportfolio.domain.investment.quote;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class QuoteRoot {
    private Map<String, StockQuote> stocks = new LinkedHashMap<>();

    /**
     * Returns a map of all the stock quotes with the stock ticker as the key.
     * 
     * @return stocks mapped with the name and value.
     */
    @JsonAnyGetter
    public Map<String, StockQuote> getStocks() {
        return this.stocks;
    }

    /**
     * Add stock based on the name and value.
     * 
     * @param name name of the stock.
     * @param value value of the stock.
     */
    @JsonAnySetter
    public void addStock(String name, StockQuote value) {
        this.stocks.put(name, value);
    }
}