package edu.bu.cs673.stockportfolio.domain.user;

import com.sun.istack.NotNull;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;

/**********************************************************************************************************************
 * The User object represents a user of our software product. Each User can have a Portfolio with many Accounts. Each
 * Account can have many AccountLines. An AccountLine represents a symbol and quantity for an active investment.
 *********************************************************************************************************************/
@Entity
@Check(constraints = "CHECK (LENGTH(TRIM(username)) > 0) &&"
        + " CHECK (LENGTH(TRIM(email)) > 0) &&"
        + " CHECK (LENGTH(TRIM(password)) > 10)")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Nationalized
    @NotNull
    private String username;

    @Nationalized
    @NotNull
    private String password;

    private String salt;

    @Nationalized
    @NotNull
    private String email;

    // mappedBy creates a foreign key
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Portfolio portfolio;

    /**
     * This constructor creates a new blank User instance.
     */
    public User() {
    }

    /**
     * This constructor creates a new User instance, with a username, password,
     * salt, email and portfolio given.
     * 
     * @param username the username.
     * @param password the password.
     * @param salt the salt value - the randomly generated string.
     * @param email the email.
     * @param portfolio the portfolio.
     */
    public User(String username, String password, String salt, String email, Portfolio portfolio) {
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.portfolio = portfolio;
    }

    /**
     * This constructor creates a new User instance, with an id, username, password,
     * salt, email and portfolio given.
     * 
     * @param id the id.
     * @param username the username.
     * @param password the password.
     * @param salt the salt value - the randomly generated string.
     * @param email the email.
     * @param portfolio the portfolio.
     */
    public User(Long id, String username, String password, String salt, String email, Portfolio portfolio) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.portfolio = portfolio;
    }

    /**
     * Returns the id.
     * 
     * @return the id.
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
     * Returns the username.
     * 
     * @return the username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username.
     * 
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password.
     * 
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     * 
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the email. 
     * 
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     * 
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the salt value.
     * 
     * @return the salt
     */
    public String getSalt() {
        return salt;
    }

    /**
     * Sets the salt value.
     * 
     * @param salt
     */
    public void setSalt(String salt) {
        this.salt = salt;
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
}
