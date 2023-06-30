package com.pantelis.services.kafkaServices;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.pantelis.domain.Comment;
import com.pantelis.services.crudServices.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
@Slf4j
public class KafkaConsumerService {

    @Autowired
    CommentService commentService;

    /**
     * Listens to messages from the "my-topic" Kafka topic and processes them.
     * Creates a Comment object from the received message and updates the list of comments.
     *
     * @param message The message received from the Kafka topic.
     * @throws JsonProcessingException if there is an error processing the JSON content.
     */
    @KafkaListener(topics = "my-topic", groupId = "my-group")
    public void receiveMessage(String message) throws JsonProcessingException {
        log.debug("Message received from my-topic: " + message);
        Comment comment = new Comment();
        comment.setContent(message);
        commentService.createCommentAndUpdateList(comment);
    }
}