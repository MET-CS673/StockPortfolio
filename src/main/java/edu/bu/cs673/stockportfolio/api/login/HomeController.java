package edu.bu.cs673.stockportfolio.api.login;

import edu.bu.cs673.stockportfolio.domain.account.Account;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**********************************************************************************************************************
 * Handles requests to populate the homepage after a user is logged in.
 *********************************************************************************************************************/
@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final PortfolioService portfolioService;
    private final ResponseService responseService;

    /**
     * 
     * Class contructor that handles the view of the homepage
     * 
     * @param userService           ensures that the correct authenticated user is making the request
     * @param portfolioService      the portfolio imported by the user
     * @param responseService       packages the responses from portfolioService
     */
    public HomeController(UserService userService, PortfolioService portfolioService,
                          ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.responseService = responseService;
    }

    /**
     * Provides a view of homepage. 
     * 
     * The homepage is populated if the user has imported a portfolio.
     * 
     * @param authentication    checks if the user is authenticated
     * @param model             contains the data from portfolioService and responseService
     * @return                  the homepage viewed by user
     */
    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        User user = getUser(authentication);
        model.addAttribute("user", user);

        Portfolio portfolio = null;
        if (user.getPortfolio() != null) {

            portfolio = portfolioService.getPortfolioBy(user.getPortfolio().getId());
        } else {
            model.addAttribute("portfolio", new ArrayList<>());
        }

        if (portfolio != null) {
            List<Account> accounts = portfolio.getAccounts();
            model.addAttribute("portfolio", responseService.createPortfolioTable(accounts));
        } else {
            model.addAttribute("portfolio", new ArrayList<>());
        }
        
        return "home";
    }

    /**
     * Checks if the correct user is authenticated and has access to their individual account
     * 
     * @param authentication    checks the credentials of the user
     * @return                  True if the authenticated user is registered
     */
    private User getUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }
}