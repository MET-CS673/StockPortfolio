package edu.bu.cs673.stockportfolio.api.dashboards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sector_breakdown")
public class SectorController {

    @GetMapping
    public String sectorBreakdownView() {
        return "sector_breakdown";
    }
}
