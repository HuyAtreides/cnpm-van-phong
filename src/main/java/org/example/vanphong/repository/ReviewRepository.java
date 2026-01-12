package org.example.vanphong.repository;

import org.example.vanphong.model.Review;
import org.example.vanphong.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductOrderByCreateAtDesc(Product product);
}

