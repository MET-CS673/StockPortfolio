package edu.bu.cs673.stockportfolio.service.utilities;

import edu.bu.cs673.stockportfolio.domain.user.User;
import edu.bu.cs673.stockportfolio.service.portfolio.PortfolioService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Locale;

/**********************************************************************************************************************
 * Package responses before returning them to the client.
 *********************************************************************************************************************/
@Service
public class ResponseService {
//    public String uploadSuccess(boolean result, Model model, User user, PortfolioService service) {
//        model.addAttribute(service.getClass().getSimpleName(), service.getPortfolio(user));
//
//        return uploadSuccess(result, model, service
//                .getClass()
//                .getSimpleName()
//                .toLowerCase(Locale.ROOT));
//    }

    private String uploadSuccess(boolean result, Model model, String serviceType) {
        model.addAttribute("success", result);
        model.addAttribute("nav", "/home#nav-" + serviceType);

        return "result";
    }

    public String uploadFailure(boolean result, Model model, PortfolioService service) {
        model.addAttribute("uploadFailed", result);
        model.addAttribute("applicationEdgeCaseErrorMessage", true);
        model.addAttribute("nav", "/home#nav-" + service
                .getClass()
                .getSimpleName()
                .toLowerCase(Locale.ROOT));

        return "result";
    }
}
