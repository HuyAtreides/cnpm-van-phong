package org.example.vanphong.controller;

import org.example.vanphong.model.Order;
import org.example.vanphong.repository.OrderRepository;
import org.example.vanphong.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/orders")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "admin/order/list";
    }

    @GetMapping("/{id}")
    public String viewOrder(@PathVariable Integer id, Model model) {
        Order order = orderService.getOrderById(id)
            .orElseThrow(() -> new RuntimeException("Order not found"));
        model.addAttribute("order", order);
        return "admin/order/detail";
    }

    @PostMapping("/{id}/cancel")
    public String cancelOrder(@PathVariable Integer id) {
        try {
            orderService.cancelOrder(id);
        } catch (Exception e) {
            
            return "redirect:/admin/orders/" + id + "?error=" + e.getMessage();
        }
        return "redirect:/admin/orders/" + id + "?cancelled";
    }

    @PostMapping("/{id}/status")
    public String updateStatus(@PathVariable Integer id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/admin/orders/" + id + "?updated";
    }
}

