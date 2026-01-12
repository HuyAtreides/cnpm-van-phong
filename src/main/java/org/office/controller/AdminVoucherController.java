package org.office.controller;

import org.office.model.Voucher;
import org.office.service.AdminVoucherService;
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
    private AdminVoucherService adminVoucherService;

    @GetMapping
    public String listVouchers(Model model) {
        List<Voucher> vouchers = adminVoucherService.getAllVouchers();
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
            adminVoucherService.addVoucher(code, dateStart, dateEnd, minOrderValue, discountPercent, maxDiscount);
            redirectAttributes.addFlashAttribute("success", "Thêm voucher thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/vouchers";
    }

    @PostMapping("/delete/{id}")
    public String deleteVoucher(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            adminVoucherService.deleteVoucher(id);
            redirectAttributes.addFlashAttribute("success", "Xóa voucher thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }
        return "redirect:/admin/vouchers";
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Voucher getVoucher(@PathVariable Integer id) {
        return adminVoucherService.getVoucherById(id).orElse(null);
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
            adminVoucherService.updateVoucher(voucherId, code, dateStart, dateEnd, minOrderValue, discountPercent, maxDiscount);
            redirectAttributes.addFlashAttribute("success", "Cập nhật voucher thành công");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + e.getMessage());
        }

        return "redirect:/admin/vouchers";
    }
}

