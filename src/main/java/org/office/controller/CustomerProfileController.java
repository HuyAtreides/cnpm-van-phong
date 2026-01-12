package org.office.controller;

import org.office.model.Address;
import org.office.model.User;
import org.office.repository.AddressRepository;
import org.office.repository.CustomerRepository;
import org.office.repository.UserRepository;
import org.office.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CustomerProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AddressRepository addressRepository;

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        if (authentication == null) {
            return "redirect:/login";
        }
        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);
        
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        List<Address> addresses = addressRepository.findByUser(user);
        model.addAttribute("addresses", addresses);
        
        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User updatedUser, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) return "redirect:/login";

        String username = authentication.getName();
        User currentUser = userRepository.findByEmail(username).orElse(null);

        if (currentUser != null) {
            currentUser.setName(updatedUser.getName());
            currentUser.setPhone(updatedUser.getPhone());
            currentUser.setAddress(updatedUser.getAddress()); 
            currentUser.setGender(updatedUser.getGender());
            userRepository.save(currentUser);
            redirectAttributes.addFlashAttribute("success", "Cập nhật thông tin thành công!");
        } else {
            return "redirect:/profile?error";
        }

        return "redirect:/profile";
    }

    

    @PostMapping("/profile/address/add")
    public String addAddress(@ModelAttribute Address address, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) return "redirect:/login";
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            address.setUser(user);
            
            
            List<Address> existing = addressRepository.findByUser(user);
            if (existing.isEmpty()) {
                address.setDefault(true);
            }
            
            addressRepository.save(address);
            redirectAttributes.addFlashAttribute("success", "Thêm địa chỉ thành công!");
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/address/delete")
    public String deleteAddress(@RequestParam Integer addressId, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) return "redirect:/login";
        Address address = addressRepository.findById(addressId).orElse(null);
        if (address != null && address.getUser().getEmail().equals(authentication.getName())) {
            addressRepository.delete(address);
            redirectAttributes.addFlashAttribute("success", "Xóa địa chỉ thành công!");
        }
        return "redirect:/profile";
    }
    
    @PostMapping("/profile/address/default")
    public String setDefaultAddress(@RequestParam Integer addressId, Authentication authentication, RedirectAttributes redirectAttributes) {
        if (authentication == null) return "redirect:/login";
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).orElse(null);
        if (user != null) {
            List<Address> addresses = addressRepository.findByUser(user);
            for (Address addr : addresses) {
                if (addr.getAddressId().equals(addressId)) {
                    addr.setDefault(true);
                } else {
                    addr.setDefault(false);
                }
                addressRepository.save(addr);
            }
            redirectAttributes.addFlashAttribute("success", "Đã đặt địa chỉ mặc định!");
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(Authentication authentication,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmNewPassword,
                                 Model model) {
        if (authentication == null) return "redirect:/login";

        if (!newPassword.equals(confirmNewPassword)) {
            return "redirect:/profile?error=PasswordMismatch";
        }

        String email = authentication.getName();
        User user = userService.findByEmail(email).orElse(null);

        if (user != null) {
             
             
             
             
             
             user.setPassword("{noop}" + newPassword);
             userRepository.save(user);
             return "redirect:/profile?success=PasswordChanged";
        }

        return "redirect:/profile?error";
    }
}

