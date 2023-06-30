package com.pantelis.controllers;


import com.pantelis.domain.Comment;
import com.pantelis.services.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CommentController {


    private final KafkaProducerService kafkaProducerService;

    public CommentController(KafkaProducerService kafkaProducerService) {
        this.kafkaProducerService = kafkaProducerService;
    }

    /**
     * Handles the WebSocket message for adding a comment.
     *
     * @param comment The Comment object received as the payload of the WebSocket message.
     */
    @MessageMapping("/addComment")
    public void addComment(@Payload Comment comment) {

        log.debug("Received comment: " + comment);
        kafkaProducerService.sendMessage("my-topic", comment.getText());
    }

}
