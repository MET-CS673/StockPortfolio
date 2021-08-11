package edu.bu.cs673.stockportfolio.service.utilities;

import edu.bu.cs673.stockportfolio.domain.account.Account;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

/**********************************************************************************************************************
 * Validates client requests to ensure the current user has authorization to perform the 
 * specified action.
 *********************************************************************************************************************/
@Service
public class ValidationService {

    /**
     * <h3>This method attempts to validate a user, as the owner of an account</h3>
     * 
     * <p>Fetches the portfolio from the account and calls the 
     * {@link #validatePortfolioOwner(Portfolio, User, Model, String) validatePortfolioOwner(...)} to
     * validate that the user is the owner of the portfolio.</p>
     * 
     * @param account the account.
     * @param user the user.
     * @param model model object to provide data to template.
     * @param crud the crud operation.
     * @return true if the owner has been validated, false otherwise.
     */
    public boolean validateAccountOwner(Account account, User user, Model model, String crud) {
        Portfolio portfolio = account.getPortfolio();
        return validatePortfolioOwner(portfolio, user, model, crud);
    }

    /**
     * <h3>This method attempts to validate a user, as the owner of a portfolio</h3>
     * 
     * <p>If the portfolio is not null, and the value returned by 
     * {@link edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio#getUser() getUser()}
     * matches the test user, this method will return {@code true}.</p>
     * 
     * <p>If the passed in {@code portfolio} is {@code NULL}, an attribute "message" is 
     * added to the {@code model}, with the value "Portfolio not found.", and then this
     * method will return false.</p>
     * 
     * <p>If the passed in {@code portfolio}'s {@code getUser()} does not match the passed in
     * {@code user}, an attribute "message" is added to the {@code model}, with the value:
     * <div>"Error. Only the portfolio or account owner can perform an " + crud + "."</div>
     * and then this method will return false.</p>
     * 
     * @param portfolio the portfolio.
     * @param user the test user.
     * @param model the model object to provide data to template.
     * @param crud the crud operation.
     * @return true if the owner has been validated, false otherwise.
     */
    public boolean validatePortfolioOwner(Portfolio portfolio, User user, Model model, String crud){
        String message = null;
        boolean result = true;

        if (portfolio == null) {
            message = "Portfolio not found.";
        } else if (portfolio.getUser() != user) {
            message = "Error. Only the portfolio or account owner can perform an " + crud + ".";
        }

        if (message != null) {
            model.addAttribute("message", message);
            result = false;
        }

        return result;
    }
}
