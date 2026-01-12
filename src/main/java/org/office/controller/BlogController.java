package org.office.controller;

import org.office.model.Blog;
import org.office.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    // 📌 Đọc blog + tìm kiếm + phân trang
    @GetMapping
    public String listBlogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "yyyy-MM-dd") Date postingDate,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Blog> blogPage =
                blogService.searchApprovedBlogs(title, postingDate, pageable);

        model.addAttribute("blogPage", blogPage);
        model.addAttribute("blogs", blogPage.getContent());

        model.addAttribute("currentPage", page + 1);     // UI
        model.addAttribute("totalPages", blogPage.getTotalPages());

        model.addAttribute("title", title);
        model.addAttribute("postingDate", postingDate);

        return "blog-list";
    }

    @GetMapping("/{id}")
    public String viewBlog(
            @PathVariable Integer id,
            Model model
    ) {
        // E1 – Blog không tồn tại
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Bài blog không tồn tại hoặc đã bị xóa"
                        )
                );

        // E2 – Blog chưa được duyệt
        if (blog.getApproval() == null || blog.getApproval() != 1) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Bài blog này chưa được duyệt"
            );
        }

        model.addAttribute("blog", blog);
        return "blog-detail";
    }

    /*
    @GetMapping
    public String listBlogs(Model model) {
        List<Blog> blogs = blogService.getApprovedBlogs();
        model.addAttribute("blogs", blogs);
        return "blog-list";
    }

    
    @GetMapping("/{id}")
    public String viewBlog(@PathVariable Integer id, Model model) {
        Blog blog = blogService.getBlogById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        return "blog-detail";
    } */
}

