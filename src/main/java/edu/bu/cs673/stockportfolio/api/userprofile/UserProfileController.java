package edu.bu.cs673.stockportfolio.api.userprofile;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.domain.user.UserRepository;
import edu.bu.cs673.stockportfolio.service.authentication.HashService;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller bean responsible for handling "profile" requests
 */
@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final UserService userService;
    private final ResponseService responseService;
    private final HashService hashService;
    private final FluentLogger log = FluentLoggerFactory.getLogger(PortfolioService.class);

    /**
     * Creates a UserProfileController. (Autowired by Spring).
     * Responsible for handling request(s) to show the profile page.
     * 
     * @param userService the UserService (provided by the Spring dependency injection).
     * @param responseService the ResponseService (provided by the Spring dependency injection).
     * @param hashService the HashService (provided by the Spring dependency injection).
     */
    public UserProfileController(UserService userService, ResponseService responseService, HashService hashService) {
        this.userService = userService;
        this.responseService = responseService;
        this.hashService = hashService;
    }

    /**
     * GET Request Handler for '/profile', shows the "user_profile.html" view.
     * 
     * @return the user_profile view ... (resources/templates/user_profile.html).
     */
    @GetMapping()
    public String getUserProfile() {
        return "user_profile";
    }

    /**
     * <h3>POST Request Handler for '/profile/delete'</h3>
     * 
     * <p>Attempts to delete a user's profile.<p>
     * 
     * <p>This method will take the authenticated user from the Spring Authentication result
     * and delete the user using the user service. After deleting the user, will clear the 
     * jsession cookie and show the "signup.html" view.</p>
     * 
     * <p>If the user does not exist, or the user service raises an exception, the handler will
     * show the {@link edu.bu.cs673.stockportfolio.service.utilities.ResponseService#deletePortfolioError(boolean, Model)
     * deletePortfolioError(...)} view.</p>
     * 
     * <p>An error will be logged if an exception is thrown.</p>
     * 
     * @param authentication the Spring authentication object - used to get the User principal.
     * @param response the http servlet response object.
     * @param model the Model object to provide data to template.
     * @return the view.
     * @see edu.bu.cs673.stockportfolio.service.utilities.ResponseService#deletePortfolioError(boolean, Model)
     * deletePortfolioError(...).
     */
    @PostMapping("/delete")
    public String deleteUserProfile(Authentication authentication, HttpServletResponse response, Model model) {
        User currentUser = getCurrentUser(authentication);

        if (currentUser == null) {
            return responseService.deletePortfolioError(true, model);
        }

        try {
            userService.delete(currentUser);

            if (currentUser.getId() != null) {
                Cookie cookie = new Cookie("JSESSIONID", "");
                cookie.setMaxAge(0);
                response.addCookie(cookie);

                return "signup";
            }
        } catch (Exception e) {
            log.error().log("Error deleting account for userId=" + currentUser.getId());
        }

        return responseService.deletePortfolioError(true, model);
    }

    /**
     * <h3>POST Request Handler for '/profile/modifyPwd'</h3>
     * 
     * <p>This method will take the authenticated user from Spring Authentication result and 
     * uses the UserService to get the current user. If there is a current user, it will
     * first verify the old password, and if verified, will try to change the password
     * to the new password. If this succeeds, then it will return an html response with
     * "true" as the response body. Otherwise it will return "false" in the response body.</p>
     * 
     * @param authentication the Spring authentication object - used to get the current user.
     * @param oldPassword the old password.
     * @param newPassword the new password.
     * @return the response body either "true" or "false".
     */
    @ResponseBody
    @PostMapping("/modifyPwd")
    public String modifyPassword(Authentication authentication,
                                 @RequestParam("oldPwd") String oldPassword,
                                 @RequestParam("newPwd") String newPassword) {

        User currentUser = getCurrentUser(authentication);

        boolean result = false;
        if (currentUser != null) {
            result = userService.verifyPassword(currentUser, oldPassword);
        }

        if (result) {
            result = userService.updatePassword(currentUser, newPassword);
        }

        if (result) {
            return "true";
        }

        return "false";  // The oldPassword does not match the password stored in our database
    }

    /*
     * Get the current User, if it exists, from the Authentication principal.
     * 
     * @param authentication The Spring authentication object - used to get the User principal
     * @return The current User object if found, null otherwise
     */
    private User getCurrentUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }
}
