package org.office.repository;

import org.office.model.ProductType;
import org.office.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductTypeRepository extends JpaRepository<ProductType, Integer> {
    List<ProductType> findByProduct(Product product);
    
    @Query("SELECT pt FROM ProductType pt WHERE pt.price BETWEEN :minPrice AND :maxPrice")
    List<ProductType> findByPriceRange(@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice);
}

