package edu.bu.cs673.stockportfolio.api.login;

import edu.bu.cs673.stockportfolio.domain.account.Account;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.authentication.HashService;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioNotFoundException;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
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
    private final FluentLogger log = FluentLoggerFactory.getLogger(HomeController.class);

    public HomeController(UserService userService, PortfolioService portfolioService,
                          ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.responseService = responseService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        User user = getUser(authentication);
        model.addAttribute("user", user);

        Portfolio portfolio = null;
        if (user.getPortfolio() != null) {
            try {
                portfolio = portfolioService.getPortfolioBy(user.getPortfolio().getId());
            } catch (PortfolioNotFoundException e) {
                // Fail gracefully by logging error and returning an arrayList to mimic an empty portfolio
                log.error().log("Portfolio not found.");
                model.addAttribute("portfolio", new ArrayList<>());
            }
        }

        if (portfolio != null) {
            List<Account> accounts = portfolio.getAccounts();
            model.addAttribute("portfolio", responseService.createPortfolioTable(accounts));
        }
        
        return "home";
    }

    private User getUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }
}