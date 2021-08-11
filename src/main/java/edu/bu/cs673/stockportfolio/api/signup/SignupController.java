package edu.bu.cs673.stockportfolio.api.signup;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**********************************************************************************************************************
 * This controller handles signup requests. It depends on the UserService to check if the username 
 * provided already exists. If not, delegates account creation to the UserService.
 *
 * Controller bean responsible for handling "signup" requests
 *********************************************************************************************************************/
@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    /**
     * Creates a SignupController. (Autowired by Spring).
     * Responsible for handling request(s) to show the signup page.
     * 
     * @param userService the UserService (provided by Spring dependency injection).
     */
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    /**
     * GET Request Handler for '/signup', shows the "signup.html" view.
     * 
     * @return the signup view ... (resources/templates/signup.html).
     */
    @GetMapping()
    public String signupView() {
        return "signup";
    }

    /**
     * <h3>POST Request Handler for '/signup'</h3>
     * 
     * <p>This method attempts to signup a new user.</p>
     * 
     * <p>This method first checks to make sure that the username provided does not already
     * exist using the {@code UserService}. If this is a new username, it then attempts to create a
     * new user. If successful, a model attribute named "signupSuccess", with the value {@code true}, 
     * is added to the model; additionally, a flash attribute named "message", with the value 
     * "Account created!", is added to the {@code redirectAttributes} before the page is redirected to
     * '/login'.</p>
     * 
     * <p>If there is an error in signing up, an attribute with the name "signupError", and the value
     * equal to the error message, is added to the {@code model}, and then the view '/signup.html'
     * is returned to be rendered.</p>
     * 
     * @param user the User object - from the form post data (should contain new username...).
     * @param model the Model object to provide data to template.
     * @param redirectAttributes the redirect attributes - used for adding Flash attributes.
     * @return either the name of the view to be rendered ('signup.html') or a redirect string (to login url - '/login').
     */
    @PostMapping
    public String signup(@ModelAttribute User user, Model model, RedirectAttributes redirectAttributes) {
        String signupError = null;

        if (!userService.isUserNameAvailable(user.getUsername())) {
            signupError = "This username already exists.";
        }

        if (signupError == null) {
            User savedUser = userService.save(user);
            if (savedUser.getId() < 0) {
                signupError = "There was an error signing up. Please try again.";
            }
        }

        if (signupError == null) {
            model.addAttribute("signupSuccess", true);
            redirectAttributes.addFlashAttribute("message", "Account created!");
            return "redirect:/login";
        }

        model.addAttribute("signupError", signupError);
        return "signup";
    }
}
