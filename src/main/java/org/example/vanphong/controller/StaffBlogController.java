package org.example.vanphong.controller;

import org.example.vanphong.model.Blog;
import org.example.vanphong.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/staff/blogs")
public class StaffBlogController {

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        return "staff/staff-blog-list";
    }

    @GetMapping("/new")
    public String newBlogForm(Model model) {
        Blog blog = new Blog();
        blog.setApproval(1); 
        model.addAttribute("blog", blog);
        return "staff/staff-blog-form";
    }

    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog) {
        System.out.println("=== BLOG SAVE DEBUG ===");
        System.out.println("Blog ID: " + blog.getBlogId());
        System.out.println("Blog Title: " + blog.getBlogTitle());
        System.out.println("Blog Content: " + (blog.getContent() != null ? blog.getContent().substring(0, Math.min(50, blog.getContent().length())) : "null"));
        System.out.println("Posting Date: " + blog.getPostingDate());
        System.out.println("Approval: " + blog.getApproval());
        
        if (blog.getPostingDate() == null) {
            blog.setPostingDate(new Date());
        }
        if (blog.getApproval() == null) {
            blog.setApproval(1);
        }
        
        try {
            Blog saved = blogRepository.save(blog);
            System.out.println("Blog saved successfully with ID: " + saved.getBlogId());
        } catch (Exception e) {
            System.err.println("Error saving blog: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        
        return "redirect:/staff/blogs";
    }

    @GetMapping("/{id}/edit")
    public String editBlog(@PathVariable Integer id, Model model) {
        model.addAttribute("blog", blogRepository.findById(id).orElseThrow());
        return "staff/staff-blog-form";
    }

    @GetMapping("/{id}/delete")
    public String deleteBlog(@PathVariable Integer id) {
        blogRepository.deleteById(id);
        return "redirect:/staff/blogs";
    }
}

