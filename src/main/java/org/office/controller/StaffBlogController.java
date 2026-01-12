package org.office.controller;

import org.office.model.Blog;
import org.office.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/staff/blogs")
public class StaffBlogController {

    @Autowired
    private BlogService blogService;

    // Hiển thị danh sách blog
    @GetMapping
    public String listBlogs(Model model) {
        model.addAttribute("blogs", blogService.getAllBlogs());
        return "staff/staff-blog-list";
    }

    // Form tạo blog mới
    @GetMapping("/new")
    public String newBlogForm(Model model) {
        Blog blog = new Blog();
        blog.setApproval(1);
        blog.setPostingDate(new Date());
        model.addAttribute("blog", blog);
        return "staff/staff-blog-form";
    }

    // Lưu blog (tạo mới hoặc cập nhật)
    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog) {
        if (blog.getPostingDate() == null) {
            blog.setPostingDate(new Date());
        }
        if (blog.getApproval() == null) {
            blog.setApproval(1);
        }

        if (blog.getBlogId() == null) {
            blogService.createBlog(blog);
        } else {
            blogService.updateBlog(blog.getBlogId(), blog);
        }

        return "redirect:/staff/blogs";
    }

    // Form chỉnh sửa blog
    @GetMapping("/{id}/edit")
    public String editBlog(@PathVariable Integer id, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found"));
        model.addAttribute("blog", blog);
        return "staff/staff-blog-form";
    }

    // Xóa blog
    @GetMapping("/{id}/delete")
    public String deleteBlog(@PathVariable Integer id) {
        blogService.deleteBlog(id);
        return "redirect:/staff/blogs";
    }
}
