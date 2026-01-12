package org.office.service;

import org.office.model.Product;
import org.office.model.Category;
import org.office.model.Review;
import org.office.repository.ProductRepository;
import org.office.repository.CategoryRepository;
import org.office.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Review> getProductReviews(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(value -> reviewRepository.findByProductOrderByCreateAtDesc(value)).orElse(List.of());
    }

    public List<Product> getRelatedProducts(Integer productId) {
        Optional<Product> currentProduct = productRepository.findById(productId);
        if (currentProduct.isPresent()) {
            Category category = currentProduct.get().getCategory();
            List<Product> similarProducts = productRepository.findByCategoryAndIsDelete(category, 0);
            return similarProducts.stream()
                .filter(p -> !p.getProductId().equals(productId))
                .limit(4)
                .collect(java.util.stream.Collectors.toList());
        }
        return List.of();
    }

    public List<Product> getAllActiveProducts() {
        return productRepository.findByIsDelete(0);
    }
    
    public Page<Product> getAllActiveProducts(Pageable pageable) {
        return productRepository.findByIsDelete(0, pageable);
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveProducts();
        }
        return productRepository.searchByKeyword(keyword);
    }

    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllActiveProducts(pageable);
        }
        return productRepository.searchByKeyword(keyword, pageable);
    }

    public List<Product> getProductsByCategory(Integer categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return productRepository.findByCategoryAndIsDelete(category.get(), 0);
        }
        return List.of();
    }
    
    public Page<Product> getProductsByCategory(Integer categoryId, Pageable pageable) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isPresent()) {
            return productRepository.findByCategoryAndIsDelete(category.get(), 0, pageable);
        }
        return Page.empty();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product p = product.get();
            p.setIsDelete(1); 
            productRepository.save(p);
        }
    }
}

