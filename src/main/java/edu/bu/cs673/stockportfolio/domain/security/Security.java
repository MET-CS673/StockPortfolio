package edu.bu.cs673.stockportfolio.domain.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import edu.bu.cs673.stockportfolio.domain.account.Account;

import javax.persistence.*;
import java.util.List;

/**********************************************************************************************************************
 * Data object representing investment products. Each security can be owned by an Account.
 *
 *********************************************************************************************************************/
@JsonPropertyOrder({
        "symbol",
        "isUSMarketOpen",
        "iexRealtimePrice",
        "latestPrice"
})
@Entity
public class Security {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("isUSMarketOpen")
    private Boolean isUSMarketOpen;
    @JsonProperty("iexRealtimePrice")
    private Double iexRealtimePrice;
    @JsonProperty("latestPrice")
    private Double latestPrice;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_line_test",
            joinColumns = {@JoinColumn(name = "security_id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id")}
    )
    private List<Account> accounts;

    /**
     * No args constructor for use in serialization
     *
     */
    public Security() {
    }

    /**
     *
     * @param symbol
     * @param latestPrice
     * @param isUSMarketOpen
     * @param iexRealtimePrice
     */
    public Security(String symbol, Boolean isUSMarketOpen, Double iexRealtimePrice, Double latestPrice) {
        super();
        this.symbol = symbol;
        this.isUSMarketOpen = isUSMarketOpen;
        this.iexRealtimePrice = iexRealtimePrice;
        this.latestPrice = latestPrice;
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

    @JsonProperty("isUSMarketOpen")
    public Boolean getIsUSMarketOpen() {
        return isUSMarketOpen;
    }

    @JsonProperty("isUSMarketOpen")
    public void setIsUSMarketOpen(Boolean isUSMarketOpen) {
        this.isUSMarketOpen = isUSMarketOpen;
    }

    @JsonProperty("iexRealtimePrice")
    public Double getIexRealtimePrice() {
        return iexRealtimePrice;
    }

    @JsonProperty("iexRealtimePrice")
    public void setIexRealtimePrice(Double iexRealtimePrice) {
        this.iexRealtimePrice = iexRealtimePrice;
    }

    @JsonProperty("latestPrice")
    public Double getLatestPrice() {
        return latestPrice;
    }

    @JsonProperty("latestPrice")
    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}