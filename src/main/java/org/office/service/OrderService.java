package org.office.service;

import org.office.model.*;
import org.office.repository.CartRepository;
import org.office.repository.CustomerRepository;
import org.office.repository.OrderItemRepository;
import org.office.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private CartRepository cartRepository;

    public Order createOrder(Integer customerId, String cityOfProvince, String district,
                             String ward, String streetNumber, String phone,
                             List<Integer> selectedItemIds, String voucherCode, String paymentMethod) {
        
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            throw new RuntimeException("Customer not found");
        }

        Cart cart = cartService.getOrCreateCart(customerId);
        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }


        List<CartItem> selectedItems = cart.getCartItems().stream()
            .filter(item -> selectedItemIds != null && selectedItemIds.contains(item.getCartItemId()))
            .collect(java.util.stream.Collectors.toList());

        if (selectedItems.isEmpty()) {
            throw new RuntimeException("No items selected for checkout");
        }
        
        Order order = new Order();
        order.setCustomer(customer.get());
        order.setOrderDate(new Date());
        order.setStatus("Đang xử lý");
        order.setCityOfProvince(cityOfProvince);
        order.setDistrict(district);
        order.setWard(ward);
        order.setStreetNumber(streetNumber);
        order.setPhone(phone);
        order.setPaymentMethod(paymentMethod != null ? paymentMethod : "COD");

        Double total = selectedItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
        
        order.setTotalCost(total);
        order.setDiscount(0.0);
        order.setActualCost(total);


        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem cartItem : selectedItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProductType(cartItem.getProductType());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProductType().getPrice());
            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            try {
                voucherService.applyVoucher(voucherCode, order);
            } catch (Exception e) {
                System.out.println("Voucher error: " + e.getMessage());
            }
        }

        Order savedOrder = orderRepository.save(order);


        cart.getCartItems().removeAll(selectedItems);
        cartRepository.save(cart);

        return savedOrder;
    }

    public List<Order> getOrdersByCustomer(Integer customerId) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isEmpty()) {
            return List.of();
        }
        return orderRepository.findByCustomerWithItems(customer.get());
    }

    @Autowired
    private OrderItemRepository orderItemRepository;

    public Optional<Order> getOrderById(Integer orderId) {
        Optional<Order> orderOpt = orderRepository.findOrderWithDetails(orderId);
        if (orderOpt.isPresent()) {
            Order order = orderOpt.get();
            if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
                List<OrderItem> items = orderItemRepository.findByOrder(order);
                order.setOrderItems(items);
            }

            if (order.getOrderItems() != null) {
                order.getOrderItems().forEach(item -> {
                    if (item.getProductType() != null) {
                        org.hibernate.Hibernate.initialize(item.getProductType());
                        if (item.getProductType().getProduct() != null) {
                            org.hibernate.Hibernate.initialize(item.getProductType().getProduct());
                        }
                    }
                });
            }

            return Optional.of(order);
        }
        return Optional.empty();
    }

    public void cancelOrder(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && "Đang xử lý".equals(order.get().getStatus())) {
            Order o = order.get();
            o.setStatus("Đã hủy");
            orderRepository.save(o);
        } else {
            throw new RuntimeException("Order cannot be cancelled");
        }
    }

    public void updateOrderStatus(Integer orderId, String status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            Order o = order.get();
            o.setStatus(status);
            orderRepository.save(o);
        }
    }

    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }
}
