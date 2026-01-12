package org.example.vanphong.controller;

import org.example.vanphong.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String name,
                              @RequestParam String email,
                              @RequestParam String password,
                              @RequestParam String phone,
                              @RequestParam(required = false) String address,
                              @RequestParam(required = false) String gender,
                              Model model) {
        try {
            if (userService.emailExists(email)) {
                model.addAttribute("error", "Email đã tồn tại");
                return "register";
            }

            userService.registerCustomer(name, email, password, phone, 
                                        address != null ? address : "", 
                                        gender != null ? gender : "Male");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Đăng ký thất bại: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, Model model) {
        
        
        if (!userService.emailExists(email)) {
            model.addAttribute("error", "Email không tồn tại trong hệ thống");
            return "forgot-password";
        }
        
        return "redirect:/reset-password?email=" + email;
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam(required = false) String email, Model model) {
        if (email != null) {
            model.addAttribute("email", email);
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String email, 
                                       @RequestParam String password, 
                                       @RequestParam String confirmPassword, 
                                       Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu xác nhận không khớp");
            model.addAttribute("email", email);
            return "reset-password";
        }
        
        org.example.vanphong.model.User user = userService.findByEmail(email).orElse(null);
        if (user != null) {
             
             userService.updatePassword(user, password);
             return "redirect:/login?resetSuccess";
        }
        
        model.addAttribute("error", "Lỗi không tìm thấy người dùng");
        return "reset-password";
    }
}

