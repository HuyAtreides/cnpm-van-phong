package org.office.controller;

import org.office.repository.OrderRepository;
import org.office.dto.TopProductDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.TreeMap;

@Controller
@RequestMapping("/admin/stats")
public class AdminStatsController {

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String viewStats(Model model) {
        Double revenue = orderRepository.sumRevenue();
        Long orders = orderRepository.countCompletedOrders();
        List<TopProductDto> topProducts = orderRepository.findTopSellingProducts(PageRequest.of(0, 10));
        
        // Get order status counts
        List<Object[]> statusCounts = orderRepository.countByStatus();
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : statusCounts) {
            statusMap.put((String) row[0], (Long) row[1]);
        }
        
        // Get monthly revenue data
        List<Object[]> monthlyRevenueData = orderRepository.getMonthlyRevenue();
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();
        
        // Initialize all 12 months with 0
        String[] monthNames = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
        for (String month : monthNames) {
            monthlyRevenue.put(month, 0.0);
        }
        
        // Fill in actual data
        for (Object[] row : monthlyRevenueData) {
            int month = ((Number) row[0]).intValue();
            Double amount = ((Number) row[2]).doubleValue();
            if (month >= 1 && month <= 12) {
                monthlyRevenue.put(monthNames[month - 1], amount);
            }
        }

        model.addAttribute("revenue", revenue != null ? revenue : 0.0);
        model.addAttribute("orders", orders != null ? orders : 0);
        model.addAttribute("topProducts", topProducts);
        model.addAttribute("statusMap", statusMap);
        model.addAttribute("monthlyRevenue", monthlyRevenue);
        
        return "admin/stats";
    }
}

