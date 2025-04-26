package com.travelbnb.kafka.consumer;

import com.travelbnb.kafka.payload.ReviewSubmittedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class ReviewEventConsumer {

    @KafkaListener(topics = "review-submitted-topic", groupId = "travelbnb-review-group")
    public void consumeReviewEvent(ReviewSubmittedEvent event) {
        System.out.println("📥 Received Review Event: " + event);
        // TODO: send notification or analytics logic here
    }
}