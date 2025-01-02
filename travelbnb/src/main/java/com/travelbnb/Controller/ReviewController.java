package com.travelbnb.Controller;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Service.ReviewService;
import com.travelbnb.payload.ReviewsDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/addReview")
    public ResponseEntity<ReviewsDto> addReview(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam long propertyId,
            @RequestBody ReviewsDto reviewsDto
    ){
        ReviewsDto dto = reviewService.addReview(appUser, propertyId, reviewsDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    //here through  @AuthenticationPrincipal userId is taken
    //or alternate ,in other ways we directly supply userId through @RequestParam
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewsDto> updateReview(
            @PathVariable long reviewId,
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam long propertyId,
            @RequestBody ReviewsDto reviewsDto
    ){
        ReviewsDto dto = reviewService.updateReview(reviewId, appUser, propertyId, reviewsDto);
        return new ResponseEntity<>(dto,HttpStatus.OK);
    }
    @GetMapping("/getReviewByUser")
    public ResponseEntity<List<ReviewsDto>> getUserReview(
            @AuthenticationPrincipal AppUser appUser
    ){
        List<ReviewsDto> userReview = reviewService.getUserReview(appUser);
        return new ResponseEntity<>(userReview,HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<ReviewsDto>> getAllReview(
            @RequestParam(name="pageSize",defaultValue = "5",required = false) int pageSize,
            @RequestParam(name="pageNo",defaultValue = "0",required = false) int pageNo,
            @RequestParam(name="sortBy",defaultValue = "id",required = false) String sortBy,
            @RequestParam(name="sortDir",defaultValue = "id",required = false) String sortDir
    ){
        List<ReviewsDto> allReview = reviewService.getAllReview(pageSize, pageNo, sortBy, sortDir);
        return new ResponseEntity<>(allReview,HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<String> deleteReview(
            @AuthenticationPrincipal AppUser appUser,
            @RequestParam long propertyId
    ){
        reviewService.deleteReview(appUser, propertyId);
        return new ResponseEntity<>("Record is deleted",HttpStatus.OK);
    }
}
