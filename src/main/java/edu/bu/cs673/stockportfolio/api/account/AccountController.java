package edu.bu.cs673.stockportfolio.api.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
* Handles the request to view "/account"
* Controller maps to the url request "/account"
* 
*/
@Controller
@RequestMapping("/account")
public class AccountController {

    /**
    *   When HTTP GET request is received for /account, accountView()
    *   is called to handle the request
    *
    *   @return "account" when /account is requested
    */
    @GetMapping
    public String accountView() {
        return "account";
    }
}