package edu.bu.cs673.stockportfolio.domain.investment.sector;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompanyRoot {
    private Map<String, StockSector> companies = new LinkedHashMap<>();

    /**
     * 
     * @return Companies mapped with name and sector.
     */
    @JsonAnyGetter
    public Map<String, StockSector> getCompanies() {
        return this.companies;
    }

    /**
     * Add company based on the ticker symbol and sector.
     * 
     * @param ticker the ticker symbol of a company.
     * @param sector the sector of a security.
     */
    @JsonAnySetter
    public void addCompany(String ticker, StockSector sector) {
        this.companies.put(ticker, sector);
    }
}