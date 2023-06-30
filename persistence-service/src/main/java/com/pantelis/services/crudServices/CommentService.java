package com.pantelis.services.crudServices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.pantelis.domain.Comment;

import java.util.List;

public interface CommentService {


    /**
     * Adds a new comment to the database, fetches all comments from the database and sends them as a message to Kafka topic my-topic-list.
     *
     * @param comment the Comment object to be added
     * @return the created Comment object
     */
    Comment createCommentAndUpdateList(Comment comment) throws JsonProcessingException;


    /**
     * Returns a list of all comments.
     *
     * @return a list of Comment objects representing all the comments
     */
    List<Comment> getAllComments();

}
