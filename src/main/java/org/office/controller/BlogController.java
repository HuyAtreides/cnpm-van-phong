package org.office.controller;

import org.office.model.Blog;
import org.office.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/blog")
public class BlogController {

    @Autowired
    private BlogService blogService;

    
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
    }
}

