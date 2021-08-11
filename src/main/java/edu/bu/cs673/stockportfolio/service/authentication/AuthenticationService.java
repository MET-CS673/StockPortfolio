package edu.bu.cs673.stockportfolio.service.authentication;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.domain.user.UserRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**********************************************************************************************************************
 * Implements the Spring interface AuthenticationProvider. This allows the service to integrate our
 * provider with different authentication schemes.
 *********************************************************************************************************************/
@Service
public class AuthenticationService implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final HashService hashService;

    /**
     * Creates an AuthenticationService
     * 
     * @param userRepository the UserRepository (provided by Spring dependency injection).
     * @param hashService the HashService (provided by Spring dependency injection).
     */
    public AuthenticationService(UserRepository userRepository, HashService hashService) {
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    /**
     * <h3>Attempts to authenticate a user using a given username and password</h3>
     * 
     * <p>The method first tries to find a user object for a given username,
     * using the {@code userRepository}. If found, it compares the user's stored hashed password
     * value with the newly hashed value from the user input to see if the two
     * match. If successful, it will return the authenticated token.</p>
     * 
     * <p>Return the value {@code null} otherwise.</p>
     *
     * @param authentication a Spring Authentication object, containing a username and password.
     * @return an authenticated authentication token if successful, {@code null} otherwise.
     * @throws AuthenticationException Authentication error object.
     * @see org.springframework.security.authentication.AuthenticationProvider#authenticate(Authentication) authenticate(...)
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        User user = userRepository.findAllByUsername(username);
        if (user != null) {
            String encodedSalt = user.getSalt();
            String hashedPassword = hashService.getHashedValue(password, encodedSalt);
            if (user.getPassword().equals(hashedPassword)) {
                return new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>());
            }
        }

        return null;
    }

    /**
     * 
     * @param authentication a Spring Authentication class to test.
     * @return {@code true} if the {@code authentication} class is a {@code UsernamePasswordAuthenticationToken}, false otherise.
     * @see org.springframework.security.authentication.AuthenticationProvider#supports(Class) supports(...)
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
