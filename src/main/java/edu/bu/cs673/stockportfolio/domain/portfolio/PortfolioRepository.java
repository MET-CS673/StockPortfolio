package edu.bu.cs673.stockportfolio.domain.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The JPA Repository for Portfolio data.
 */
@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
