package org.office.repository;

import org.office.model.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findByApproval(Integer approval);

    // Blog đã duyệt
    Page<Blog> findByApproval(Integer approval, Pageable pageable);

    // Blog đã duyệt + tìm theo tiêu đề (LIKE)
    Page<Blog> findByApprovalAndBlogTitleContainingIgnoreCase(
            Integer approval,
            String blogTitle,
            Pageable pageable
    );

    // Blog đã duyệt + lọc theo ngày đăng
    Page<Blog> findByApprovalAndPostingDate(
            Integer approval,
            Date postingDate,
            Pageable pageable
    );

    // Blog đã duyệt + tìm theo tiêu đề + ngày đăng
    Page<Blog> findByApprovalAndBlogTitleContainingIgnoreCaseAndPostingDate(
            Integer approval,
            String blogTitle,
            Date postingDate,
            Pageable pageable
    );
}

