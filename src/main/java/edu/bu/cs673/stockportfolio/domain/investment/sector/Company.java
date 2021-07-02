package edu.bu.cs673.stockportfolio.domain.investment.sector;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "symbol",
        "sector",
})
public class Company {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("sector")
    private String sector;

    /**
     * No args constructor for use in serialization
     */
    public Company() {
    }

    /**
     *
     * @param symbol
     * @param sector
     */
    public Company(String symbol, String sector) {
        super();
        this.symbol = symbol;
        this.sector = sector;
    }

    @JsonProperty("symbol")
    public String getSymbol() {
        return symbol;
    }

    @JsonProperty("symbol")
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @JsonProperty("sector")
    public String getSector() {
        return sector;
    }

    @JsonProperty("sector")
    public void setLatestPrice(String sector) {
        this.sector = sector;
    }
}