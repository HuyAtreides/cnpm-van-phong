package org.office.controller;

import org.office.model.Product;
import org.office.model.ProductImage;
import org.office.model.Category;
import org.office.service.ProductService;
import org.office.service.FileUploadService;
import org.office.repository.ProductImageRepository;
import org.office.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.getAllActiveProducts(pageable);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        return "admin/products";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping("/create")
    public String createProduct(@ModelAttribute Product product, Model model) {
        try {
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                model.addAttribute("error", "Tên sản phẩm không được để trống");
                model.addAttribute("product", product);
                model.addAttribute("categories", categoryRepository.findAll());
                return "admin/product-form";
            }
            product.setIsDelete(0);
            productService.saveProduct(product);
            return "redirect:/admin/products?success";
        } catch (Exception e) {
            model.addAttribute("error", "Tạo sản phẩm thất bại: " + e.getMessage());
            model.addAttribute("product", product);
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/product-form";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Integer id, Model model) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isEmpty()) {
            return "redirect:/admin/products?error=notfound";
        }
        model.addAttribute("product", product.get());
        model.addAttribute("categories", categoryRepository.findAll());
        return "admin/product-form";
    }

    @PostMapping("/{id}/edit")
    public String updateProduct(@PathVariable Integer id, 
                               @ModelAttribute Product updatedProduct,
                               Model model) {
        try {
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isEmpty()) {
                return "redirect:/admin/products?error=notfound";
            }

            if (updatedProduct.getName() == null || updatedProduct.getName().trim().isEmpty()) {
                model.addAttribute("error", "Tên sản phẩm không được để trống");
                model.addAttribute("product", updatedProduct);
                model.addAttribute("categories", categoryRepository.findAll());
                return "admin/product-form";
            }

            Product product = existingProduct.get();
            product.setName(updatedProduct.getName());
            product.setDescript(updatedProduct.getDescript());
            product.setCategory(updatedProduct.getCategory());
            productService.saveProduct(product);
            return "redirect:/admin/products?success";
        } catch (Exception e) {
            model.addAttribute("error", "Cập nhật sản phẩm thất bại: " + e.getMessage());
            model.addAttribute("product", updatedProduct);
            model.addAttribute("categories", categoryRepository.findAll());
            return "admin/product-form";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return "redirect:/admin/products?deleted";
        } catch (Exception e) {
            return "redirect:/admin/products?error=delete";
        }
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

