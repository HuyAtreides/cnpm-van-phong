package org.example.vanphong.repository;

import org.example.vanphong.model.Product;
import org.example.vanphong.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByIsDelete(Integer isDelete);
    Page<Product> findByIsDelete(Integer isDelete, Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.isDelete = 0 AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.descript) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Product> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Product p WHERE p.isDelete = 0 AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(p.descript) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
    
    List<Product> findByCategoryAndIsDelete(Category category, Integer isDelete);
    Page<Product> findByCategoryAndIsDelete(Category category, Integer isDelete, Pageable pageable);
    
    List<Product> findByName(String name);
}

