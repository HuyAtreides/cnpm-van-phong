package org.office.repository;

import org.office.model.RefundRequest;
import org.office.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RefundRequestRepository extends JpaRepository<RefundRequest, Integer> {
    List<RefundRequest> findByOrder(Order order);
    List<RefundRequest> findAllByOrderByCreatedDateDesc();
}

