package com.pantelis.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumerService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    /**
     * Listens to messages from the "my-topic-list" Kafka topic and processes them.
     * Sends the received message to a WebSocket destination.
     *
     * @param message The message received from the Kafka topic.
     * @return The same message that was received.
     */
    @KafkaListener(topics = "my-topic-list", groupId = "my-group")
    public String receiveMessage(String message) {
        log.debug("Received message from my-topic-list: " + message);
        messagingTemplate.convertAndSend("/topic/comments", message);
        return message;
    }
}