package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.account.Account;
import edu.bu.cs673.stockportfolio.domain.investment.quote.Quote;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.portfolio.PortfolioRepository;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.authentication.HashService;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**********************************************************************************************************************
 * Implements business logic for Portfolio requests.
 *********************************************************************************************************************/
@Service
@Transactional
public class PortfolioService {
    private static final String[] HEADERS = {"Account", "Symbol", "Quantity"};
    private final PortfolioRepository portfolioRepository;
    private final MarketDataServiceImpl marketDataServiceImpl;
    private final FluentLogger log = FluentLoggerFactory.getLogger(HashService.class);

    public PortfolioService(PortfolioRepository portfolioRepository, MarketDataServiceImpl marketDataServiceImpl) {
        this.portfolioRepository = portfolioRepository;
        this.marketDataServiceImpl = marketDataServiceImpl;
    }

    public boolean save(MultipartFile multipartFile, User currentUser) {
        Portfolio savedPortfolio = null;

        try {
            BufferedReader fileReader = createBufferedReader(multipartFile);
            Iterable<CSVRecord> records = createCSVRecords(fileReader);

            Map<String, List<String>> csvRecords = doInternalParse(records);

            // Package all symbols in the portfolio and send a batch request to IEX Cloud to reduce network traffic
            List<String> allSymbols = new ArrayList<>();
            csvRecords.forEach((account, symbols) -> {
                allSymbols.add(String.join(",", symbols));  //
            });

            // Send market data batch request to IEX Cloud
            List<Quote> quotes = marketDataServiceImpl.doGetQuotes(allSymbols);

            // Create the portfolio and flush the transaction to generate a portfolio id
            Portfolio portfolio = createPortfolio(currentUser);
            savedPortfolio = portfolioRepository.save(portfolio);

            List<Account> accounts = createAccounts(savedPortfolio, csvRecords, quotes);

            // Maintain referential integrity between a portfolio and accounts
            savedPortfolio.setAccounts(accounts);
        } catch (IOException e) {
            log.error().log("Portfolio persistence error for User=" + currentUser, e.getMessage());
        }

        return savedPortfolio != null && savedPortfolio.getId() > 0;
    }

    private BufferedReader createBufferedReader(MultipartFile multipartFile) throws IOException {
        return new BufferedReader(new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8));
    }

    private Iterable<CSVRecord> createCSVRecords(BufferedReader fileReader) throws IOException {
        return CSVFormat.DEFAULT
                .withHeader(HEADERS)
                .withFirstRecordAsHeader()
                .parse(fileReader);
    }

    // Read portfolio data from a CSV file and store the account number as a key and the symbols as a modifiable list
    private Map<String, List<String>> doInternalParse(Iterable<CSVRecord> records) {
        Map<String, List<String>> accountLines = new HashMap<>();
        for (CSVRecord record : records) {
            // Extract the records from the csv file by using the column headers
            String account = record.get(HEADERS[0]);
            String symbol = record.get(HEADERS[1]);

            if (accountLines.containsKey(account)) {
                accountLines.get(account).add(symbol);
            } else {
                accountLines.put(account, new ArrayList<>(List.of(symbol)));
            }
        }

        return accountLines;
    }

    // Add a quote to an account only when a symbol has been purchased within the specified account number
    private List<Account> createAccounts(Portfolio portfolio,
                                         Map<String, List<String>> csvRecords, List<Quote> allQuotes) {
        List<String> createdAccounts = new ArrayList<>();
        List<Account> accounts = new ArrayList<>();

        // Update or create an account with quotes provided by IEX Cloud
        csvRecords.forEach((accountNumber, symbols) -> {
            if (createdAccounts.contains(accountNumber)) {
                Optional<Account> account = doAccountFilter(accounts, accountNumber);
                account.ifPresent(accountToBeUpdated -> doSymbolFilter(symbols, allQuotes, accountToBeUpdated));
            } else {
                Account account = doCreateAccount(symbols, allQuotes, portfolio, accountNumber);
                createdAccounts.add(account.getAccountNumber());
                accounts.add(account);
            }
        });

        return accounts;
    }

    // Find the current account within the portfolio
    private Optional<Account> doAccountFilter(List<Account> accounts, String accountNumber) {
        return accounts
                .stream()
                .filter(account -> account.getAccountNumber().equals(accountNumber))
                .findFirst();
    }

    // Find all symbols purchased within an account and then add the quote provided by IEX Cloud
    private void doSymbolFilter(List<String> symbols, List<Quote> allQuotes, Account accountToBeUpdated) {
        symbols.forEach(symbol ->                              // An account can contain multiple symbols
                allQuotes.forEach(quote -> {                   // A batch response of all symbols across all accounts
                    if (quote.getSymbol().contains(symbol)) {  // Match quotes to accounts that own them
                        accountToBeUpdated.getQuotes().add(quote);
                    }
                })
        );
    }

    // Instantiate an account and add quotes for all the symbols owned by the account
    private Account doCreateAccount(List<String> symbols, List<Quote> allQuotes,
                                    Portfolio portfolio, String accountNumber) {
        Account account = new Account(portfolio, accountNumber);
        List<Quote> accountSpecificQuotes = new ArrayList<>();
        symbols.forEach(symbol ->
                allQuotes.forEach(quote -> {
                    if (quote.getSymbol().contains(symbol)) {
                        accountSpecificQuotes.add(quote);
                    }
                })
        );

        account.setQuotes(accountSpecificQuotes);
        return account;
    }

    private Portfolio createPortfolio(User user) {
        return new Portfolio(user);
    }

    public Portfolio findPortfolio(Portfolio portfolio) {
        return portfolioRepository.findById(portfolio.getId()).orElseThrow(PortfolioNotFoundException::new);
    }

    public boolean delete(Portfolio portfolio) {
        portfolioRepository.delete(portfolio);

        return portfolioRepository.findById(portfolio.getId()).isEmpty();
    }
}