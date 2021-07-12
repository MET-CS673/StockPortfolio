package edu.bu.cs673.stockportfolio.api.dashboards;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mc_breakdown")
public class MarketCapController {

    @GetMapping
    public String marketCapBreakdownView(Model model) {

        Map<String, Integer> largeCapData = new LinkedHashMap<String, Integer>();
        largeCapData.put("FB", 500);
        largeCapData.put("AAPL", 1000);
        largeCapData.put("AMZN", 2000);
        largeCapData.put("NFLX", 500);
        largeCapData.put("GOOG", 5000);
        model.addAttribute("largeCapData", largeCapData);

        Map<String, Integer> midCapData = new LinkedHashMap<String, Integer>();
        midCapData.put("CLOV", 500);
        midCapData.put("AMBA", 500);
        midCapData.put("SFIX", 2000);
        model.addAttribute("midCapData", midCapData);

        Map<String, Integer> smallCapData = new LinkedHashMap<String, Integer>();
        smallCapData.put("GME", 800);
        smallCapData.put("SAVA", 1200);
        smallCapData.put("PACB", 200);
        smallCapData.put("BLDR", 500);
        model.addAttribute("smallCapData", smallCapData);

        return "mc_breakdown";
    }
}
