package edu.bu.cs673.stockportfolio.api.dashboards;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * Controller bean responsible for handling "sector_breakdown" requests.
 */
@Controller
@RequestMapping("/sector_breakdown")
public class SectorController {

    private final PortfolioService portfolioService;
    private final ResponseService responseService;
    private final UserService userService;

    /**
     * Creates an SectorController. (Autowired by Spring).
     * Responsible for handling request(s) to show sector breakdowns.
     * 
     * @param portfolioService the PortfolioService (provided by Spring dependency injection).
     * @param responseService the ResponseService (provided by Spring dependency injection).
     * @param userService the UserService (provided by Spring dependency injection).
     */
    public SectorController(PortfolioService portfolioService, ResponseService responseService, UserService userService) {

        this.portfolioService = portfolioService;
        this.responseService = responseService;
        this.userService = userService;
    }

    /**
     * <h3>GET Mapping for '/sector_breakdown', shows the "sector_breakdown" view </h3>
     * 
     * <p>This method will take the authenticated user from Spring Authentication result
     * and look up the user's portfolio using portfolioService. From the portfolio, it will get 
     * the user's accounts and then it will aggregate the account data by sector and add 
     * that result to the model attribute "data" for rendering in the view 
     * '/sector_breakdown.html'.</p>
     * 
     * @param authentication the Spring authentication object - used to get the User principal.
     * @param model the Model object to provide data to template.
     * @return the name of the view to show 'resources/templates/sector_breakdown.html'.
     */
    @GetMapping
    public String sectorBreakdownView(Authentication authentication, Model model) {
        
        User user = getUser(authentication);
        Portfolio portfolio = null;
        Map<String, Float> data = new LinkedHashMap<String, Float>();
            
        if (user.getPortfolio() != null) {

            portfolio = portfolioService.getPortfolioBy(user.getPortfolio().getId());
            List<Account> accounts = portfolio.getAccounts();

            data = responseService.aggregateSumBySector(accounts);
        }

        model.addAttribute("data", data);
        return "sector_breakdown";
    }

    /*
     * Get the User Object from Authentication principal
     * 
     * @param authentication the Spring authentication object - used to get the User principal. 
     * @return the User object if found, null otherwise.
     */
    private User getUser(Authentication authentication) {

        return userService.findUserByName(authentication.getName());
    }
}
