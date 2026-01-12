package org.office.repository;

import org.office.model.Wishlist;
import org.office.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
    Optional<Wishlist> findByCustomer(Customer customer);

    boolean existsByCustomer_UserIdAndProducts_ProductId(
            Integer userId,
            Integer productId
    );

    @Query("""
        SELECT p.productId
        FROM Wishlist w
        JOIN w.products p
        WHERE w.customer.userId = :customerId
    """)
    List<Integer> findWishlistProductIds(@Param("customerId") Integer customerId);
}

