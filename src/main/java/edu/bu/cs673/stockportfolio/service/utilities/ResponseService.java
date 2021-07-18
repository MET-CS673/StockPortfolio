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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**********************************************************************************************************************
 * Package responses before returning them to the client.
 *********************************************************************************************************************/
@Service
public class ResponseService {
    public String uploadSuccess(boolean result, Model model, User user, PortfolioService service) {
        Long id = user.getPortfolio().getId();
        Portfolio portfolio = service.getPortfolioBy(id);
        List<Account> accounts = portfolio.getAccounts();
        List<List<String>> response = packageResponse(accounts);

        model.addAttribute("portfolio", response);

        return uploadSuccess(result, model);
    }

    public List<List<String>> packageResponse(List<Account> accounts) {
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

        return response;
    }

    public Map<String, Float> aggregateSumBySymbol(List<Account> accounts) {

        Map<String, Float> data = new LinkedHashMap<String, Float>();
        
        String symbol;
        Float totalValue;
        for (Account account : accounts) {

            List<AccountLine> accountLines = account.getAccountLines();
            for (AccountLine accountLine : accountLines) {

                symbol = accountLine.getQuote().getSymbol();

                // If we've already seen this symbol, add the total value
                // from this account line to the existing total value.
                // Otherwise, the total value for this stock is simply
                // the value from this account line
                if (data.containsKey(symbol)) {

                    // TODO - Refactor this equation into a separate function
                    totalValue = data.get(symbol) 
                    + accountLine.getQuote().getLatestPrice()
                    .multiply(BigDecimal.valueOf(accountLine.getQuantity())).floatValue();
                } else {

                    totalValue = accountLine.getQuote().getLatestPrice()
                    .multiply(BigDecimal.valueOf(accountLine.getQuantity())).floatValue();
                }

                data.put(symbol, totalValue);
            }
        }

        return data;
    }

    // Return a map of Symbols to their Aggregated total value across all accounts within
    // the input marketCapType
    public Map<String, Float> aggregateSumBySymbol(List<Account> accounts, MarketCapType marketCapType) {

        Map<String, Float> data = new LinkedHashMap<String, Float>();
        
        String symbol;
        long marketCap;
        Float totalValue;
        for (Account account : accounts) {

            List<AccountLine> accountLines = account.getAccountLines();
            for (AccountLine accountLine : accountLines) {

                symbol = accountLine.getQuote().getSymbol();
                
                // If this quotes market cap is outside of the range for this
                // MarketCapType, don't include it in the return data
                marketCap = accountLine.getQuote().getMarketCap();
                if ( marketCap < marketCapType.getMinimum() || marketCap >= marketCapType.getMaximum() ) {
                    continue;
                }

                // If we've already seen this symbol, add the total value
                // from this account line to the existing total value.
                // Otherwise, the total value for this stock is simply
                // the value from this account line
                if (data.containsKey(symbol)) {

                    // TODO - Refactor this equation into a separate function
                    totalValue = data.get(symbol) 
                    + accountLine.getQuote().getLatestPrice()
                    .multiply(BigDecimal.valueOf(accountLine.getQuantity())).floatValue();
                } else {

                    totalValue = accountLine.getQuote().getLatestPrice()
                    .multiply(BigDecimal.valueOf(accountLine.getQuantity())).floatValue();
                }

                data.put(symbol, totalValue);
            }
        }

        return data;
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

    private String uploadSuccess(boolean result, Model model) {
        model.addAttribute("success", result);
        model.addAttribute("nav", "/home");

        return "result";
    }

    public String uploadFailure(boolean result, Model model, PortfolioService service) {
        model.addAttribute("uploadFailed", result);
        model.addAttribute("applicationEdgeCaseErrorMessage", true);
        model.addAttribute("nav", "/home");

        return "result";
    }
}
