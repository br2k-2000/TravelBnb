package com.travelbnb.payload;

import com.travelbnb.Entity.Property;
import lombok.Data;

@Data
public class ImageDto {

    private Long id;
    private String imageUrl;
    private Property property;
}
