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
 * Controller bean responsible for handling "stock_breakdown" requests
 */
@Controller
@RequestMapping("/stock_breakdown")
public class InvestmentController {
    private final PortfolioService portfolioService;
    private final ResponseService responseService;
    private final UserService userService;
    private final FluentLogger log = FluentLoggerFactory.getLogger(InvestmentController.class);

    /**
     * Creates an InvestmentController. (Autowired by Spring)
     * Responsible for handling request(s) to show stock breakdowns.
     * 
     * @param portfolioService An autowired implementation of the PortfolioService (provided by the Spring dependency injection)
     * @param responseService An autowired implementation of the ResponseService (provided by the Spring dependency injection)
     * @param userService An autowired implementation of the UserService (provided by the Spring dependency injection)
     */
    public InvestmentController(PortfolioService portfolioService,
                                ResponseService responseService, UserService userService) {

        this.portfolioService = portfolioService;
        this.responseService = responseService;
        this.userService = userService;
    }

    /**
     * <h3>GET Mapping for '/stock_breakdown', shows the "stock_breakdown" view</h3>
     * 
     * <p>This method will take the authenticated user from Spring Authentication result and
     * look up the user's portfolio using portfolioService. From the portfolio, it will
     * get the user's accounts and then it will aggregate the account data and add that result
     * to the model attribute data for rendering in the view '/stock_breakdown.html'.</p>
     * 
     * @param authentication The Spring authentication object - used to get the User principal
     * @param model Model object to provide data to template
     * @return The name of the view to show 'resources/templates/stock_breakdown.html'
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
     * Get the User object from the Authentication principal.
     * 
     * @param authentication The Spring authentication object - used to get the User principal
     * @return The User object if found, null otherwise
     */
    private User getUser(Authentication authentication) {

        return userService.findUserByName(authentication.getName());
    }
}