package org.office.service;

import org.office.dto.TopProductDto;
import org.office.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.LinkedHashMap;

@Service
public class AdminStatsService {

    @Autowired
    private OrderRepository orderRepository;

    public Double getRevenue() {
        Double revenue = orderRepository.sumRevenue();
        return revenue != null ? revenue : 0.0;
    }

    public Long getCompletedOrdersCount() {
        Long orders = orderRepository.countCompletedOrders();
        return orders != null ? orders : 0;
    }

    public List<TopProductDto> getTopSellingProducts(Pageable pageable) {
        return orderRepository.findTopSellingProducts(pageable);
    }

    public Map<String, Long> getOrderStatusCounts() {
        List<Object[]> statusCounts = orderRepository.countByStatus();
        Map<String, Long> statusMap = new HashMap<>();
        for (Object[] row : statusCounts) {
            statusMap.put((String) row[0], (Long) row[1]);
        }
        return statusMap;
    }

    public Map<String, Double> getMonthlyRevenueData() {
        List<Object[]> monthlyRevenueData = orderRepository.getMonthlyRevenue();
        Map<String, Double> monthlyRevenue = new LinkedHashMap<>();

        // Initialize all 12 months with 0
        String[] monthNames = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8", "T9", "T10", "T11", "T12"};
        for (String month : monthNames) {
            monthlyRevenue.put(month, 0.0);
        }

        // Fill in actual data
        for (Object[] row : monthlyRevenueData) {
            int month = ((Number) row[0]).intValue();
            Double amount = ((Number) row[2]).doubleValue();
            if (month >= 1 && month <= 12) {
                monthlyRevenue.put(monthNames[month - 1], amount);
            }
        }

        return monthlyRevenue;
    }
}
