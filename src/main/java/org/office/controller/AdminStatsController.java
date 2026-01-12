package org.office.controller;

import org.office.service.AdminStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/stats")
public class AdminStatsController {

    @Autowired
    private AdminStatsService adminStatsService;

    @GetMapping
    public String viewStats(Model model) {
        Double revenue = adminStatsService.getRevenue();
        Long orders = adminStatsService.getCompletedOrdersCount();
        var topProducts = adminStatsService.getTopSellingProducts(PageRequest.of(0, 10));
        var statusMap = adminStatsService.getOrderStatusCounts();
        var monthlyRevenue = adminStatsService.getMonthlyRevenueData();

        model.addAttribute("revenue", revenue);
        model.addAttribute("orders", orders);
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("statusMap", statusMap);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        
        return "admin/stats";
    }
}

