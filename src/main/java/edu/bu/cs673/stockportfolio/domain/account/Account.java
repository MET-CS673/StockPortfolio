package edu.bu.cs673.stockportfolio.domain.account;

import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.investment.quote.Quote;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************************************
 * Data object representing a users Account. An account holds investment products such as stocks.
 *
 *********************************************************************************************************************/
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private String accountNumber;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    private List<AccountLine> accountLines = new ArrayList<>();

    public Account() {
    }

    public Account(Portfolio portfolio, String accountNumber) {
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
    }

    public Account(Portfolio portfolio, String accountNumber, List<AccountLine> accountLines) {
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
        this.accountLines = accountLines;
    }

    public Account(Long id, Portfolio portfolio, String accountNumber, List<AccountLine> accountLines) {
        this.id = id;
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
        this.accountLines = accountLines;
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

    public List<AccountLine> getAccountLines() {
        return accountLines;
    }

    public void setAccountLines(List<AccountLine> quotes) {
        this.accountLines = accountLines;
    }
}
