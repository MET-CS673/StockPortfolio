package edu.bu.cs673.stockportfolio.api.dashboards;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import java.math.BigDecimal;

import org.apache.catalina.connector.Response;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.bu.cs673.stockportfolio.domain.account.Account;
import edu.bu.cs673.stockportfolio.domain.account.AccountLine;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;

@Controller
@RequestMapping("/stock_breakdown")
public class SecurityController {
    private final PortfolioService portfolioService;
    private final ResponseService responseService;
    private final UserService userService;

    public SecurityController(PortfolioService portfolioService, ResponseService responseService, UserService userService) {

        this.portfolioService = portfolioService;
        this.responseService = responseService;
        this.userService = userService;
    }

    @GetMapping
    public String securityBreakdownView(Authentication authentication, Model model) {

        User user = getUser(authentication);
        Portfolio portfolio = null;
        Map<String, Float> data = new LinkedHashMap<String, Float>();

        if (user.getPortfolio() != null) {

            portfolio = portfolioService.getPortfolioBy(user.getPortfolio().getId());
            List<Account> accounts = portfolio.getAccounts();

            data = responseService.aggregateSumBySymbol(accounts);
        }

        model.addAttribute("data", data);
        return "stock_breakdown";
    }

    private User getUser(Authentication authentication) {

        return userService.findUserByName(authentication.getName());
    }
}