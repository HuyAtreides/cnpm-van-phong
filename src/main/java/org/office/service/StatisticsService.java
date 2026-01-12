package org.office.service;

import org.office.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BlogRepository blogRepository;

    
    public Map<String, Object> viewStatistics() {
        Map<String, Object> stats = new HashMap<>();

        
        stats.put("totalUsers", userRepository.count());
        stats.put("totalCustomers", customerRepository.count());

        
        stats.put("totalProducts", productRepository.count());
        stats.put("activeProducts", productRepository.findByIsDelete(0).size());

        
        stats.put("totalOrders", orderRepository.count());
        stats.put("processingOrders", orderRepository.findByStatus("Đang xử lý").size());
        stats.put("completedOrders", orderRepository.findByStatus("Hoàn thành").size());
        stats.put("cancelledOrders", orderRepository.findByStatus("Đã hủy").size());

        
        stats.put("totalReviews", reviewRepository.count());

        
        stats.put("totalBlogs", blogRepository.count());
        stats.put("approvedBlogs", blogRepository.findByApproval(1).size());
        stats.put("pendingBlogs", blogRepository.findByApproval(0).size());

        return stats;
    }

    
    public Map<String, Object> getRevenueStatistics() {
        Map<String, Object> stats = new HashMap<>();

        
        Double totalRevenue = orderRepository.findByStatus("Hoàn thành").stream()
            .mapToDouble(order -> order.getActualCost())
            .sum();

        stats.put("totalRevenue", totalRevenue);
        stats.put("completedOrdersCount", orderRepository.findByStatus("Hoàn thành").size());

        
        long completedCount = orderRepository.findByStatus("Hoàn thành").size();
        stats.put("averageOrderValue", completedCount > 0 ? totalRevenue / completedCount : 0.0);

        return stats;
    }

    
    public Map<String, Object> getProductStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalProducts", productRepository.count());
        stats.put("activeProducts", productRepository.findByIsDelete(0).size());
        stats.put("deletedProducts", productRepository.count() - productRepository.findByIsDelete(0).size());

        return stats;
    }

    
    public Map<String, Object> getCustomerStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalCustomers", customerRepository.count());
        stats.put("customersWithOrders", orderRepository.findAll().stream()
            .map(order -> order.getCustomer().getUserId())
            .distinct()
            .count());

        return stats;
    }
}

