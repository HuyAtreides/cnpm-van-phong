package org.office.service;

import org.office.model.Blog;
import org.office.repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    public Page<Blog> searchApprovedBlogs(
            String title,
            Date postingDate,
            Pageable pageable
    ) {
        // chỉ blog đã duyệt
        Integer approval = 1;

        if ((title == null || title.isBlank()) && postingDate == null) {
            return blogRepository.findByApproval(approval, pageable);
        }

        if (title != null && !title.isBlank() && postingDate != null) {
            return blogRepository
                    .findByApprovalAndBlogTitleContainingIgnoreCaseAndPostingDate(
                            approval, title, postingDate, pageable
                    );
        }

        if (title != null && !title.isBlank()) {
            return blogRepository
                    .findByApprovalAndBlogTitleContainingIgnoreCase(
                            approval, title, pageable
                    );
        }

        return blogRepository
                .findByApprovalAndPostingDate(approval, postingDate, pageable);
    }
    
    public List<Blog> getApprovedBlogs() {
        return blogRepository.findByApproval(1);
    }

    
    public List<Blog> getAllBlogs() {
        return blogRepository.findAll();
    }

    
    public Optional<Blog> getBlogById(Integer blogId) {
        return blogRepository.findById(blogId);
    }

    
    public void approveBlog(Integer blogId) {
        Optional<Blog> blogOpt = blogRepository.findById(blogId);
        
        if (blogOpt.isEmpty()) {
            throw new RuntimeException("Blog not found");
        }

        Blog blog = blogOpt.get();
        blog.setApproval(1);
        blogRepository.save(blog);
    }

    
    public void rejectBlog(Integer blogId) {
        Optional<Blog> blogOpt = blogRepository.findById(blogId);
        
        if (blogOpt.isEmpty()) {
            throw new RuntimeException("Blog not found");
        }

        Blog blog = blogOpt.get();
        blog.setApproval(0);
        blogRepository.save(blog);
    }

    
    public Blog createBlog(Blog blog) {
        blog.setApproval(0); 
        return blogRepository.save(blog);
    }

    
    public Blog updateBlog(Integer blogId, Blog updatedBlog) {
        Optional<Blog> blogOpt = blogRepository.findById(blogId);
        
        if (blogOpt.isEmpty()) {
            throw new RuntimeException("Blog not found");
        }

        Blog blog = blogOpt.get();
        blog.setBlogTitle(updatedBlog.getBlogTitle());
        blog.setContent(updatedBlog.getContent());
        
        return blogRepository.save(blog);
    }

    
    public void deleteBlog(Integer blogId) {
        blogRepository.deleteById(blogId);
    }
}

