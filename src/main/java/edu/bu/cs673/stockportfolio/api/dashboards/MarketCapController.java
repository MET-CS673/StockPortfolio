package edu.bu.cs673.stockportfolio.api.dashboards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mc_breakdown")
public class MarketCapController {

    @GetMapping
    public String marketCapBreakdownView() {
        return "mc_breakdown";
    }
}
