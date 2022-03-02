package com.cev.prueba.prueba.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cev.prueba.prueba.domain.Review;
import com.cev.prueba.prueba.repository.ReviewRepository;

@RestController
public class ReviewController {
	
	ReviewRepository reviewRepository;

	
	public ReviewController(ReviewRepository reviewRepository) {
	this.reviewRepository = reviewRepository;
	}

	@GetMapping(path="/review")
	List<Review> getAll(){
		return reviewRepository.findAll();
	}
	
	@PostMapping(path="/review")
	Review altaReview(@RequestBody Review review) {
		return reviewRepository.save(review);
	}
	

}
