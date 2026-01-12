package org.office.controller;

import org.office.model.Product;
import org.office.model.Category;
import org.office.model.User;
import org.office.repository.CustomerRepository;
import org.office.service.ProductService;
import org.office.repository.CategoryRepository;
import org.office.service.UserService;
import org.office.service.WishlistService;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private WishlistService wishlistService;

    @GetMapping("/products")
    public String listProducts(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) Integer categoryId,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model, Authentication authentication) {
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

        /* ===== WISHLIST LOGIC ===== */
        if (authentication != null) {
            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);

            if (user != null && customerRepository.existsById(user.getUserId())) {
                List<Integer> wishlistProductIds =
                        wishlistService.getWishlistProductIds(user.getUserId());
                model.addAttribute("wishlistProductIds", wishlistProductIds);
            }
        }

        return "products";
    }

    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model, Authentication authentication) {
        Product product = productService.getProductById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

        boolean inWishlist = false;

        // Nếu đã login → kiểm tra wishlist
        if (authentication != null) {
            String email = authentication.getName();
            User user = userService.findByEmail(email).orElse(null);

            if (user != null && customerRepository.existsById(user.getUserId())) {
                inWishlist = wishlistService
                        .isProductInWishlist(user.getUserId(), product.getProductId());
            }
        }

        model.addAttribute("product", product);
        model.addAttribute("relatedProducts", productService.getRelatedProducts(id));
        model.addAttribute("reviews", productService.getProductReviews(id));
        model.addAttribute("inWishlist", inWishlist);
        return "product-detail";
    }
}

