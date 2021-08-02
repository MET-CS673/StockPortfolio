package edu.bu.cs673.stockportfolio.api.portfolio;

import edu.bu.cs673.stockportfolio.domain.portfolio.Portfolio;
import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioNotFoundException;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import edu.bu.cs673.stockportfolio.service.user.UserService;
import edu.bu.cs673.stockportfolio.service.utilities.ResponseService;
import edu.bu.cs673.stockportfolio.service.utilities.ValidationService;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;
import org.fissore.slf4j.FluentLogger;
import org.fissore.slf4j.FluentLoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**********************************************************************************************************************
 * The FileUploadController handles client requests to insert and delete files.
 *
 * Controller maps to the url request "/portfolio"
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
    private final FluentLogger log = FluentLoggerFactory.getLogger(PortfolioController.class);

    /**
    *   Class constructor that initializes the dependencies to UserService,
    *   PortfolioService, ResponseService and ValidationService
    *
    *   @param userService The User request object
    *   @param portfolioService The Portfolio request object
    *   @param validationService Validation request object
    *   @param responseService Object to package responses
    */
    public PortfolioController(UserService userService, PortfolioService portfolioService,
                               ValidationService validationService, ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.validationService = validationService;
        this.responseService = responseService;
    }

    /**
    *   Maps HTTP POST request onto specifc handler method uploadPortfolio.
    *   Handles request to insert files
    *
    *   @param authentication Spring authentication object
    *   @param multipartFile The csv file being uploaded
    *   @param model Takes success or error responses and added it to model
    *   @return responseService packages the response if file upload is successful
    */
    @PostMapping
    public String uploadPortfolio(Authentication authentication,
                                  @RequestParam("csvUpload")MultipartFile multipartFile, Model model) {
        User currentUser = getCurrentUser(authentication);

        // Handle empty file upload attempts
        if (multipartFile.isEmpty()) {
            return responseService.uploadFailure(true, model);
        } else {
            boolean result;
            try {
                result = portfolioService.save(multipartFile, currentUser);
            } catch (InvalidFileNameException e) {
                log.error().log("Portfolio file name is invalid. Check for NUL character or malicious activity. "
                        + e.getMessage());
                result = false;
                model.addAttribute("message", e.getMessage());
            }

            return responseService.uploadSuccess(result, model, currentUser, portfolioService);
        }
    }

    /**
    *   Get the user's username
    *
    *   @param authentication Spring authentication object
    *   @return The user'a username
    */
    private User getCurrentUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }

    /**
    *   Maps HTTP POST request onto specifc handler method deletePortfolio.
    *   Handles request to delete files
    *
    *   @param authentication Spring authentication object
    *   @param model Takes success or error responses and added it to model
    *   @return Package message based on deletion success or error
    */
    @PostMapping("/delete")
    public String deletePortfolio(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);
        Portfolio currentPortfolio = currentUser.getPortfolio();
        Long id = currentPortfolio.getId();

        boolean result = validationService.validatePortfolioOwner(currentPortfolio, currentUser, model, "delete");

        if (result) {
            portfolioService.deletePortfolioBy(id);
        }

        if (isPortfolioDeleted(id)) {
            return responseService.deleteSuccess(true, model);
        }

        // If control flow gets here, the existing portfolio will be presented to the user
        return responseService.deletePortfolioError(true, model);
    }

    /**
    *   Checks the database to confirm the portfolios existence after the delete 
    *   request has been committed.
    *
    *   @param id The id of the portfolio
    *   @return Boolean to check if the portfolio has successfully been deleted
    *
    */
    private boolean isPortfolioDeleted(Long id) {
        try {
            Portfolio currentPortfolio = portfolioService.getPortfolioBy(id);
            return currentPortfolio == null;
        } catch (PortfolioNotFoundException e) {
            // Fail gracefully by logging error and return true because the portfolio can't be found
            log.error().log("Portfolio not found.");
            return true;
        }
    }
}
