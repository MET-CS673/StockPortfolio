package edu.bu.cs673.stockportfolio.domain.investment.sector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The JPA Repository for Company data.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
}