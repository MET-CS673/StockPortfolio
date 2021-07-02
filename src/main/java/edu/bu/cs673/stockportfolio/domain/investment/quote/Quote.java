package edu.bu.cs673.stockportfolio.domain.investment.quote;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.bu.cs673.stockportfolio.domain.account.Account;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**********************************************************************************************************************
 * Data object representing investment products. Each quote is for a specific symbol and can be associated with an
 * Account.
/*********************************************************************************************************************/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "symbol",
        "latestPrice",
        "marketCap"
})
@Entity
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("latestPrice")
    private BigDecimal latestPrice;

    @JsonProperty("marketCap")
    private Long marketCap;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_line",
            joinColumns = {@JoinColumn(name = "quote_id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id")}
    )
    private List<Account> accounts;

    /**
     * No args constructor for use in serialization.
     */
    public Quote() {
    }

    /**
     * A parameterized constructor for use in creating a quote.
     * @param symbol The ticker symbol associated with this quote.
     * @param latestPrice Either the realtime price if the market is open. Otherwise, the closing price.
     * @param marketCap The market capitalization of the company represented by the symbol.
     */
    public Quote(String symbol, BigDecimal latestPrice, Long marketCap) {
        super();
        this.symbol = symbol;
        this.latestPrice = latestPrice;
        this.marketCap = marketCap;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Long getMarketCap() {
        return marketCap;
    }

    @JsonProperty("marketCap")
    public void setMarketCap(Long marketCap) {
        this.marketCap = marketCap;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

}