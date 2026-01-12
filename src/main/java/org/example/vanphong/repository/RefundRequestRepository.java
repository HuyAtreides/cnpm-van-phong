package org.example.vanphong.repository;

import org.example.vanphong.model.RefundRequest;
import org.example.vanphong.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRequestRepository extends JpaRepository<RefundRequest, Integer> {
    List<RefundRequest> findByOrder(Order order);
    List<RefundRequest> findAllByOrderByCreatedDateDesc();
}

