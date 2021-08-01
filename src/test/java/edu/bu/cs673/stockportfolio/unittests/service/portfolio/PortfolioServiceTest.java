package edu.bu.cs673.stockportfolio.unittests.service.portfolio;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import edu.bu.cs673.stockportfolio.domain.account.AccountLineRepository;
import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.portfolio.PortfolioRepository;
import edu.bu.cs673.stockportfolio.service.company.CompanyService;
import edu.bu.cs673.stockportfolio.service.portfolio.MarketDataServiceImpl;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioNotFoundException;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/*
 * https://rieckpil.de/difference-between-mock-and-mockbean-spring-boot-applications/
 */
@ExtendWith(MockitoExtension.class)
public class PortfolioServiceTest {

    @InjectMocks
    private PortfolioService portfolioService;

    @Mock
    private PortfolioRepository portfolioRepository;

    @Mock
    private MarketDataServiceImpl marketDataServiceImpl;

    @Mock
    private AccountLineRepository accountLineRepository;

    @Mock
    private CompanyService companyService;

    @Test
    public void getPortfolioWithInvalidIdShouldThrowException() {
        assertThrows(PortfolioNotFoundException.class, () -> portfolioService.getPortfolioBy(100L));
    }

    @Test
    public void getPortfolioWithValidIdShouldReturnPortfolio() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);

        when(portfolioRepository.findById(1L)).thenReturn(Optional.of(portfolio));

        Portfolio result = portfolioService.getPortfolioBy(1L);

        Assertions.assertEquals(portfolio, result);
    }

    @Test
    public void deletePortfolioWithValidIdAndQueryingDatabaseForItShouldThrowPortfolioNotFoundException() {
        Portfolio portfolio = new Portfolio();
        portfolio.setId(1L);

        when(portfolioRepository.save(portfolio)).thenReturn(portfolio);

        portfolioRepository.save(portfolio);

        portfolioService.deletePortfolioBy(portfolio.getId());

        assertThrows(PortfolioNotFoundException.class, () -> portfolioService.getPortfolioBy(portfolio.getId()));
    }
}
