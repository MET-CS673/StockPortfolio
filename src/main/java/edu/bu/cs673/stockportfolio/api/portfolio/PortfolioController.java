package edu.bu.cs673.stockportfolio.api.portfolio;

import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;
import edu.bu.cs673.stockportfolio.service.utilities.ValidationService;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**********************************************************************************************************************
 * The FileUploadController handles client requests to insert and delete files.
 *
 * @note Max file size of 10MB.
 *********************************************************************************************************************/
@Controller
@RequestMapping("/portfolio")
public class PortfolioController {
    private final UserService userService;
    private final PortfolioService portfolioService;
    private final ValidationService validationService;
    private final ResponseService responseService;

    public PortfolioController(UserService userService, PortfolioService portfolioService,
                               ValidationService validationService, ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.validationService = validationService;
        this.responseService = responseService;
    }

    @PostMapping
    public String uploadPortfolio(Authentication authentication,
                                  @RequestParam("csvUpload")MultipartFile multipartFile, Model model) {
        User currentUser = getCurrentUser(authentication);

        // Handle empty file upload attempts
        if (multipartFile.isEmpty()) {
            return responseService.uploadFailure(true, model, portfolioService);
        } else {
            boolean result = false;
            try {
                result = portfolioService.save(multipartFile, currentUser);
            } catch (InvalidFileNameException e) {
                result = false;
                model.addAttribute("message", e.getMessage());
            }

            // TODO: 7/10/21 package response for all account lines 
            return "test";
            //return responseService.uploadSuccess(result, model, currentUser, portfolioService);
        }
    }

//    @GetMapping("/delete")
//    public String deletePortfolio(Authentication authentication, @ModelAttribute Portfolio portfolio, Model model) {
//        portfolio = portfolioService.findPortfolio(portfolio);
//        User currentUser = getCurrentUser(authentication);
//
//        boolean result = validationService.validatePortfolioOwner(portfolio, currentUser, model, "delete");
//
//        if (result) {
//            result = portfolioService.delete(portfolio);
//        }
//
//        return responseService.uploadSuccess(result, model, currentUser, portfolioService);
//    }

    private User getCurrentUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }
}
