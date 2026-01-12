package org.example.vanphong.service;

import org.example.vanphong.model.Customer;
import org.example.vanphong.model.Product;
import org.example.vanphong.model.Wishlist;
import org.example.vanphong.repository.CustomerRepository;
import org.example.vanphong.repository.ProductRepository;
import org.example.vanphong.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class WishlistService {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductRepository productRepository;

    
    public Wishlist getOrCreateWishlist(Integer customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<Wishlist> existingWishlist = wishlistRepository.findByCustomer(customer.get());
        if (existingWishlist.isPresent()) {
            return existingWishlist.get();
        }

        Wishlist newWishlist = new Wishlist();
        newWishlist.setCustomer(customer.get());
        return wishlistRepository.save(newWishlist);
    }

    
    public void addToWishlist(Integer customerId, Integer productId) {
        Wishlist wishlist = getOrCreateWishlist(customerId);
        
        Optional<Product> product = productRepository.findById(productId);
        if (product.isEmpty()) {
            throw new RuntimeException("Product not found");
        }

        
        if (!wishlist.getProducts().contains(product.get())) {
            wishlist.getProducts().add(product.get());
            wishlistRepository.save(wishlist);
        }
    }

    
    public void removeFromWishlist(Integer customerId, Integer productId) {
        Wishlist wishlist = getOrCreateWishlist(customerId);
        
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            wishlist.getProducts().remove(product.get());
            wishlistRepository.save(wishlist);
        }
    }

    
    public List<Product> getWishlistProducts(Integer customerId) {
        Wishlist wishlist = getOrCreateWishlist(customerId);
        return wishlist.getProducts();
    }

    
    public void clearWishlist(Integer customerId) {
        Wishlist wishlist = getOrCreateWishlist(customerId);
        wishlist.getProducts().clear();
        wishlistRepository.save(wishlist);
    }
}

