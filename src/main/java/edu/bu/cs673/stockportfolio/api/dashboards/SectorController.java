package edu.bu.cs673.stockportfolio.api.dashboards;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 
 * The controller for Sector Breakdown.
 */
@Controller
@RequestMapping("/sector_breakdown")
public class SectorController {

    /**
     * Provides a view of the sector breakdown
     * 
     * @return  sector breakdown of a user's portfolio
     */
    @GetMapping
    public String sectorBreakdownView() {
        return "sector_breakdown";
    }
}
