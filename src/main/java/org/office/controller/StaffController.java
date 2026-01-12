package org.office.controller;

import org.office.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/staff")
public class StaffController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        
        model.addAttribute("stats", statisticsService.viewStatistics());
        model.addAttribute("revenueStats", statisticsService.getRevenueStatistics());
        return "staff/staff-dashboard";
    }
}

