package org.office.controller;

import org.office.model.Cart;
import org.office.model.CartItem;
import org.office.model.Customer;
import org.office.model.User;
import org.office.service.CartService;
import org.office.service.UserService;
import org.office.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private UserService userService; 

    
    @GetMapping
    public String viewCart(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }

        
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (user == null) {
             return "redirect:/login";
        }

        
        Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
        
        if (customer == null) {
            
            return "redirect:/?message=Account type not supported for shopping";
        }

        Cart cart = cartService.getOrCreateCart(customer.getUserId());
        Double total = cartService.calculateCartTotal(customer.getUserId());

        model.addAttribute("cart", cart);
        model.addAttribute("cartItems", cart.getCartItems());
        model.addAttribute("total", total);

        return "cart";
    }

    
    @PostMapping("/add")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addToCart(
            @RequestParam Integer productTypeId,
            @RequestParam(defaultValue = "1") Integer quantity,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            response.put("success", false);
            response.put("message", "Vui lòng đăng nhập để thêm sản phẩm vào giỏ hàng");
            response.put("redirectUrl", "/login");
            return ResponseEntity.ok(response);
        }

        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            if (user == null) {
                 response.put("success", false);
                 response.put("message", "User not found");
                 return ResponseEntity.ok(response);
            }
            
            Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
            
            if (customer == null) {
                 response.put("success", false);
                 response.put("message", "Tài khoản quản trị không thể mua hàng");
                 return ResponseEntity.ok(response);
            }

            CartItem cartItem = cartService.addToCart(
                    customer.getUserId(),
                    productTypeId,
                    quantity
            );

            Cart cart = cartService.getOrCreateCart(customer.getUserId());
            int itemCount = cart.getCartItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            response.put("success", true);
            response.put("message", "Đã thêm sản phẩm vào giỏ hàng");
            response.put("cartCount", itemCount);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/update/{itemId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @PathVariable Integer itemId,
            @RequestParam Integer quantity,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return ResponseEntity.ok(response);
        }

        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
            if (customer == null) {
                 response.put("success", false);
                 response.put("message", "Unauthorized customer");
                 return ResponseEntity.ok(response);
            }

            cartService.updateCartItemQuantity(itemId, quantity);

            Double total = cartService.calculateCartTotal(customer.getUserId());
            Cart cart = cartService.getOrCreateCart(customer.getUserId());
            int itemCount = cart.getCartItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            response.put("success", true);
            response.put("message", "Đã cập nhật số lượng");
            response.put("total", total);
            response.put("cartCount", itemCount);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    
    @DeleteMapping("/remove/{itemId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @PathVariable Integer itemId,
            Authentication authentication) {

        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            response.put("success", false);
            response.put("message", "Unauthorized");
            return ResponseEntity.ok(response);
        }

        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
            if (customer == null) {
                 response.put("success", false);
                 response.put("message", "Unauthorized customer");
                 return ResponseEntity.ok(response);
            }

            cartService.removeFromCart(itemId);

            Double total = cartService.calculateCartTotal(customer.getUserId());
            Cart cart = cartService.getOrCreateCart(customer.getUserId());
            int itemCount = cart.getCartItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            response.put("success", true);
            response.put("message", "Đã xóa sản phẩm khỏi giỏ hàng");
            response.put("total", total);
            response.put("cartCount", itemCount);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Có lỗi xảy ra: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    
    @GetMapping("/count")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getCartCount(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication == null) {
            response.put("count", 0);
            return ResponseEntity.ok(response);
        }

        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            if (user == null) {
                response.put("count", 0);
                return ResponseEntity.ok(response);
            }

            Customer customer = customerRepository.findById(user.getUserId()).orElse(null);
            if (customer == null) {
                
                response.put("count", 0);
                return ResponseEntity.ok(response);
            }

            Cart cart = cartService.getOrCreateCart(customer.getUserId());
            int itemCount = cart.getCartItems().stream()
                    .mapToInt(CartItem::getQuantity)
                    .sum();

            response.put("count", itemCount);

        } catch (Exception e) {
            response.put("count", 0);
        }

        return ResponseEntity.ok(response);
    }
}

