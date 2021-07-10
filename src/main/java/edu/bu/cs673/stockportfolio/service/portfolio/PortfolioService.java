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
 *
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

            /*
            Package all symbols across the entire portfolio and send one
            batch request to IEX cloud to reduce network network traffic
             */
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

    private Map<String, List<String>> doInternalParse(Iterable<CSVRecord> records) {
        Map<String, List<String>> accountLines = new HashMap<>();

        // The map and arrayList link symbols to the accounts that own them
        for (CSVRecord record : records) {

            // Extract the records from the csv file by searching for static headers
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

        csvRecords.forEach((accountNumber, symbols) -> {
            if (createdAccounts.contains(accountNumber)) {     // Append quotes if the account already exists
                accounts.forEach(account -> {                  // Get the correct account to append quotes to
                    if (account.getAccountNumber().equals(accountNumber)) {
                        symbols.forEach(symbol ->              // An account can contain multiple symbols
                                allQuotes.forEach(quote -> {   // A batch response of all symbols across all accounts
                                    if (quote.getSymbol().contains(symbol)) {  // Match quotes to accounts that own them
                                        account.getQuotes().add(quote);
                                    }
                                })
                        );
                    }
                });
            } else {                                   // Create the account if it doesn't exist
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
                accounts.add(account);
                createdAccounts.add(accountNumber);
            }
        });

        return accounts;
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