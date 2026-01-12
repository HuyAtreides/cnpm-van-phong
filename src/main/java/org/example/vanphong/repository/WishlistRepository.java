package org.example.vanphong.repository;

import org.example.vanphong.model.Wishlist;
import org.example.vanphong.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByCustomer(Customer customer);
}

