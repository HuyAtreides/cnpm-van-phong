package org.example.vanphong.service;

import org.example.vanphong.model.User;
import org.example.vanphong.model.Customer;
import org.example.vanphong.model.Role;
import org.example.vanphong.model.Log;
import org.example.vanphong.repository.UserRepository;
import org.example.vanphong.repository.CustomerRepository;
import org.example.vanphong.repository.RoleRepository;
import org.example.vanphong.repository.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.Date;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private LogRepository logRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User findByUsername(String username) {
        return userRepository.findByEmail(username).orElse(null);
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public Customer registerCustomer(String name, String email, String password, String phone, String address, String gender) {
        if (emailExists(email)) {
            throw new RuntimeException("Email already exists");
        }

        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setGender(gender);
        customer.setStatus("Active");
        customer.setIsDelete(0);

        
        Role role = roleRepository.findByRoleName("CUSTOMER")
            .orElseGet(() -> {
                
                Role newRole = new Role();
                newRole.setRoleId(3);
                newRole.setRoleName("CUSTOMER");
                return roleRepository.save(newRole);
            });
        customer.setRole(role);

        return customerRepository.save(customer);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }
    
    
    public User login(String email, String password) {
        
        Optional<User> userOptional = findByEmail(email);
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        
        User user = userOptional.get();
        
        
        if (!validatePassword(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        
        
        setActive(user);
        
        
        loginRecord(user);
        
        return user;
    }
    
    
    public void setActive(User user) {
        user.setIsActive(1);
        userRepository.save(user);
    }
    
    
    public void setInactive(User user) {
        user.setIsActive(0);
        userRepository.save(user);
    }
    
    
    public void loginRecord(User user) {
        Log log = new Log();
        log.setUser(user);
        log.setContent("User logged in: " + user.getEmail());
        log.setDateLog(new Date());
        logRepository.save(log);
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void updatePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }
}

