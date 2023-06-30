package com.pantelis.services.crudServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pantelis.domain.Comment;
import com.pantelis.repository.CommentRepository;
import com.pantelis.services.kafkaServices.KafkaProducerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    private final KafkaProducerService kafkaProducerService;

    private final ObjectMapper objectMapper;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, KafkaProducerService kafkaProducerService, ObjectMapper objectMapper) {
        this.commentRepository = commentRepository;
        this.kafkaProducerService = kafkaProducerService;
        this.objectMapper = objectMapper;
    }

    /**
     * Adds a new comment to the database, fetches all comments from the database and sends them to Kafka topic my-topic-list.
     *
     * @param comment The comment to be added to the list and saved in the database.
     * @return The comment.
     * @throws JsonProcessingException if there is an error processing the JSON content.
     */
    public Comment createCommentAndUpdateList(Comment comment) throws JsonProcessingException {

        Comment commentDb = commentRepository.save(comment);
        List<String> listComments = commentRepository.findAll().stream()
                .map(Comment::getContent)
                .toList();
        String jsonString = objectMapper.writeValueAsString(listComments);
        kafkaProducerService.sendMessage("my-topic-list", jsonString);
        log.debug("Message "+jsonString+" was send to my-topic-list");
        return commentDb;
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }

}
