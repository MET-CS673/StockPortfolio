package edu.bu.cs673.stockportfolio.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The JPA Repository for Account Line data.
 */
@Repository
public interface AccountLineRepository extends JpaRepository<AccountLine, Long> {

    /**
     * Deletes all account lines for a given account id.
     * 
     * @param accountId
     */
    void deleteAllByAccount_Id(Long accountId);
}
