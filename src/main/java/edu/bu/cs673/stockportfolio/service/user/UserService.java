package edu.bu.cs673.stockportfolio.service.user;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.domain.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

/**********************************************************************************************************************
 * Implements business logic for User requests.
 *
 *********************************************************************************************************************/
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final HashService hashService;

    public UserService(UserRepository userRepository, HashService hashService) {
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    /**
     * Check if the provided username already exists otherwise register the user. If the username is already
     * registered, send a response indicating a registration error.
     *
     * @param username A username sent by the client.
     * @return True if the username is not in the database. Otherwise, false.
     */
    public boolean isUserNameAvailable(String username) {
        return userRepository.findAllByUsername(username) == null;
    }

    /**
     * Either creates or updates a user, based on prior existence of the user.
     *
     * @param user A user object, which can be either new or existing.
     * @return The new or updated user.
     */
    public User save(User user) {
        if (user.getId() != null) {
            return userRepository.findById(user.getId())
                    .map(userToBeUpdated -> {
                        userToBeUpdated.setEmail(user.getEmail());
                        userToBeUpdated.setUsername(user.getUsername());
                        // TODO: 6/16/21 update password securely
                        userToBeUpdated.setPortfolio(user.getPortfolio());
                        return userToBeUpdated;
                    }).orElseThrow(UserNotFoundException::new);
        }

        // Create a new user while hashing the provided password.
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);
        return userRepository.save(
                new User(user.getId(), user.getUsername(), hashedPassword,
                        user.getSalt(), user.getEmail(), user.getPortfolio()));
    }

    public User findUserById(Long id) {
        return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
