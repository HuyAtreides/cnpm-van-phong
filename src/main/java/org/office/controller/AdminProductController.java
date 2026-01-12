package org.office.controller;

import org.office.model.Product;
import org.office.model.ProductImage;
import org.office.service.ProductService;
import org.office.service.FileUploadService;
import org.office.repository.ProductImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ProductImageRepository productImageRepository;

    @GetMapping
    public String listProducts(Model model) {
        List<Product> products = productService.getAllActiveProducts();
        model.addAttribute("products", products);
        return "admin/products";
    }

    @GetMapping("/{id}/images")
    public String manageImages(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        model.addAttribute("product", product);
        return "admin/product-images";
    }

    @PostMapping("/{id}/images/upload")
    public String uploadImage(@PathVariable Integer id, 
                             @RequestParam("file") MultipartFile file,
                             Model model) {
        try {
            Product product = productService.getProductById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

            
            String filePath = fileUploadService.uploadFile(file);

            
            ProductImage productImage = new ProductImage();
            productImage.setProduct(product);
            productImage.setProductImage(filePath);
            productImageRepository.save(productImage);

            return "redirect:/admin/products/" + id + "/images?success";
        } catch (Exception e) {
            model.addAttribute("error", "Upload failed: " + e.getMessage());
            return "redirect:/admin/products/" + id + "/images?error";
        }
    }

    @PostMapping("/images/{imageId}/delete")
    public String deleteImage(@PathVariable Integer imageId, 
                             @RequestParam Integer productId) {
        try {
            ProductImage image = productImageRepository.findById(imageId)
                .orElseThrow(() -> new RuntimeException("Image not found"));
            
            
            fileUploadService.deleteFile(image.getProductImage());
            
            
            productImageRepository.delete(image);
            
            return "redirect:/admin/products/" + productId + "/images?deleted";
        } catch (Exception e) {
            return "redirect:/admin/products/" + productId + "/images?error";
        }
    }
}

