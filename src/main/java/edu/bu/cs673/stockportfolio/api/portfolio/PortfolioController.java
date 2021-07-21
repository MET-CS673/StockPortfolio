package edu.bu.cs673.stockportfolio.api.portfolio;

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

    /**
     * Class contructor that handles the user's request to insert or delete files
     * 
     * @param userService           ensures that the correct authenticated user is making the request
     * @param portfolioService      the portfolio imported by the user
     * @param validationService     ensures that the user is authorized to make the request
     * @param responseService       packages the responses from portfolioService
     */
    public PortfolioController(UserService userService, PortfolioService portfolioService,
                               ValidationService validationService, ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.validationService = validationService;
        this.responseService = responseService;
    }

    /**
     * Allows authorized user to upload the file
     * 
     * @param authentication    checks if the user is authenticated
     * @param multipartFile     the file uploaded by the user
     * @param model             contains the data from portfolioService and responseService
     * @return                  uploads file if portfolio is not empty. Raises exception for empty files
     */
    @PostMapping
    public String uploadPortfolio(Authentication authentication,
                                  @RequestParam("csvUpload")MultipartFile multipartFile, Model model) {
        User currentUser = getCurrentUser(authentication);

        // Handle empty file upload attempts
        if (multipartFile.isEmpty()) {
            return responseService.uploadFailure(true, model);
        } else {
            boolean result = false;
            try {
                result = portfolioService.save(multipartFile, currentUser);
            } catch (InvalidFileNameException e) {
                result = false;
                model.addAttribute("message", e.getMessage());
            }

            return responseService.uploadSuccess(result, model, currentUser, portfolioService);
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

    /**
     * Checks if the correct user is authenticated and has access to their individual account
     * 
     * @param authentication    checks the credentials of the user
     * @return                  True if the authenticated user is registered
     */
    private User getCurrentUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }
}
