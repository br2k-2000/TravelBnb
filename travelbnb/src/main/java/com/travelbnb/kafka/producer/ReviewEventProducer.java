package com.travelbnb.kafka.producer;

import com.travelbnb.kafka.payload.ReviewSubmittedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "review-submitted-topic";

    public void sendReviewEvent(ReviewSubmittedEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
