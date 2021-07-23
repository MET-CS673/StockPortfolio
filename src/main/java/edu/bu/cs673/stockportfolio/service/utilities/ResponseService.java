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
        List<List<String>> response = createPortfolioTable(accounts);

        model.addAttribute("portfolio", response);

        return uploadSuccess(result, model);
    }

    /**
     * Creates a data structure capable of presenting Portfolio data in a table view
     * @param accounts A list of all accounts within a Portfolio
     * @return Portfolio data that will be presented to the user on the home endpoint
     */
    public List<List<String>> createPortfolioTable(List<Account> accounts) {
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

    private String doGetCompanyName(AccountLine accountLine) {
        return accountLine.getQuote().getCompanyName();
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

    public String deleteSuccess(boolean result, Model model) {
        List<List<String>> response = new ArrayList<>(List.of());
        model.addAttribute("portfolio", response);
        return uploadSuccess(result, model);
    }

    public Map<String, Float> aggregateSumBySymbol(List<Account> accounts) {
        Map<String, Float> data = new LinkedHashMap<String, Float>();

        String symbol;
        for (Account account : accounts) {

            List<AccountLine> accountLines = account.getAccountLines();
            for (AccountLine accountLine : accountLines) {
                symbol = accountLine.getQuote().getSymbol();
                calculateMarketCaps(data, symbol, accountLine);
            }
        }

        return data;
    }

    /**
     * Calculates the total proportion of each investment relative to the total portfolio
     * @param accounts A list of all accounts within a Portfolio
     * @param marketCapType A list of market caps and their associated bands as defined by Bloomberg
     * @return The proportion of each investment and the investments market cap type for each investment within a
     * Portfolio. The data will be presented to the user on the mc_breakdown endpoint
     */
    public Map<String, Float> aggregateSumBySymbol(List<Account> accounts, MarketCapType marketCapType) {
        Map<String, Float> data = new LinkedHashMap<String, Float>();

        String symbol;
        long marketCap;
        for (Account account : accounts) {

            List<AccountLine> accountLines = account.getAccountLines();
            for (AccountLine accountLine : accountLines) {
                symbol = accountLine.getQuote().getSymbol();

                // Don't add this quotes market cap if it's outside of the range for this MarketCapType
                marketCap = accountLine.getQuote().getMarketCap();
                if (marketCap < marketCapType.getMinimum() || marketCap >= marketCapType.getMaximum()) {
                    continue;
                }

                calculateMarketCaps(data, symbol, accountLine);
            }
        }

        return data;
    }

    private void calculateMarketCaps(Map<String, Float> data, String symbol, AccountLine accountLine) {
        float totalValue;

        // If we've already seen this symbol, add the total value from this account line to the existing total value.
        // Otherwise, the total value for this stock is simply the value from this account line
        if (data.containsKey(symbol)) {
            totalValue = multiplyBigDecimals(data, symbol, accountLine);
        } else {
            totalValue = multiplyBigDecimals(accountLine);
        }

        data.put(symbol, totalValue);
    }

    private float multiplyBigDecimals(Map<String, Float> data, String symbol, AccountLine accountLine) {
        float currentValue = data.get(symbol);
        return currentValue + multiplyBigDecimals(accountLine);
    }

    private float multiplyBigDecimals(AccountLine accountLine) {
        Quote quote = doGetQuote(accountLine);
        BigDecimal latestPrice = doGetPrice(quote);
        return latestPrice.multiply(BigDecimal.valueOf(accountLine.getQuantity())).floatValue();
    }

    public String uploadFailure(boolean result, Model model) {
        model.addAttribute("uploadFailed", result);
        model.addAttribute("applicationEdgeCaseErrorMessage", result);
        model.addAttribute("nav", "/home");

        return "result";
    }

    public String deletePortfolioError(boolean result, Model model) {
        model.addAttribute("deletePortfolioError", result);
        model.addAttribute("nav", "/home");

        return "result";
    }
}
