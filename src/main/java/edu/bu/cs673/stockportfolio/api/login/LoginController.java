package edu.bu.cs673.stockportfolio.api.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**********************************************************************************************************************
 * Spring automatically leverages the AuthenticationService class to verify a users
 * identity.
 *
 *Controller maps to the url request "/login"
 *********************************************************************************************************************/
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
    *   When HTTP GET request is received for /login, loginView()
    *   is called to handle the request
    *
    *   @return "login" when /login is requested
    */
    @GetMapping
    public String loginView() {
        return "login";
    }
}
