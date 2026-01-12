package org.office.controller;

import org.office.model.Blog;
import org.office.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/content")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminContentController {

    @Autowired
    private BlogRepository blogRepository;

    @GetMapping
    public String listContent(Model model) {
        model.addAttribute("blogs", blogRepository.findAll());
        
        return "admin/content/list";
    }

    @GetMapping("/blogs/{id}/edit")
    public String editBlog(@PathVariable Integer id, Model model) {
        Blog blog = blogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        return "admin/content/form";
    }

    @PostMapping("/blogs/save")
    public String saveBlog(@ModelAttribute Blog blog) {
        if (blog.getBlogId() != null) {
            Blog existing = blogRepository.findById(blog.getBlogId())
                .orElseThrow(() -> new RuntimeException("Blog not found"));
            existing.setBlogTitle(blog.getBlogTitle());
            existing.setContent(blog.getContent());
            
            existing.setApproval(1); 
            
            if (existing.getPostingDate() == null) {
                existing.setPostingDate(new java.util.Date());
            }
            blogRepository.save(existing);
        }
        return "redirect:/admin/content";
    }

    @GetMapping("/blogs/{id}/approve")
    public String approveBlog(@PathVariable Integer id) {
        Blog blog = blogRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Blog not found"));
        blog.setApproval(1);
        blogRepository.save(blog);
        return "redirect:/admin/content";
    }

    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Integer id) {
        blogRepository.deleteById(id);
        return "redirect:/admin/content";
    }
}

