package com.travelbnb.Service;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.payload.ReviewsDto;

import java.util.List;

public interface ReviewService {

    ReviewsDto addReview(AppUser appUser, long propertyId, ReviewsDto reviewsDto);

    ReviewsDto updateReview(long reviewId, AppUser appUser, long propertyId, ReviewsDto reviewsDto);

    List<ReviewsDto> getUserReview(AppUser appUser);

    List<ReviewsDto> getAllReview(int pageSize, int pageNo, String sortBy, String sortDir);

    void deleteReview(AppUser appUser, long propertyId);
}
