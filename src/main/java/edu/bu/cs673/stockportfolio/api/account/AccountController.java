package edu.bu.cs673.stockportfolio.api.account;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.domain.user.UserRepository;
import edu.bu.cs673.stockportfolio.service.authentication.HashService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;
import java.util.Base64;

@Controller
public class AccountController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final HashService hashService;
    public AccountController(UserService userService,UserRepository userRepository,HashService hashService) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.hashService = hashService;
    }

    @GetMapping("/account")
    public String accountView() {
        return "account";
    }

    @ResponseBody
    @PostMapping("/deactivating")
    public String DeactivatingAccount(Authentication authentication, HttpServletResponse response) {
        User user = userService.findUserByName(authentication.getName());
        if (user== null) return "false";
        try {
            userRepository.delete(user);
            Cookie cookie =new Cookie("JSESSIONID","");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }catch (Exception e){
            return "false";
        }
        return "true";
    }

    @ResponseBody
    @PostMapping("/modifyPwd")
    public String modifyPassword(Authentication authentication,
                                 @RequestParam(name = "oldPwd", required = true) String oldPwd,
                                 @RequestParam(name = "newPwd", required = true) String newPwd) {
        User user = userService.findUserByName(authentication.getName());
        System.out.println(user.getSalt());
        String hashedPassword = hashService.getHashedValue(oldPwd, user.getSalt());
        if (hashedPassword.equals(user.getPassword())){
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            hashedPassword = hashService.getHashedValue(newPwd, encodedSalt);
            user.setPassword(hashedPassword);
            user.setSalt(encodedSalt);
            userRepository.save(user);
            return "true";
        }else {
            return "false";
        }

    }


}
