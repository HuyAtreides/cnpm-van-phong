package org.office.service;

import org.office.model.Customer;
import org.office.model.Product;
import org.office.model.Review;
import org.office.repository.CustomerRepository;
import org.office.repository.ProductRepository;
import org.office.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    
    public Review writeReview(Integer customerId, Integer productId, Integer rating, String comment) {
        
        if (rating < 1 || rating > 5) {
            throw new RuntimeException("Rating must be between 1 and 5");
        }

        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        Review review = new Review();
        review.setCustomer(customer.get());
        review.setProduct(product.get());
        review.setRating(rating.doubleValue());
        review.setContent(comment);
        review.setCreateAt(new Date());

        return reviewRepository.save(review);
    }

    
    public List<Review> getReviewsByProduct(Integer productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }
        return reviewRepository.findByProductOrderByCreateAtDesc(product.get());
    }

    
    public Double getAverageRating(Integer productId) {
        List<Review> reviews = getReviewsByProduct(productId);
        if (reviews.isEmpty()) {
            return 0.0;
        }
        
        double sum = reviews.stream()
            .mapToDouble(Review::getRating)
            .sum();
        
        return sum / reviews.size();
    }

    
    public void deleteReview(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

