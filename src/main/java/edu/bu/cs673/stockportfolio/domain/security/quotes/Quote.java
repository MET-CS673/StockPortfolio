package edu.bu.cs673.stockportfolio.domain.security.quotes;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.math.BigInteger;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "symbol",
        "latestPrice",
        "marketCap"
})
public class Quote {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("latestPrice")
    private BigDecimal latestPrice;
    @JsonProperty("marketCap")
    private BigInteger marketCap;

    /**
     * No args constructor for use in serialization
     */
    public Quote() {
    }

    /**
     *
     * @param symbol
     * @param latestPrice
     * @param marketCap
     */
    public Quote(String symbol, BigDecimal latestPrice, BigInteger marketCap) {
        super();
        this.symbol = symbol;
        this.latestPrice = latestPrice;
        this.marketCap = marketCap;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("latestPrice")
    public BigDecimal getLatestPrice() {
        return latestPrice;
    }

    @JsonProperty("latestPrice")
    public void setLatestPrice(BigDecimal latestPrice) {
        this.latestPrice = latestPrice;
    }

    @JsonProperty("marketCap")
    public BigInteger getMarketCap() {
        return marketCap;
    }

    @JsonProperty("marketCap")
    public void setMarketCap(BigInteger marketCap) {
        this.marketCap = marketCap;
    }
}