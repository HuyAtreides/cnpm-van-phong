package org.office.controller;

import org.office.model.Voucher;
import org.office.model.VoucherByPrice;
import org.office.repository.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/vouchers")
public class VoucherRestController {

    @Autowired
    private VoucherRepository voucherRepository;

    @GetMapping
    public List<Voucher> getActiveVouchers() {
        return voucherRepository.findActiveVouchers(new Date());
    }
    
    @PostMapping("/validate")
    public java.util.Map<String, Object> validateVoucher(
            @org.springframework.web.bind.annotation.RequestBody java.util.Map<String, Object> request) {
        
        String voucherCode = (String) request.get("voucherCode");
        Double subtotal = ((Number) request.get("subtotal")).doubleValue();
        
        java.util.Map<String, Object> response = new java.util.HashMap<>();
        
        try {
            java.util.Optional<Voucher> vOpt = voucherRepository.findByCode(voucherCode);
            
            if (vOpt.isEmpty()) {
                response.put("valid", false);
                response.put("error", "Mã voucher không tồn tại");
                return response;
            }
            
            Voucher voucher = vOpt.get();
            
            // Check validity
            Date now = new Date();
            if (voucher.getIsDelete() == 1) {
                response.put("valid", false);
                response.put("error", "Voucher đã bị xóa");
                return response;
            }
            
            if (now.before(voucher.getDateStart()) || now.after(voucher.getDateEnd())) {
                response.put("valid", false);
                response.put("error", "Voucher đã hết hạn hoặc chưa có hiệu lực");
                return response;
            }
            
            // Calculate discount for VoucherByPrice
            if (voucher instanceof VoucherByPrice) {
                VoucherByPrice vbp = (VoucherByPrice) voucher;
                
                if (subtotal < vbp.getMinOrderValue()) {
                    response.put("valid", false);
                    response.put("error", "Đơn hàng tối thiểu " + vbp.getMinOrderValue().intValue() + " VNĐ");
                    return response;
                }
                
                double discount = subtotal * (vbp.getDiscountPercent() / 100.0);
                if (vbp.getMaxDiscount() != null && discount > vbp.getMaxDiscount()) {
                    discount = vbp.getMaxDiscount();
                }
                
                response.put("valid", true);
                response.put("discount", discount);
                response.put("finalTotal", subtotal - discount);
                return response;
            }
            
            // For other voucher types, return basic validation
            response.put("valid", true);
            response.put("discount", 0.0);
            response.put("finalTotal", subtotal);
            
        } catch (Exception e) {
            response.put("valid", false);
            response.put("error", "Lỗi: " + e.getMessage());
        }
        
        return response;
    }
}
