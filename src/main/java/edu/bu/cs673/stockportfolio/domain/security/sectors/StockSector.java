package edu.bu.cs673.stockportfolio.domain.security.sectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "company"
})
public class StockSector {

    @JsonProperty("company")
    private Company company;

    public StockSector() {
    }

    public StockSector(Company company) {
        this.company = company;
    }

    @JsonProperty("company")
    public Company getQuote() {
        return company;
    }

    @JsonProperty("company")
    public void setQuote(Company company) {
        this.company = company;
    }
}