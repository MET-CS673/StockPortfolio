package edu.bu.cs673.stockportfolio.domain.account;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The JPA Repository for Account data.
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
