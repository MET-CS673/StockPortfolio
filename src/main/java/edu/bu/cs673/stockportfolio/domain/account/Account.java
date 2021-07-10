package edu.bu.cs673.stockportfolio.domain.account;

import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.investment.quote.Quote;

import javax.persistence.*;
import java.util.List;

/**********************************************************************************************************************
 * Data object representing a users Account. An account holds investment products such as stocks.
 *
 *********************************************************************************************************************/
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private String accountNumber;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_line",
            joinColumns = {@JoinColumn(name = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "quote_id")}
    )
    private List<Quote> quotes;

    public Account() {
    }

    public Account(Portfolio portfolio, String accountNumber) {
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
    }

    public Account(Portfolio portfolio, String accountNumber, List<Quote> quotes) {
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
        this.quotes = quotes;
    }

    public Account(Long id, Portfolio portfolio, String accountNumber, List<Quote> quotes) {
        this.id = id;
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
        this.quotes = quotes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }
}
