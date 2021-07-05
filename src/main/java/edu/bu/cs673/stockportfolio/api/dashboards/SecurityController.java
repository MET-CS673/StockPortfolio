package edu.bu.cs673.stockportfolio.api.dashboards;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/stock_breakdown")
public class SecurityController {

    @GetMapping
    public String securityBreakdownView(Model model) {

        Map<String, Integer> data = new LinkedHashMap<String, Integer>();
        data.put("FB", 500);
        data.put("AAPL", 1000);
        data.put("AMZN", 2000);
        data.put("NFLX", 500);
        data.put("GOOG", 5000);

        model.addAttribute("data", data);
        return "stock_breakdown";
    }
}
