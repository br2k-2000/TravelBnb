package com.travelbnb.payload;

import com.travelbnb.Entity.AppUser;
import com.travelbnb.Entity.Property;
import lombok.Data;

@Data
public class ReviewsDto {
    private Long id;
    private Integer rating;
    private String description;
    private AppUser appUser;
    private Property property;
}
