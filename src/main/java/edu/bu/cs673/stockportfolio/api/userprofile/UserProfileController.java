package edu.bu.cs673.stockportfolio.api.userprofile;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.domain.user.UserRepository;
import edu.bu.cs673.stockportfolio.service.authentication.HashService;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/profile")
public class UserProfileController {

    private final UserService userService;
    private final ResponseService responseService;
    private final HashService hashService;
    private final FluentLogger log = FluentLoggerFactory.getLogger(PortfolioService.class);

    public UserProfileController(UserService userService, ResponseService responseService , HashService hashService) {
        this.userService = userService;
        this.responseService = responseService;
        this.hashService = hashService;
    }

    @GetMapping()
    public String getUserProfile() {
        return "user_profile";
    }

    @PostMapping("/delete")
    public String deleteUserProfile(Authentication authentication, HttpServletResponse response, Model model) {
        User user = userService.findUserByName(authentication.getName());

        if (user == null) {
            return responseService.deletePortfolioError(true, model);
        }

        try {
            userService.delete(user);

            if (user.getId() != null) {
                Cookie cookie = new Cookie("JSESSIONID","");
                cookie.setMaxAge(0);
                response.addCookie(cookie);

                return "signup";
            }
        } catch (Exception e){
            log.error().log("Error deleting account for userId=" + user.getId());
        }

        return responseService.deletePortfolioError(true, model);
    }

    @PostMapping("/modifyPwd")
    public String modifyPassword(Authentication authentication,
                                 @RequestParam(name = "oldPwd", required = true) String oldPwd,
                                 @RequestParam(name = "newPwd", required = true) String newPwd) {
        User user = userService.findUserByName(authentication.getName());

        return "true";
    }
}
