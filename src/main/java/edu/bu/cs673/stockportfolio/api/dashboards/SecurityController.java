package edu.bu.cs673.stockportfolio.api.dashboards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock_breakdown")
public class SecurityController {

    @GetMapping
    public String securityBreakdownView() {
        return "stock_breakdown";
    }
}
