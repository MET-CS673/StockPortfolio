package edu.bu.cs673.stockportfolio.api.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**********************************************************************************************************************
 * Spring automatically leverages the AuthenticationService class to verify a users identity.
 * 
 * Controller bean responsible for handling "login" requests
 *********************************************************************************************************************/
@Controller
@RequestMapping("/login")
public class LoginController {

    /**
     * GET Request Handler for '/login', shows the "login.html" view.
     * 
     * @return The login view ... (resources/templates/login.html)
     */
    @GetMapping
    public String loginView() {
        return "login";
    }
}
