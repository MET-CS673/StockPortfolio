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
import edu.bu.cs673.stockportfolio.service.utilities.MarketCapType;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;


/**
 * 
 * The controller for Market Cap Breakdown.
 * 
 * Depends on PortfolioService, ResponseService and UserService.
 * 
 */
@Controller
@RequestMapping("/mc_breakdown")
public class MarketCapController {
    private final PortfolioService portfolioService;
    private final ResponseService responseService;
    private final UserService userService;

    /**
     * 
     * Class constructor that handles the view of the market cap breakdown
     * 
     * @param portfolioService  the portfolio imported by the user
     * @param responseService   packages the responses from portfolioService
     * @param userService       ensures that the correct authenticated user is making the request
     */

    public MarketCapController(PortfolioService portfolioService, ResponseService responseService, UserService userService) {

        this.portfolioService = portfolioService;
        this.responseService = responseService;
        this.userService = userService;
    }

    /**
     * 
     * Provides a view of the market cap breakdown.
     * 
     * @param authentication    checks if the user is authenticated
     * @param model             contains the data from portfolioService and responseService
     * @return                  the market cap breakdown viewed by user
     */
    @GetMapping
    public String marketCapBreakdownView(Authentication authentication, Model model) {
        
        User user = getUser(authentication);
        Portfolio portfolio = null;
        Map<String, Float> data = new LinkedHashMap<String, Float>();

        Collection<String> stocksInMarketCap;
        if (user.getPortfolio() != null) {

            portfolio = portfolioService.getPortfolioBy(user.getPortfolio().getId());
            List<Account> accounts = portfolio.getAccounts();

            for ( MarketCapType marketCapType : MarketCapType.values() ) {

                data = responseService.aggregateSumBySymbol(accounts, marketCapType);
                stocksInMarketCap = data.keySet();
                System.out.println("\n\nMarket Cap: " + marketCapType.toString());
                for ( String stock : stocksInMarketCap ) {
                    System.out.println(stock);
                }
                model.addAttribute(marketCapType.toString(), data);
            }
        }

        return "mc_breakdown";
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
