package org.office.controller;

import org.office.model.Cart;
import org.office.model.*;
import org.office.service.CartService;
import org.office.service.OrderService;
import org.office.service.UserService;
import org.office.repository.CustomerRepository;
import java.util.List;

import org.office.repository.OrderRepository;
import org.office.repository.RefundRequestRepository;
import org.office.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private VoucherService voucherService;

    @GetMapping("/checkout")
    public String checkout(@RequestParam(required = false) List<Integer> items, 
                           @RequestParam(required = false) String voucherCode,
                           Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);

        if (customer == null) {
            return "redirect:/?message=Unauthorized";
        }

        Cart cart = cartService.getOrCreateCart(customer.getUserId());
        List<CartItem> allItems = cart.getCartItems();
        
        List<CartItem> selectedItems;
        if (items != null && !items.isEmpty()) {
            selectedItems = allItems.stream()
                .filter(item -> items.contains(item.getCartItemId()))
                .collect(java.util.stream.Collectors.toList());
        } else {
             return "redirect:/cart?message=Please select items to checkout";
        }

        if (selectedItems.isEmpty()) {
            return "redirect:/cart?message=No valid items selected";
        }

        Double total = selectedItems.stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
            
        Double discount = 0.0;
        Double finalTotal = total;
        String voucherError = null;

        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            try {
                
                java.util.Optional<Voucher> vOpt = voucherService.findByCode(voucherCode);
                
                if (vOpt.isPresent()) {
                    Voucher v = vOpt.get();
                    
                    Order dummyOrder = new Order();
                    dummyOrder.setTotalCost(total);
                    
                    List<OrderItem> orderItems = new java.util.ArrayList<>();
                    for (CartItem ci : selectedItems) {
                        OrderItem oi = new OrderItem();
                        oi.setProductType(ci.getProductType());
                        oi.setPrice(ci.getProductType().getPrice());
                        oi.setQuantity(ci.getQuantity());
                        orderItems.add(oi);
                    }
                    dummyOrder.setOrderItems(orderItems);
                    
                    if (voucherService.canApply(v, dummyOrder)) {
                        discount = voucherService.calculateDiscount(v, dummyOrder);
                        finalTotal = total - discount;
                        if (finalTotal < 0) finalTotal = 0.0;
                    } else {
                        voucherError = "Voucher không đủ điều kiện áp dụng";
                    }
                } else {
                    voucherError = "Mã voucher không tồn tại";
                }
            } catch (Exception e) {
                voucherError = "Lỗi kiểm tra voucher: " + e.getMessage();
            }
        }

        model.addAttribute("customer", customer);
        model.addAttribute("cart", cart); 
        model.addAttribute("checkoutItems", selectedItems); 
        model.addAttribute("subtotal", total);
        model.addAttribute("discount", discount);
        model.addAttribute("total", finalTotal);
        model.addAttribute("selectedItemIds", items); 
        model.addAttribute("voucherCode", voucherCode);
        model.addAttribute("voucherError", voucherError);

        return "checkout";
    }

    @PostMapping("/checkout")
    public String placeOrder(
            @RequestParam String city,
            @RequestParam String district,
            @RequestParam String ward,
            @RequestParam String street,
            @RequestParam String phone,
            @RequestParam(required = false) List<Integer> selectedItemIds,
            @RequestParam(required = false) String voucherCode,
            @RequestParam(required = false) String paymentMethod,
            Authentication authentication) {
        
        if (authentication == null) return "redirect:/login";

        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);

        if (customer == null) return "redirect:/";

        try {
            orderService.createOrder(
                customer.getUserId(),
                city,
                district,
                ward,
                street,
                phone,
                selectedItemIds,
                voucherCode,
                paymentMethod
            );
            
            
            return "redirect:/checkout/success";
        } catch (Exception e) {
            return "redirect:/checkout?error=" + e.getMessage();
        }
    }

    @GetMapping("/orders")
    public String viewOrders(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);

        if (customer == null) {
            return "redirect:/";
        }

        model.addAttribute("orders", orderService.getOrdersByCustomer(customer.getUserId()));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String viewOrderDetail(@org.springframework.web.bind.annotation.PathVariable("id") Integer orderId, Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username);
        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);

        if (customer == null) {
            return "redirect:/";
        }

        Order order = orderService.getOrderById(orderId).orElse(null);

        if (order == null || !order.getCustomer().getUserId().equals(customer.getUserId())) {
            return "redirect:/orders?error=OrderNotFound";
        }

        if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
        }

        model.addAttribute("order", order);
        return "order-detail";
    }

    @Autowired
    private RefundRequestRepository refundRequestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/order/refund")
    public String showRefundForm(@RequestParam Integer orderId, Authentication authentication, Model model) {
        if (authentication == null) return "redirect:/login";
        
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) return "redirect:/orders?error=OrderNotFound";
        
        model.addAttribute("order", order);
        return "refund-request";
    }

    @PostMapping("/order/refund")
    public String processRefundRequest(@RequestParam Integer orderId, 
                                       @RequestParam String reason,
                                       @RequestParam String requestType,
                                       Authentication authentication,
                                       org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        if (authentication == null) return "redirect:/login";

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            RefundRequest request = new RefundRequest();
            request.setOrder(order);
            request.setReason(reason);
            request.setRequestType(requestType);
            request.setStatus("PENDING");
            refundRequestRepository.save(request);

            redirectAttributes.addFlashAttribute("success", "Yêu cầu đổi trả đã được gửi thành công!");
        }
        return "redirect:/orders";
    }

    @GetMapping("/checkout/success")
    public String checkoutSuccess() {
        return "checkout-success";
    }
}

