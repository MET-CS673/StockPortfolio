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
 *********************************************************************************************************************/
@Controller
@RequestMapping("/signup")
public class SignupController {

    private final UserService userService;

    /**
     * Class contructor that handles requests for signing up
     * 
     * @param userService       creates an account if the username does not exist
     */
    public SignupController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Allows a user to signup
     * 
     * @return      handles request by user to create an account
     */
    @GetMapping()
    public String signupView() {
        return "signup";
    }

    /**
     * Checks to see whether username already exists.
     * 
     * Creates an account if username does not exist and valid information has been entered.
     * 
     * @param user                      Refers to the user  
     * @param model                     Contains information regarding successful or unsuccesful signup
     * @param redirectAttributes        Lets user know if an account has been created and redirects to login 
     *                                  if successful
     * @return                          Redirects to login if succesful and raises an error if unsuccessful
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
