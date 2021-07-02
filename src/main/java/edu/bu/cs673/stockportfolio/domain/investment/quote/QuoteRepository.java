package edu.bu.cs673.stockportfolio.domain.investment.quote;

import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
