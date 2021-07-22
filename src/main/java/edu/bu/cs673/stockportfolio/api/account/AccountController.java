package edu.bu.cs673.stockportfolio.api.account;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 
 * The controller for a user's account
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    /**
     * Handles the request to view the user's account
     * 
     * @return  a user's account
     */
    @GetMapping
    public String accountView() {
        return "account";
    }
}