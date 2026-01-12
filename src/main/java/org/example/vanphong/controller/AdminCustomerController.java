package org.example.vanphong.controller;

import org.example.vanphong.model.Customer;
import org.example.vanphong.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/customers")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminCustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public String listCustomers(Model model) {
        model.addAttribute("customers", customerRepository.findAll());
        return "admin/customer/list"; 
    }
    
    
    @GetMapping("/{id}/toggle-status")
    public String toggleStatus(@PathVariable Integer id) {
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Customer not found"));
        
        if (customer.getIsActive() == 1) {
            customer.setIsActive(0);
        } else {
            customer.setIsActive(1);
        }
        
        customerRepository.save(customer);
        return "redirect:/admin/customers";
    }
}

