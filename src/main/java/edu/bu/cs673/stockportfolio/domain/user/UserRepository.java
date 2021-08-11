package edu.bu.cs673.stockportfolio.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The JPA Repository for User data.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds all users by a given username.
     * 
     * @param username the username.
     * @return the user.
     */
    User findAllByUsername(String username);
}
