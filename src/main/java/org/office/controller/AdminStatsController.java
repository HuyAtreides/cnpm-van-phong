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

        model.addAttribute("revenue", revenue != null ? revenue : 0.0);
        model.addAttribute("orders", orders != null ? orders : 0);
        model.addAttribute("topProducts", topProducts);
        
        return "admin/stats";
    }
}
