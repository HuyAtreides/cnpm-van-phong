package org.office.controller;

import org.office.model.Voucher;
import org.office.model.VoucherByPrice;
import org.office.repository.VoucherRepository;
import org.office.repository.VoucherByPriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin/vouchers")
public class AdminVoucherController {

    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private VoucherByPriceRepository voucherByPriceRepository;

    @GetMapping
    public String listVouchers(Model model) {
        List<Voucher> vouchers = voucherRepository.findAll();
        model.addAttribute("vouchers", vouchers);
        return "admin/vouchers";
    }

    @PostMapping("/add")
    public String addVoucher(
            @RequestParam String code,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd,
            @RequestParam Double minOrderValue,
            @RequestParam Double discountPercent,
            @RequestParam(required = false) Double maxDiscount,
            RedirectAttributes redirectAttributes) {
        
        try {
            // Basic validation
            if (voucherRepository.findByCode(code).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Mã voucher đã tồn tại");
                return "redirect:/admin/vouchers";
            }

            VoucherByPrice voucher = new VoucherByPrice();
            voucher.setCode(code.toUpperCase());
            voucher.setDateStart(dateStart);
            voucher.setDateEnd(dateEnd);
            voucher.setIsDelete(0);
            
            voucher.setMinOrderValue(minOrderValue);
            voucher.setDiscountPercent(discountPercent);
            voucher.setMaxDiscount(maxDiscount);
            
            voucher.setDiscount(discountPercent);
            
            voucherByPriceRepository.save(voucher);
            
            redirectAttributes.addFlashAttribute("success", "Thêm voucher thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/vouchers";
    }

    @PostMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            Voucher voucher = voucherRepository.findById(id).orElse(null);
            if (voucher != null) {
                voucher.setIsDelete(1);
                voucherRepository.save(voucher);
                redirectAttributes.addFlashAttribute("success", "Xóa voucher thành công");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Voucher getVoucher(@PathVariable Integer id) {
        return voucherRepository.findById(id).orElse(null);
    }

    @PostMapping("/update")
    public String updateVoucher(
            @RequestParam Integer voucherId,
            @RequestParam String code,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateStart,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dateEnd,
            @RequestParam Double minOrderValue,
            @RequestParam Double discountPercent,
            @RequestParam(required = false) Double maxDiscount,
            RedirectAttributes redirectAttributes) {
        
        try {
            Voucher voucher = voucherRepository.findById(voucherId).orElse(null);
            if (voucher instanceof VoucherByPrice) {
                VoucherByPrice v = (VoucherByPrice) voucher;
                // v.setCode(code); // Code usually shouldn't change, or check duplicates if changed
                v.setDateStart(dateStart);
                v.setDateEnd(dateEnd);
                v.setMinOrderValue(minOrderValue);
                v.setDiscountPercent(discountPercent);
                v.setMaxDiscount(maxDiscount);
                v.setDiscount(discountPercent); // Sync base field
                
                voucherByPriceRepository.save(v);
                redirectAttributes.addFlashAttribute("success", "Cập nhật voucher thành công");
            } else {
                redirectAttributes.addFlashAttribute("error", "Loại voucher không hỗ trợ sửa (hoặc không tìm thấy)");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/vouchers";
    }
}
