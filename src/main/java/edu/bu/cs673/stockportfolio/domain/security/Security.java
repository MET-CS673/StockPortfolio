package edu.bu.cs673.stockportfolio.domain.security;

import edu.bu.cs673.stockportfolio.domain.account.Account;

import javax.persistence.*;
import java.util.List;

/**********************************************************************************************************************
 * Data object representing investment products. Each security can be owned by an Account.
 *
 *********************************************************************************************************************/
@Entity
public class Security {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String symbol;

    private String companyName;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "account_line",
            joinColumns = {@JoinColumn(name = "security_id")},
            inverseJoinColumns = {@JoinColumn(name = "account_id")}
    )
    private List<Account> accounts;

    public Security() {
    }

    public Security(String symbol, String companyName) {
        this.symbol = symbol;
        this.companyName = companyName;
    }

    public Security(Long id, String symbol, String companyName) {
        this.id = id;
        this.symbol = symbol;
        this.companyName = companyName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
