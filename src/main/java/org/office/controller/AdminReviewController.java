package org.office.controller;

import org.office.model.Review;
import org.office.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping
    public String listReviews(Model model) {
        List<Review> reviews = reviewRepository.findAll(Sort.by(Sort.Direction.DESC, "createAt"));
        model.addAttribute("reviews", reviews);
        model.addAttribute("current", "reviews");
        return "admin/reviews";
    }
}
