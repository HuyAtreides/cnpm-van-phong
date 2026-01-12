package org.office.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.office.model.User;
import org.office.service.UserService;
import org.office.service.WishlistService;
import org.office.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/wishlist")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public String viewWishlist(Authentication authentication, Model model) {
        try {
            if (authentication == null) return "redirect:/login";

            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            if (user == null || customerRepository.findById(user.getUserId()).isEmpty()) {
                return "redirect:/";
            }

            model.addAttribute("products", wishlistService.getWishlistProducts(user.getUserId()));
            return "wishlist";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/?error=WishlistError";
        }
    }

    @PostMapping("/add")
    public String addToWishlist(Authentication authentication, @RequestParam Integer productId, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            if (authentication == null) return "redirect:/login";

            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);
            if (user != null) {
                if (customerRepository.existsById(user.getUserId())) {
                    wishlistService.addToWishlist(user.getUserId(), productId);
                    redirectAttributes.addFlashAttribute("message", "Đã thêm vào yêu thích!");
                } else {
                     redirectAttributes.addFlashAttribute("error", "Tính năng này chỉ dành cho khách hàng.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Lỗi hệ thống: " + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/wishlist");
//        return "redirect:/wishlist";
    }

    @PostMapping("/remove")
    public String removeFromWishlist(Authentication authentication, @RequestParam Integer productId, HttpServletRequest request) {
        if (authentication == null) return "redirect:/login";

        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        if (user != null) {
            wishlistService.removeFromWishlist(user.getUserId(), productId);
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/wishlist");
//        return "redirect:/wishlist";
    }
}

