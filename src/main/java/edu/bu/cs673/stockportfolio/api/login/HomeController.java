package edu.bu.cs673.stockportfolio.api.login;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.MarketDataService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**********************************************************************************************************************
 * Handles requests to populate the homepage after a user is logged in.
 *********************************************************************************************************************/
@Controller
@RequestMapping("/home")
public class HomeController {

    private final UserService userService;
    private final MarketDataService marketDataService;

    public HomeController(UserService userService, MarketDataService marketDataService) {
        this.userService = userService;
        this.marketDataService = marketDataService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        User user = userService.findUserByName(authentication.getName());

        // TODO: 6/21/21 delete this. used for api testing 
        marketDataService.run();
        marketDataService.runBatch();

        // TODO: 6/19/21 add user data to model
        
        return "home";
    }
}