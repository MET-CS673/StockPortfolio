package edu.bu.cs673.stockportfolio.domain.security.sectors;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.util.LinkedHashMap;
import java.util.Map;

public class SectorRoot {
    private Map<String, StockSector> sectors = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, StockSector> getCompanies() {
        return this.sectors;
    }

    @JsonAnySetter
    public void addCompany(String ticker, StockSector sector) {
        this.sectors.put(ticker, sector);
    }
}