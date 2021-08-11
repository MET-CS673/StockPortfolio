package edu.bu.cs673.stockportfolio.domain.account;

import edu.bu.cs673.stockportfolio.domain.investment.quote.Quote;

import javax.persistence.*;

/**********************************************************************************************************************
 * An AccountLine represents an associative entity between an Account and a Quote. An Account can 
 * have multiple investments, including different lots of the same symbol, and the AccountLine 
 * represents each of these investments.
 *********************************************************************************************************************/
@Entity
public class AccountLine {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quote_id")
    private Quote quote;

    private int quantity;

    /**
     * This constructor creates a new blank Account Line instance.
     */
    public AccountLine() {
    }

    /**
     * This constructor creates a new Account Line instance, with an account, quote and
     * the number of account lines given.
     * 
     * @param account the account.
     * @param quote the quote.
     * @param quantity the number of account lines.
     */
    public AccountLine(Account account, Quote quote, int quantity) {
        this.account = account;
        this.quote = quote;
        this.quantity = quantity;
    }

    /**
     * Returns the id.
     * 
     * @return id.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Returns the account.
     * 
     * @return the account.
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Sets the account.
     * 
     * @param account
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * Returns the quote.
     * 
     * @return quote.
     */
    public Quote getQuote() {
        return quote;
    }

    /**
     * Sets the quote.
     * 
     * @param quote
     */
    public void setQuote(Quote quote) {
        this.quote = quote;
    }

    /**
     * Returns the number of account lines.
     * 
     * @return quantity.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the number of account lines.
     * 
     * @param quantity
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
