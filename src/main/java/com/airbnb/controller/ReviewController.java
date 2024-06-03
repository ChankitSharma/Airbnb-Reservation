package com.airbnb.controller;

import com.airbnb.entity.Property;
import com.airbnb.entity.PropertyUser;
import com.airbnb.entity.Review;
import com.airbnb.exception.ResourceNotFound;
import com.airbnb.payload.DeleteReviewRequest;
import com.airbnb.payload.ReviewDto;
import com.airbnb.repository.PropertyRepository;
import com.airbnb.repository.ReviewRepository;
import com.airbnb.service.UserService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PropertyRepository propertyRepository;

    @PostMapping ("/addReviews")   //Getting value from Nested JSON object
    public ResponseEntity<String> addReview(@Valid @RequestBody ReviewDto reviewDto, BindingResult bindingResult, @AuthenticationPrincipal PropertyUser user){
        if(bindingResult.hasErrors()) {
            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
        }
        long propertyId = reviewDto.getProperty().getId();
        Optional<Property> optionalPropertyID = propertyRepository.findById(propertyId);
        Property property = optionalPropertyID.orElseThrow(() -> new ResourceNotFound("Property not present!!"));

        Review reviewId = reviewRepository.findReviewByUserIdAndPropertyId(property, user);
        if(reviewId != null){
            return new ResponseEntity<>("You have added already a Review for this property!!",HttpStatus.BAD_REQUEST);
        }
        Review review = userService.addReview(propertyId, reviewDto, user);
        return new ResponseEntity<>("Review Added Successfully",HttpStatus.OK);
    }

//    @PostMapping("{propertyId}")//Getting value from URl
//    public ResponseEntity<String> addReview(@PathVariable long propertyId, @Valid @RequestBody ReviewDto reviewDto, BindingResult bindingResult, @AuthenticationPrincipal PropertyUser user){
//        if(bindingResult.hasErrors()) {
//            return new ResponseEntity<>(bindingResult.getFieldError().getDefaultMessage(),HttpStatus.BAD_REQUEST);
//        }
//        Optional<Property> optionalPropertyID = propertyRepository.findById(propertyId);
//        Property property = optionalPropertyID.orElseThrow(() -> new ResourceNotFound("Property not present!!"));
//
//        Review reviewId = reviewRepository.findReviewByUserIdAndPropertyId(property, user);
//        if(reviewId != null){
//            return new ResponseEntity<>("You have added already a Review for this property!!",HttpStatus.BAD_REQUEST);
//        }
//        Review review = userService.addReview(propertyId, reviewDto, user);
//        return new ResponseEntity<>("Review Added Successfully",HttpStatus.OK);
//    }
    @GetMapping("/getReviews")
    public ResponseEntity<List<Review>> getUserReviews(@AuthenticationPrincipal PropertyUser user) {
        List<Review> reviews = reviewRepository.findByPropertyUser(user);
        return new ResponseEntity<>(reviews,HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping
    public ResponseEntity<String> deleteUserReview(@RequestBody DeleteReviewRequest deleteReviewRequest, @AuthenticationPrincipal PropertyUser user) {
        long id = deleteReviewRequest.getReviewId();
        int deleted = reviewRepository.deleteReviewByIdAndUser(id, user);
        if (deleted > 0) {
            return new ResponseEntity<>("Review is deleted!!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Review is not present!!", HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

//@PostMapping("/{propertyId}")
//public ResponseEntity<String> addReview(@PathVariable long propertyId, @RequestBody Review review, @AuthenticationPrincipal PropertyUser user) {
//Optional<Property> optionalPropertyID = propertyRepository.findById(propertyId);
//Property property = optionalPropertyID.get();
//           review.setProperty(property);
//           review.setPropertyUser(user);
//           reviewRepository.save(review);
//    return new ResponseEntity<>("Review Added Successfully", HttpStatus.OK);
//    }