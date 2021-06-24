package edu.bu.cs673.stockportfolio.service.portfolio;

import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.portfolio.PortfolioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**********************************************************************************************************************
 * Implements business logic for Portfolio requests.
 *
 *********************************************************************************************************************/
@Service
@Transactional
public class PortfolioService {

    private final PortfolioRepository portfolioRepository;
    private final MarketDataServiceImpl marketDataServiceImpl;

    public PortfolioService(PortfolioRepository portfolioRepository, MarketDataServiceImpl marketDataServiceImpl) {
        this.portfolioRepository = portfolioRepository;
        this.marketDataServiceImpl = marketDataServiceImpl;
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
}