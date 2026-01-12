package org.office.controller;

import org.office.model.RefundRequest;
import org.office.model.Order;
import org.office.repository.RefundRequestRepository;
import org.office.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/refunds")
public class AdminRefundController {

    @Autowired
    private RefundRequestRepository refundRequestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping
    public String listRefunds(Model model) {
        List<RefundRequest> requests = refundRequestRepository.findAllByOrderByCreatedDateDesc();
        model.addAttribute("requests", requests);
        return "admin/refunds";
    }

    @PostMapping("/{id}/approve")
    public String approveRefund(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        RefundRequest request = refundRequestRepository.findById(id).orElse(null);
        if (request != null) {
            request.setStatus("APPROVED");
            refundRequestRepository.save(request);

            Order order = request.getOrder();
            if (order != null) {
                // Update order status based on request type
                if ("REFUND".equalsIgnoreCase(request.getRequestType()) || "Hoàn tiền".equalsIgnoreCase(request.getRequestType())) {
                    order.setStatus("Đang hoàn tiền");
                } else {
                    order.setStatus("Đang đổi hàng");
                }
                orderRepository.save(order);
            }
            redirectAttributes.addFlashAttribute("success", "Đã chấp nhận yêu cầu!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy yêu cầu.");
        }
        return "redirect:/admin/refunds";
    }

    @PostMapping("/{id}/reject")
    public String rejectRefund(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        RefundRequest request = refundRequestRepository.findById(id).orElse(null);
        if (request != null) {
            request.setStatus("REJECTED");
            refundRequestRepository.save(request);
            
            // Optionally revert order status if it was locked, but here we didn't lock it on request creation so just leave it.
            
            redirectAttributes.addFlashAttribute("success", "Đã từ chối yêu cầu.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Không tìm thấy yêu cầu.");
        }
        return "redirect:/admin/refunds";
    }
}
