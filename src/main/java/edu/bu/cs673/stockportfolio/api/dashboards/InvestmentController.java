package edu.bu.cs673.stockportfolio.api.dashboards;

import java.util.List;
import java.util.Map;

import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioNotFoundException;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.bu.cs673.stockportfolio.domain.account.Account;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;

/**
*
*   Controller maps to the url request "/stock_breakdown"
*/
@Controller
@RequestMapping("/stock_breakdown")
public class InvestmentController {
    private final PortfolioService portfolioService;
    private final ResponseService responseService;
    private final UserService userService;
    private final FluentLogger log = FluentLoggerFactory.getLogger(InvestmentController.class);

    /**
    *   Class Constructor that initializes the dependencies to PortfolioService,
    *   ResponseService and UserService
    *
    *   @param portfolioService The Portfolio request object
    *   @param responseService Object to package responses
    *   @param userService The User request object
    */
    public InvestmentController(PortfolioService portfolioService,
                                ResponseService responseService, UserService userService) {

        this.portfolioService = portfolioService;
        this.responseService = responseService;
        this.userService = userService;
    }

    /**
    *   When HTTP GET request is received for /stock_breakdown, securityBreakdownView()
    *   is called to handle the request
    *
    *   @param authentication Spring authentication object
    *   @param model Data from responseService added to model
    *   @return "Stock_breakdown" when /stock_breakdown is requested
    *
    */
    @GetMapping
    public String securityBreakdownView(Authentication authentication, Model model) {
        User user = getUser(authentication);
        Portfolio portfolio;
        Map<String, Float> data;

        if (user.getPortfolio() != null) {

            try {
                portfolio = portfolioService.getPortfolioBy(user.getPortfolio().getId());
                List<Account> accounts = portfolio.getAccounts();
                data = responseService.aggregateSumBySymbol(accounts);
                model.addAttribute("data", data);
            } catch (PortfolioNotFoundException e) {
                // Fail gracefully by logging error, while allowing control flow to return an empty html page.
                log.error().log("Portfolio not found.");
            }
        }
        return "stock_breakdown";
    }

    /**
    *   Get the user's username
    *
    *   @param authentication Spring authentication object
    *   @return The user's username
    *
    */
    private User getUser(Authentication authentication) {

        return userService.findUserByName(authentication.getName());
    }
}