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
 * Controller bean responsible for handling "portfolio" requests
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
     * Creates a PortfolioController. (Autowired by Spring).
     * Responsible for handling request to show portfolio.
     * 
     * @param userService the UserService (provided by Spring dependency injection).
     * @param portfolioService the PortfolioService (provided by Spring dependency injection).
     * @param validationService the ValidationService (provided by Spring dependency injection).
     * @param responseService the ResponseService (provided by Spring dependency injection).
     */
    public PortfolioController(UserService userService, PortfolioService portfolioService,
                               ValidationService validationService, ResponseService responseService) {
        this.userService = userService;
        this.portfolioService = portfolioService;
        this.validationService = validationService;
        this.responseService = responseService;
    }

    /**
     * <h3>POST Request Handler for '/portfolio'</h3>
     * 
     * <p>This method will try to upload a csv file containing the user's portfolio.</p>
     * 
     * <p>This method will take the authenticated user from the Spring Authentication result.
     * It will then take the uploaded file (from the request multipart data upload) and try to save
     * the file using the PortfolioService and the current user. Upon successful save, the
     * {@link edu.bu.cs673.stockportfolio.service.utilities.ResponseService#uploadSuccess(boolean, Model, User, PortfolioService)
     *  responseService.uploadSuccess(...)} view will be rendered.</p>
     * 
     * <p>If the uploaded file is empty, the 
     * {@link edu.bu.cs673.stockportfolio.service.utilities.ResponseService#uploadError(boolean, Model)
     * responseService.uploadError(...)} view will be rendered.</p>
     * 
     * <p>If the file name is invalid, an InvalidFileNameException is thrown and the error is
     * logged. The detailed error message is added to the model attribute "message", and the
     * {@link edu.bu.cs673.stockportfolio.service.utilities.ResponseService#uploadSuccess(boolean, Model, User, PortfolioService)
     * responseService.uploadSuccess(...)} view will be rendered.</p>
     * 
     * @param authentication the Spring authentication object - used to get the User principal.
     * @param multipartFile the multipart form data being uploaded, sourced from the request parameter 'csvUpload'.
     * @param model the model object to provide data to template.
     * @return the view to show, either responseService.uploadSuccess(...) or responseService.uploadError(...).
     * @see edu.bu.cs673.stockportfolio.service.utilities.ResponseService#uploadSuccess(boolean, Model, User, PortfolioService)
     *  responseService.uploadSuccess(...).
     * @see edu.bu.cs673.stockportfolio.service.utilities.ResponseService#uploadError(boolean, Model)
     * responseService.uploadError(...).
     */
    @PostMapping
    public String uploadPortfolio(Authentication authentication,
                                  @RequestParam("csvUpload")MultipartFile multipartFile, Model model) {
        User currentUser = getCurrentUser(authentication);

        if ( multipartFile == null) {
            return "home";
        }

        if (multipartFile.isEmpty()) {
            return responseService.uploadError(true, model);
        }

        boolean result;
        try {
            result = portfolioService.save(multipartFile, currentUser);
        } catch (InvalidFileNameException e) {
            log.error().log("Portfolio file name is invalid. Check for NUL character or malicious activity. "
                    + e.getMessage());

            return responseService.uploadError(true, model);
        }

        return responseService.uploadSuccess(result, model, currentUser, portfolioService);
    }

    /*
     * Get the current User, if it exists, from the Authentication principal.
     * 
     * @param authentication the Spring authentication object - used to get the User principal.
     * @return the User object if found, null otherwise.
     */
    private User getCurrentUser(Authentication authentication) {
        return userService.findUserByName(authentication.getName());
    }

    /**
     * <h3>POST Request Handler for '/delete'</h3>
     * 
     * <p>This method will take the authenticated user from Spring Authentication result
     * and gets the id of the current portfolio. If the user is authorized to make this request,
     * the portfolio associated with the given id is deleted by PortfolioService.</p>
     * 
     * <p>If the portfolio has successfully been deleted, the 
     * {@link edu.bu.cs673.stockportfolio.service.utilities.ResponseService#deleteSuccess(boolean, Model)
     * responseService.deleteSuccess(...)} view will be rendered.</p>
     * 
     * <p>If there is an error in deleting the portfolio, the existing portfolio will
     * be presented to the user and the 
     * {@link edu.bu.cs673.stockportfolio.service.utilities.ResponseService#deletePortfolioError(boolean, Model)
     * responseService.deletePortfolioError(...)} view will be rendered.</p>
     * 
     * @param authentication the Spring authentication object - used to get the User principal.
     * @param model the model object to provide data to template.
     * @return True if the portfolio has been deleted, else show existing portfolio.
     * @see edu.bu.cs673.stockportfolio.service.utilities.ResponseService#deleteSuccess(boolean, Model)
     * responseService.deleteSuccess(...).
     * @see edu.bu.cs673.stockportfolio.service.utilities.ResponseService#deletePortfolioError(boolean, Model)
     * responseService.deletePortfolioError(...).
     */
    @PostMapping("/delete")
    public String deletePortfolio(Authentication authentication, Model model) {
        User currentUser = getCurrentUser(authentication);
        Portfolio currentPortfolio = currentUser.getPortfolio();

        if (currentPortfolio == null) {
            return "home";
        }

        boolean result = validationService.validatePortfolioOwner(currentPortfolio, currentUser, model, "delete");

        Long id = currentPortfolio.getId();
        if (result) {
            portfolioService.deletePortfolioBy(id);
        }

        if (isPortfolioDeleted(id)) {
            return responseService.deleteSuccess(true, model);
        }

        // If control flow gets here, the existing portfolio will be presented to the user
        return responseService.deletePortfolioError(true, model);
    }

    
    /*
     * Checks the database to confirm the portfolios existence after the 
     * delete request has been committed.
     * 
     * This method will return true if the portfolio referenced by {@code id} is deleted
     * or not found.
     * 
     * @param id the id of the portfolio.
     * @param True if not found or deleted.
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
