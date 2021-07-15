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

    public HomeController(UserService userService, PortfolioService portfolioService,
                          ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.responseService = responseService;
    }

    @GetMapping
    public String getHomePage(Authentication authentication, Model model) {
        User user = getUser(authentication);
        Portfolio portfolio = portfolioService.getPortfolioBy(user.getPortfolio().getId());

        model.addAttribute("user", user);
        if (portfolio.getId() != null) {
            List<Account> accounts = portfolio.getAccounts();
            model.addAttribute("portfolio", responseService.packageResponse(accounts));
        } else {
            model.addAttribute("portfolio", new ArrayList<>());
        }
        
        return "home";
    }

    private User getUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }
}