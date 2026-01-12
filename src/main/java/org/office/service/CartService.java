package org.office.service;

import org.office.model.Cart;
import org.office.model.CartItem;
import org.office.model.Customer;
import org.office.model.ProductType;
import org.office.repository.CartItemRepository;
import org.office.repository.CartRepository;
import org.office.repository.CustomerRepository;
import org.office.repository.ProductTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ProductTypeRepository productTypeRepository;

    public Cart getOrCreateCart(Integer customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Optional<Cart> existingCart = cartRepository.findByCustomer(customer.get());
        if (existingCart.isPresent()) {
            return existingCart.get();
        }

        
        Cart newCart = new Cart();
        newCart.setCustomer(customer.get());
        return cartRepository.save(newCart);
    }

    public CartItem addToCart(Integer customerId, Integer typeId, Integer quantity) {
        Cart cart = getOrCreateCart(customerId);
        
        Optional<ProductType> productType = productTypeRepository.findById(typeId);
        if (productType.isEmpty()) {
            throw new RuntimeException("Product type not found");
        }

        
        Optional<CartItem> existingItem = cart.getCartItems().stream()
            .filter(item -> item.getProductType().getTypeId().equals(typeId))
            .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            return cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProductType(productType.get());
            newItem.setQuantity(quantity);
            newItem.setPrice(productType.get().getPrice());
            return cartItemRepository.save(newItem);
        }
    }

    public void removeFromCart(Integer cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public CartItem updateCartItemQuantity(Integer cartItemId, Integer quantity) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            return null;
        }
        
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    public void clearCart(Integer customerId) {
        Cart cart = getOrCreateCart(customerId);
        
        cartItemRepository.deleteAll(cart.getCartItems());
        
        cart.getCartItems().clear();
        
        cartRepository.save(cart);
    }

    public Double calculateCartTotal(Integer customerId) {
        Cart cart = getOrCreateCart(customerId);
        return cart.getCartItems().stream()
            .mapToDouble(item -> item.getPrice() * item.getQuantity())
            .sum();
    }
}

