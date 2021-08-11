package edu.bu.cs673.stockportfolio.domain.investment.sector;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.bu.cs673.stockportfolio.domain.investment.sector.Company;

@JsonPropertyOrder({
        "company"
})
public class StockSector {

    @JsonProperty("company")
    private Company company;

    /**
     * This constructor creates a new blank StockSector instance.
     */
    public StockSector() {
    }

    /**
     * This constructor creates a new StockSector instance, with a company given.
     * 
     * @param company
     */
    public StockSector(Company company) {
        this.company = company;
    }

    /**
     * Returns the company.
     * 
     * @return the company.
     */
    @JsonProperty("company")
    public Company getCompany() {
        return company;
    }

    /**
     * Sets the company.
     * 
     * @param company
     */
    @JsonProperty("company")
    public void setCompany(Company company) {
        this.company = company;
    }
}