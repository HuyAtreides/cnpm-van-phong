package org.office.controller;

import org.office.model.Role;
import org.office.model.Staff;
import org.office.repository.RoleRepository;
import org.office.repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/staff")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminStaffController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String listStaff(Model model) {
        model.addAttribute("staffList", staffRepository.findAll());
        return "admin/admin-staff-list";
    }

    @GetMapping("/new")
    public String newStaffForm(Model model) {
        model.addAttribute("staff", new Staff());
        return "admin/admin-staff-form";
    }

    @PostMapping("/save")
    public String saveStaff(@ModelAttribute Staff staff) {
        
        if (staff.getUserId() == null) {
            staff.setStatus("Active");
            staff.setIsDelete(0);
            staff.setIsActive(1); 
            
            
            Role role = roleRepository.findByRoleName("STAFF")
                .orElseThrow(() -> new RuntimeException("Role STAFF not found"));
            staff.setRole(role);
            
            
            staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        } else {
            
            Staff existing = staffRepository.findById(staff.getUserId())
                .orElseThrow(() -> new RuntimeException("Staff not found"));
            
            existing.setName(staff.getName());
            existing.setPhone(staff.getPhone());
            existing.setAddress(staff.getAddress());
            existing.setGender(staff.getGender());
            existing.setPosition(staff.getPosition());
            
            
            
            staff = existing;
        }

        staffRepository.save(staff);
        return "redirect:/admin/staff";
    }

    @GetMapping("/{id}/delete")
    public String deleteStaff(@PathVariable Integer id) {
        Staff staff = staffRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Staff not found"));
        staff.setIsDelete(1); 
        staff.setStatus("Inactive");
        staffRepository.save(staff);
        return "redirect:/admin/staff";
    }
}

