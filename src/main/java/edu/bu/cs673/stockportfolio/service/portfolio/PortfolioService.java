package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.portfolio.PortfolioRepository;
import edu.bu.cs673.stockportfolio.domain.user.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**********************************************************************************************************************
 * Implements business logic for Portfolio requests.
 *
 *********************************************************************************************************************/
@Service
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MarketDataService marketDataService;

    public PortfolioService(PortfolioRepository portfolioRepository, MarketDataService marketDataService) {
        this.portfolioRepository = portfolioRepository;
        this.marketDataService = marketDataService;
    }

    public boolean save(Portfolio portfolio) {
        Portfolio savedPortfolio = portfolioRepository.save(portfolio);

        return savedPortfolio.getId() > 0;
    }

    public Portfolio findPortfolio(Portfolio portfolio) {
        return portfolioRepository.findById(portfolio.getId()).orElseThrow(PortfolioNotFoundException::new);
    }

    public boolean delete(Portfolio portfolio) {
        portfolioRepository.delete(portfolio);

        return portfolioRepository.findById(portfolio.getId()).isEmpty();
    }

    public void test() {
        marketDataService.run();
    }
}