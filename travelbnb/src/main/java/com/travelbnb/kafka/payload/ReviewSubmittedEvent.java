package com.travelbnb.kafka.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReviewSubmittedEvent {

    private Long reviewId;
    private Long propertyId;
    private Long userId;
    private String comment;
    private Integer rating;
}
