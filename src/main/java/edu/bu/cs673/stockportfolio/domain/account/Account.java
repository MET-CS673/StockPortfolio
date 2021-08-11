package edu.bu.cs673.stockportfolio.domain.account;

import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************************************
 * Data object representing a users Account. An account holds investment products such as stocks.
 *********************************************************************************************************************/
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    private String accountNumber;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccountLine> accountLines = new ArrayList<>();

    /**
     * This constructor creates a new blank Account instance.
     */
    public Account() {
    }

    /**
     * This constructor creates a new Account instance, with a portfolio and
     * account number given.
     * 
     * @param portfolio the portfolio.
     * @param accountNumber the account number.
     */
    public Account(Portfolio portfolio, String accountNumber) {
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
    }

    /**
     * This constructor creates a new Account instance, with a portfolio,
     * account number and the list of account lines given.
     * 
     * @param portfolio the portfolio.
     * @param accountNumber the account number.
     * @param accountLines the list of account lines.
     */
    public Account(Portfolio portfolio, String accountNumber, List<AccountLine> accountLines) {
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
        this.accountLines = accountLines;
    }

    /**
     * This constructor creates a new Account instance, with an id, portfolio,
     * account number and the list of account lines given.
     * 
     * @param id reference to the id of the account.
     * @param portfolio the portfolio.
     * @param accountNumber the account number.
     * @param accountLines the list of account lines.
     */
    public Account(Long id, Portfolio portfolio, String accountNumber, List<AccountLine> accountLines) {
        this.id = id;
        this.portfolio = portfolio;
        this.accountNumber = accountNumber;
        this.accountLines = accountLines;
    }

    /**
     * Returns the id of an account.
     * 
     * @return id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id of an account.
     * 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the portfolio.
     * 
     * @return the portfolio.
     */
    public Portfolio getPortfolio() {
        return portfolio;
    }

    /**
     * Sets the portfolio.
     * 
     * @param portfolio
     */
    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    /**
     * Returns the account number.
     * 
     * @return the account number.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the account number.
     * 
     * @param accountNumber
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Returns the list of account lines.
     * 
     * @return the account lines.
     */
    public List<AccountLine> getAccountLines() {
        return accountLines;
    }

    /**
     * Sets the account lines.
     * 
     * @param accountLines
     */
    public void setAccountLines(List<AccountLine> accountLines) {
        this.accountLines = accountLines;
    }
}
