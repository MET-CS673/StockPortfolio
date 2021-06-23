package edu.bu.cs673.stockportfolio.domain.security;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Root {
    private Map<String, Stock> stocks = new LinkedHashMap<>();

    @JsonAnyGetter
    public Map<String, Stock> getStocks() {
        return this.stocks;
    }

    @JsonAnySetter
    public void addStock(String name, Stock value) {
        this.stocks.put(name, value);
    }
}