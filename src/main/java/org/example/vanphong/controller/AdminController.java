package org.example.vanphong.controller;

import org.example.vanphong.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        
        model.addAttribute("revenueStats", statisticsService.getRevenueStatistics());
        model.addAttribute("adminStats", statisticsService.viewStatistics());
        return "admin/admin-dashboard";
    }
}

