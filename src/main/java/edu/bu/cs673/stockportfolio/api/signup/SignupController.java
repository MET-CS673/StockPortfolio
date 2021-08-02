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
 * This controller handles signup requests. It depends on the UserService to check
 * if the username provided already exists. If not, delegates account creation 
 * to the UserService.
 *
 * Controller maps to the url request "/signup"
 *********************************************************************************************************************/
@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    /**
    *   Class constructor that initializes the dependency to UserService
    *
    *   @param userService The User request object
    */
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    /**
    *   When HTTP GET request is recieved for /signup, signupView()
    *   is called to handle the request
    *
    *   @return "signup" when /signup is requested
    */
    @GetMapping()
    public String signupView() {
        return "signup";
    }

    /**
    *   Maps HTTP POST request onto specifc handler method signup.
    *   Checks to see if the username provided already exists and redirects to login
    *   upon successful signup
    *
    *   @param user Creates a new address object for user and puts it into model
    *   @param model Takes the result of signing up and adds it to model.
    *   @param redirectAttributes Stores flash attributes on successful signup
    *   @return "signup" when /signup is requested
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
