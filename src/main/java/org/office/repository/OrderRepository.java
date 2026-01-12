package org.office.repository;

import org.office.dto.TopProductDto;
import org.office.model.Order;
import org.office.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.productType pt LEFT JOIN FETCH pt.product WHERE o.customer = :customer ORDER BY o.orderDate DESC")
    List<Order> findByCustomerWithItems(@org.springframework.data.repository.query.Param("customer") Customer customer);
    
    List<Order> findByCustomerOrderByOrderDateDesc(Customer customer);
    List<Order> findByStatus(String status);

    @org.springframework.data.jpa.repository.Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems oi LEFT JOIN FETCH oi.productType pt LEFT JOIN FETCH pt.product WHERE o.orderId = :orderId")
    java.util.Optional<Order> findOrderWithDetails(@org.springframework.data.repository.query.Param("orderId") Integer orderId);
    
    @org.springframework.data.jpa.repository.Query("SELECT SUM(o.actualCost) FROM Order o WHERE o.status = 'Hoàn thành'")
    Double sumRevenue();

    @org.springframework.data.jpa.repository.Query("SELECT COUNT(o) FROM Order o WHERE o.status = 'Hoàn thành'")
    Long countCompletedOrders();
    
    @org.springframework.data.jpa.repository.Query("SELECT new org.office.dto.TopProductDto(pt.product.name, SUM(oi.quantity)) " +
           "FROM OrderItem oi JOIN oi.productType pt JOIN oi.order o " +
           "WHERE o.status = 'Hoàn thành' " +
           "GROUP BY pt.product.name " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<TopProductDto> findTopSellingProducts(org.springframework.data.domain.Pageable pageable);
}

