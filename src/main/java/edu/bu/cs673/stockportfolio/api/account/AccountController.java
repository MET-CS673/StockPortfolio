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
     * Allows a user to view their account
     * 
     * @return  a user's account
     */
    @GetMapping
    public String accountView() {
        return "account";
    }
}