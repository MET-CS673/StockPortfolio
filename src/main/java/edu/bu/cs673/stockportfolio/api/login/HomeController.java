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
 * 
 * Controller bean responsible for "home" requests
 *********************************************************************************************************************/
@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final PortfolioService portfolioService;
    private final ResponseService responseService;
    private final FluentLogger log = FluentLoggerFactory.getLogger(HomeController.class);

    /**
     * Creates a HomeController. (Autowired by Spring)
     * Responsible for handling request(s) to show the homepage.
     * 
     * @param userService An autowired implementation of the UserService (provided by the Spring dependency injection)
     * @param portfolioService An autowired implementation of the PortfolioService (provided by the Spring dependency injection)
     * @param responseService An autowired implementation of the ResponseService (provided by the Spring dependency injection)
     */
    public HomeController(UserService userService, PortfolioService portfolioService,
                          ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.responseService = responseService;
    }

    /**
     * <h3>GET Mapping '/home', shows the "home.html" view</h3>
     * 
     * <p>This method will take the authenticated user from Spring Authentication result,
     * and add it to the {@code model}.</p>
     * 
     * <p>If the user's portfolio property is not null, it will try to fetch the portfolio
     * from PortfolioService using the id of the portfolio. If the portfolio exists, it will get 
     * the list of accounts and create a portfolio table using
     * {@link edu.bu.cs673.stockportfolio.service.utilities.ResponseService#createPortfolioTable(List)
     * responseService.createPortfolioTable(...)}. Then this is added to model attribute "portfolio"
     * for rendering in the view '/home.html'.</p>
     * 
     * <p>If the portfolio is not found, then add an empty ArrayList to model attribute "portfolio"
     * for rendering in the view '/home.html'.</p>
     * 
     * <p>If the portfolio is null(user does not have a portfolio), add an empty ArrayList to 
     * model attribute "portfolio" for rendering in the view '/home.html'.</p>
     * 
     * @param authentication The Spring authentication object - used to get the User principal
     * @param model Model object to provide data to template
     * @return The name of the view to show 'resources/templates/home.html'
     * @see edu.bu.cs673.stockportfolio.service.utilities.ResponseService#createPortfolioTable(List)
     * responseService.createPortfolioTable(...)
     */
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