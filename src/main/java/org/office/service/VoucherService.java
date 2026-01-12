package org.office.service;

import org.office.model.*;
import org.office.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Optional;

@Service
public class VoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    
    public Optional<Voucher> findByCode(String voucherCode) {
        return voucherRepository.findByCode(voucherCode);
    }

    
    public boolean isValid(Voucher voucher) {
        Date now = new Date();
        return voucher.getIsDelete() == 0
            && !now.before(voucher.getDateStart())
            && !now.after(voucher.getDateEnd());
    }

    
    public boolean canApply(Voucher voucher, Order order) {
        if (!isValid(voucher)) {
            return false;
        }

        if (voucher instanceof VoucherByPrice) {
            VoucherByPrice voucherByPrice = (VoucherByPrice) voucher;
            return order.getTotalCost() >= voucherByPrice.getMinOrderValue();
        }

        if (voucher instanceof VoucherByProduct) {
            
            VoucherByProduct voucherByProduct = (VoucherByProduct) voucher;
            return order.getOrderItems().stream()
                .anyMatch(item -> voucherByProduct.getProductTypes()
                    .contains(item.getProductType()));
        }

        return false;
    }

    
    public Double calculateDiscount(Voucher voucher, Order order) {
        if (!canApply(voucher, order)) {
            return 0.0;
        }

        if (voucher instanceof VoucherByPrice) {
            VoucherByPrice voucherByPrice = (VoucherByPrice) voucher;
            double discount = order.getTotalCost() * (voucherByPrice.getDiscountPercent() / 100.0);
            
            
            if (voucherByPrice.getMaxDiscount() != null && discount > voucherByPrice.getMaxDiscount()) {
                discount = voucherByPrice.getMaxDiscount();
            }
            
            return discount;
        }

        if (voucher instanceof VoucherByProduct) {
            VoucherByProduct voucherByProduct = (VoucherByProduct) voucher;
            double discount = 0.0;
            
            
            for (OrderItem item : order.getOrderItems()) {
                if (voucherByProduct.getProductTypes().contains(item.getProductType())) {
                    discount += item.getPrice() * item.getQuantity() * (voucherByProduct.getDiscountPercent() / 100.0);
                }
            }
            
            return discount;
        }

        return 0.0;
    }

    
    public void applyVoucher(String voucherCode, Order order) {
        Optional<Voucher> voucherOpt = findByCode(voucherCode);
        
        if (voucherOpt.isEmpty()) {
            throw new RuntimeException("Voucher not found");
        }

        Voucher voucher = voucherOpt.get();
        
        if (!canApply(voucher, order)) {
            throw new RuntimeException("Voucher cannot be applied to this order");
        }

        Double discount = calculateDiscount(voucher, order);
        order.setDiscount(discount);
        order.setActualCost(order.getTotalCost() - discount);

        
        voucherRepository.save(voucher);
    }
}

