package edu.bu.cs673.stockportfolio.service.utilities;

import edu.bu.cs673.stockportfolio.domain.account.Account;
import edu.bu.cs673.stockportfolio.domain.account.AccountLine;
import edu.bu.cs673.stockportfolio.domain.investment.quote.Quote;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**********************************************************************************************************************
 * Package responses before returning them to the client.
 *********************************************************************************************************************/
@Service
public class ResponseService {
    public String uploadSuccess(boolean result, Model model, User user, PortfolioService service) {
        Long id = user.getPortfolio().getId();
        Portfolio portfolio = service.getPortfolioBy(id);
        List<Account> accounts = portfolio.getAccounts();

        List<List<String>> response = new ArrayList<>();

        accounts.forEach(account -> {
            account.getAccountLines().forEach(accountLine -> {
                response.add(List.of(
                        doGetCompanyName(accountLine),
                        doGetSymbol(doGetQuote(accountLine)),
                        formatQuantity(doGetQuantity(accountLine)),
                        formatPrice(doGetPrice(doGetQuote(accountLine))),
                        doGetMarketValue(accountLine))
                );
            });
        });

        model.addAttribute(service.getClass().getSimpleName(), response);

        return uploadSuccess(result, model, service.getClass().getSimpleName().toLowerCase(Locale.ROOT));
    }

    private String doGetCompanyName(AccountLine accountLine) {
        return accountLine.getQuote().getCompanyName();
    }

    private Account doGetAccount(AccountLine accountLine) {
        return accountLine.getAccount();
    }

    private String doGetAccountNumber(Account account) {
        return account.getAccountNumber();
    }

    private Quote doGetQuote(AccountLine accountLine) {
        return accountLine.getQuote();
    }

    private String doGetSymbol(Quote quote) {
        return quote.getSymbol();
    }

    private BigDecimal doGetPrice(Quote quote) {
        return quote.getLatestPrice();
    }

    private String formatPrice(BigDecimal price) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.US);
        return currencyFormatter.format(price);
    }

    private String doGetMarketValue(AccountLine accountLine) {
        BigDecimal latestPrice = doGetPrice(accountLine.getQuote());
        int quantity = doGetQuantity(accountLine);

        return formatPrice(latestPrice
                .multiply(BigDecimal
                        .valueOf(quantity)));
    }

    private int doGetQuantity(AccountLine accountLine) {
        return accountLine.getQuantity();
    }

    private String formatQuantity(int quantity) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(Locale.US);
        return numberFormatter.format(quantity);
    }

    private String uploadSuccess(boolean result, Model model, String serviceType) {
        model.addAttribute("success", result);
        model.addAttribute("nav", "/home");

        return "result";
    }

    public String uploadFailure(boolean result, Model model, PortfolioService service) {
        model.addAttribute("uploadFailed", result);
        model.addAttribute("applicationEdgeCaseErrorMessage", true);
        model.addAttribute("nav", "/home#nav-" + service
                .getClass()
                .getSimpleName()
                .toLowerCase(Locale.ROOT));

        return "result";
    }
}
