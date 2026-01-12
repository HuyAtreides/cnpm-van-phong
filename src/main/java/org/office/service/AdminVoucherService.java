package org.office.service;

import org.office.model.Voucher;
import org.office.model.VoucherByPrice;
import org.office.repository.VoucherRepository;
import org.office.repository.VoucherByPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AdminVoucherService {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherByPriceRepository voucherByPriceRepository;

    public List<Voucher> getAllVouchers() {
        return voucherRepository.findAll();
    }

    public Optional<Voucher> getVoucherById(Integer id) {
        return voucherRepository.findById(id);
    }

    public Optional<Voucher> findByCode(String code) {
        return voucherRepository.findByCode(code);
    }

    public void addVoucher(String code, Date dateStart, Date dateEnd, Double minOrderValue, 
                          Double discountPercent, Double maxDiscount) {
        // Check if code already exists
        if (voucherRepository.findByCode(code).isPresent()) {
            throw new RuntimeException("Mã voucher đã tồn tại");
        }

        VoucherByPrice voucher = new VoucherByPrice();
        voucher.setCode(code.toUpperCase());
        voucher.setDateStart(dateStart);
        voucher.setDateEnd(dateEnd);
        voucher.setIsDelete(0);

        // Subclass fields
        voucher.setMinOrderValue(minOrderValue);
        voucher.setDiscountPercent(discountPercent);
        voucher.setMaxDiscount(maxDiscount);

        // Base discount field
        voucher.setDiscount(discountPercent);

        voucherByPriceRepository.save(voucher);
    }

    public void deleteVoucher(Integer id) {
        Optional<Voucher> voucherOpt = voucherRepository.findById(id);
        if (voucherOpt.isPresent()) {
            Voucher voucher = voucherOpt.get();
            voucher.setIsDelete(1); // Soft delete
            voucherRepository.save(voucher);
        }
    }

    public void updateVoucher(Integer voucherId, String code, Date dateStart, Date dateEnd, 
                             Double minOrderValue, Double discountPercent, Double maxDiscount) {
        Optional<Voucher> voucherOpt = voucherRepository.findById(voucherId);
        if (voucherOpt.isPresent() && voucherOpt.get() instanceof VoucherByPrice) {
            VoucherByPrice v = (VoucherByPrice) voucherOpt.get();
            v.setDateStart(dateStart);
            v.setDateEnd(dateEnd);
            v.setMinOrderValue(minOrderValue);
            v.setDiscountPercent(discountPercent);
            v.setMaxDiscount(maxDiscount);
            v.setDiscount(discountPercent); // Sync base field

            voucherByPriceRepository.save(v);
        } else {
            throw new RuntimeException("Loại voucher không hỗ trợ sửa (hoặc không tìm thấy)");
        }
    }
}
