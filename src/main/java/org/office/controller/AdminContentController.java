package org.office.controller;

import org.office.model.Blog;
import org.office.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/admin/content")
@PreAuthorize("hasRole('ADMINISTRATOR')")
public class AdminContentController {

    @Autowired
    private BlogService blogService;

    // Hiển thị tất cả blog
    @GetMapping
    public String listContent(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "admin/content/list";
    }

    // Form chỉnh sửa blog
    @GetMapping("/blogs/{id}/edit")
    public String editBlog(@PathVariable Integer id, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        return "admin/content/form";
    }

    // Lưu blog (cập nhật)
    @PostMapping("/blogs/save")
    public String saveBlog(@ModelAttribute Blog blog) {
        if (blog.getBlogId() != null) {
            // Cập nhật blog hiện có
            Blog updatedBlog = blogService.updateBlog(blog.getBlogId(), blog);

            // Đảm bảo blog được approve
            if (updatedBlog.getApproval() == null || updatedBlog.getApproval() != 1) {
                updatedBlog.setApproval(1);
                if (updatedBlog.getPostingDate() == null) {
                    updatedBlog.setPostingDate(new Date());
                }
                blogService.updateBlog(updatedBlog.getBlogId(), updatedBlog);
            }
        }
        return "redirect:/admin/content";
    }

    // Approve blog
    @GetMapping("/blogs/{id}/approve")
    public String approveBlog(@PathVariable Integer id) {
        blogService.approveBlog(id);
        return "redirect:/admin/content";
    }

    // Xóa blog
    @GetMapping("/blogs/{id}/delete")
    public String deleteBlog(@PathVariable Integer id) {
        blogService.deleteBlog(id);
        return "redirect:/admin/content";
    }
}
