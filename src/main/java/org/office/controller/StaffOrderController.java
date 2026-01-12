package org.office.controller;

import org.office.repository.OrderRepository;
import org.office.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/staff/orders")
public class StaffOrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public String listOrders(Model model) {
        model.addAttribute("orders", orderRepository.findAll());
        return "staff/staff-order-list";
    }

    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Integer id, Model model) {
        model.addAttribute("order", orderRepository.findById(id).orElseThrow());
        return "staff/staff-order-detail";
    }

    @PostMapping("/{id}/update-status")
    public String updateStatus(@PathVariable Integer id, @RequestParam String status) {
        orderService.updateOrderStatus(id, status);
        return "redirect:/staff/orders/" + id;
    }
}

