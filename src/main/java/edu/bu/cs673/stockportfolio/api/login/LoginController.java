package edu.bu.cs673.stockportfolio.api.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**********************************************************************************************************************
 * Spring automatically leverages the AuthenticationService class to verify a users identity.
 *********************************************************************************************************************/
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * Allows a user to login if verified
     * 
     * @return login if the user has been verified
     */
    @GetMapping
    public String loginView() {
        return "login";
    }
}
