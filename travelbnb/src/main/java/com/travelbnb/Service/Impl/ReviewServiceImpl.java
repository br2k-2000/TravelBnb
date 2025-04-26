package com.travelbnb.Service.Impl;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Property;
import com.travelbnb.Entity.Reviews;
import com.travelbnb.Exception.ResourceNotFoundException;
import com.travelbnb.Service.ReviewService;
import com.travelbnb.kafka.payload.ReviewSubmittedEvent;
import com.travelbnb.kafka.producer.ReviewEventProducer;
import com.travelbnb.payload.ReviewsDto;
import com.travelbnb.repository.PropertyRepository;
import com.travelbnb.repository.ReviewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {

    private PropertyRepository propertyRepository;
    private ReviewsRepository reviewsRepository;

    public ReviewServiceImpl(PropertyRepository propertyRepository, ReviewsRepository reviewsRepository) {
        this.propertyRepository = propertyRepository;
        this.reviewsRepository = reviewsRepository;
    }
    @Autowired
    private ReviewEventProducer reviewEventProducer;

    @Override
    public ReviewsDto addReview(AppUser appUser, long propertyId, ReviewsDto reviewsDto) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(
                () -> new ResourceNotFoundException("Property not found with id: " + propertyId)
        );
        if (reviewsRepository.findReviewsByUser(appUser, property) != null) {
            throw new ResourceNotFoundException("Already review exist");
        } else {
            reviewsDto.setProperty(property);
            reviewsDto.setAppUser(appUser);
            if (reviewsDto.getRating() <= 5) {
                Reviews reviews = dtoToEntity(reviewsDto);
                Reviews save = reviewsRepository.save(reviews);
                ReviewsDto dto = entityToDto(save);
                // ✅ Send Kafka event here
                ReviewSubmittedEvent event = new ReviewSubmittedEvent(
                        save.getId(),
                        property.getId(),
                        appUser.getId(),
                        save.getDescription(),
                        save.getRating()
                );
                reviewEventProducer.sendReviewEvent(event);
                return dto;
            } else {
                throw new ResourceNotFoundException("Rating is greater than 5 ");
            }
        }

    }

    @Override
    public ReviewsDto updateReview(long reviewId, AppUser appUser, long propertyId, ReviewsDto reviewsDto) {
        Reviews reviews=null;
        reviews = reviewsRepository.findById(reviewId).orElseThrow(
                () -> new ResourceNotFoundException("Review not found with id: " + reviewId)
        );
        reviewsDto.setAppUser(appUser);

        Property property = propertyRepository.findById(propertyId).orElseThrow(
                () -> new ResourceNotFoundException("Property not found with id: " + propertyId)
        );
        reviewsDto.setProperty(property);

        reviews = dtoToEntity(reviewsDto);
        reviews.setId(reviewId);

        Reviews save = reviewsRepository.save(reviews);
        ReviewsDto dto = entityToDto(save);
        return dto;
    }


    @Override
    public List<ReviewsDto> getUserReview(AppUser appUser) {
        List<Reviews> byUserReviews = reviewsRepository.reviewsByUser(appUser);
        List<ReviewsDto> collect = byUserReviews.stream().map(e -> entityToDto(e)).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<ReviewsDto> getAllReview(int pageSize, int pageNo, String sortBy, String sortDir) {
        PageRequest pageable = null;
        if (sortDir.equalsIgnoreCase("asc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        } else if (sortDir.equalsIgnoreCase("desc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).descending());
        }else {
            pageable= PageRequest.of(pageNo,pageSize);
        }

        Page<Reviews> all = reviewsRepository.findAll(pageable);
        List<Reviews> content = all.getContent();
        List<ReviewsDto> collect = content.stream().map(e -> entityToDto(e)).collect(Collectors.toList());
        return collect;

    }

    @Override
    public void deleteReview(AppUser appUser, long propertyId) {
        Property property = propertyRepository.findById(propertyId).orElseThrow(
                ()->new ResourceNotFoundException("Property not found with id: "+propertyId)
        );
        Optional<Reviews> reviews = reviewsRepository.findReviewsByUserAndProperty(appUser, property);
        if (reviews.isPresent()){
            reviewsRepository.delete(reviews.get());
        }else {
            throw new ResourceNotFoundException("Review not found for the given user and property");
        }
    }


    //dto to entity
    Reviews dtoToEntity(ReviewsDto reviewsDto) {
        Reviews entity = new Reviews();
        entity.setRating(reviewsDto.getRating());
        entity.setDescription(reviewsDto.getDescription());
        entity.setAppUser(reviewsDto.getAppUser());
        entity.setProperty(reviewsDto.getProperty());

        return entity;
    }
    //entity to dto

    ReviewsDto entityToDto(Reviews reviews) {
        ReviewsDto dto = new ReviewsDto();
        dto.setId(reviews.getId());
        dto.setRating(reviews.getRating());
        dto.setDescription(reviews.getDescription());
        dto.setAppUser(reviews.getAppUser());
        dto.setProperty(reviews.getProperty());
        return dto;
    }
}
