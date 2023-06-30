package com.pantelis.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.pantelis.domain.Comment;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@SpringBootTest(webEnvironment = RANDOM_PORT)
@SpringJUnitConfig
@Slf4j
class CommentControllerTest {

    private WebSocketStompClient webSocketStompClientSend;
    private WebSocketStompClient webSocketStompClientListen;
    @LocalServerPort
    private int port;


    @BeforeEach
    void setup() {

        this.webSocketStompClientSend = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
        this.webSocketStompClientListen = new WebSocketStompClient(new SockJsClient(
                List.of(new WebSocketTransport(new StandardWebSocketClient()))));
    }


    @Test
    @Tag("integration")
    void IntegrationCommentAppTest() throws ExecutionException, InterruptedException, TimeoutException {
        //To run this test first run both WebSocketConfigApp and PersistenceServiceApp with test profile.

        String[] prefixes = {"Mike", "John", "Sarah", "Alex", "Emily"};
        String[] suffixes = {"Smith", "Johnson", "Williams", "Jones", "Brown"};

        Random random = new Random();
        String prefix = prefixes[random.nextInt(prefixes.length)];
        String suffix = suffixes[random.nextInt(suffixes.length)];
        int randomNumber = random.nextInt(1000);
        String name = prefix + suffix + randomNumber;

        final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

        webSocketStompClientSend.setMessageConverter(new MappingJackson2MessageConverter());
        webSocketStompClientListen.setMessageConverter(new StringMessageConverter());

        String WEBSOCKET_URI = "ws://localhost:8080/comments";
        StompSession session = webSocketStompClientSend
                .connect(String.format(WEBSOCKET_URI, port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        StompSession sessionListen = webSocketStompClientListen
                .connect(String.format(WEBSOCKET_URI, port), new StompSessionHandlerAdapter() {
                })
                .get(1, SECONDS);

        String WEBSOCKET_TOPIC = "/topic/comments";
        sessionListen.subscribe(WEBSOCKET_TOPIC, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                messageQueue.offer((String) payload);
            }
        });

        Comment comment = new Comment();
        comment.setText(name);
        String WEBSOCKET_ENDPOINT = "/addComment";
        session.send(WEBSOCKET_ENDPOINT, comment);

        String receivedMessage = messageQueue.poll(5, TimeUnit.SECONDS);

        Gson gson = new Gson();
        Type listType = new TypeToken<List<String>>() {
        }.getType();
        List<String> stringList = gson.fromJson(receivedMessage, listType);
        Assertions.assertTrue(stringList.contains(name));
    }
}