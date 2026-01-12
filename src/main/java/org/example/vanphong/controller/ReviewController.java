package org.example.vanphong.controller;

import org.example.vanphong.model.Customer;
import org.example.vanphong.model.Order;
import org.example.vanphong.model.OrderItem;
import org.example.vanphong.model.Product;
import org.example.vanphong.model.Review;
import org.example.vanphong.model.User;
import org.example.vanphong.service.UserService;
import org.example.vanphong.repository.CustomerRepository;
import org.example.vanphong.repository.OrderRepository;
import org.example.vanphong.repository.ProductRepository;
import org.example.vanphong.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;

    @PostMapping("/reviews/add")
    public String addReview(@RequestParam Integer orderId,
                            @RequestParam Integer productId,
                            @RequestParam Double rating,
                            @RequestParam String content,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);

        if (customer == null) {
            return "redirect:/";
        }

        // Validate Order
        Optional<Order> orderOpt = orderRepository.findById(orderId);
        if (orderOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Đơn hàng không tồn tại.");
            return "redirect:/orders";
        }
        Order order = orderOpt.get();

        // Check ownership
        if (!order.getCustomer().getUserId().equals(customer.getUserId())) {
             redirectAttributes.addFlashAttribute("error", "Bạn không có quyền đánh giá đơn hàng này.");
             return "redirect:/orders";
        }

        // Check status
        if (!"Hoàn thành".equalsIgnoreCase(order.getStatus())) {
            redirectAttributes.addFlashAttribute("error", "Chỉ có thể đánh giá đơn hàng đã hoàn thành.");
            return "redirect:/orders";
        }

        Date utilDate = new Date(order.getOrderDate().getTime());
        LocalDate orderDate = utilDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (orderDate.plusDays(15).isBefore(LocalDate.now())) {
             redirectAttributes.addFlashAttribute("error", "Đã quá hạn 15 ngày để đánh giá.");
             return "redirect:/orders";
        }
        
        // Save Review
        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(productRepository.findById(productId).orElse(null));
        review.setRating(rating);
        review.setContent(content);
        review.setCreateAt(new Date());
        
        reviewRepository.save(review);
        redirectAttributes.addFlashAttribute("success", "Cảm ơn bạn đã đánh giá!");

        return "redirect:/orders";
    }
}
