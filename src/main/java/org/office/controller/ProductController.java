package org.office.controller;

import org.office.model.Product;
import org.office.model.Category;
import org.office.service.ProductService;
import org.office.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Integer categoryId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        
        
        if (page < 0) page = 0;
        
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("productId").descending());
        
        Page<Product> productPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            productPage = productService.searchProducts(keyword, pageable);
        } else if (categoryId != null) {
            productPage = productService.getProductsByCategory(categoryId, pageable);
        } else {
            productPage = productService.getAllActiveProducts(pageable);
        }

        List<Category> categories = categoryRepository.findAll();
        
        model.addAttribute("productPage", productPage);
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("keyword", keyword);
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        
        return "products";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));
        
        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.getRelatedProducts(id));
        model.addAttribute("reviews", productService.getProductReviews(id));
        return "product-detail";
    }
}

