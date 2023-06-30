package com.pantelis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;


@SpringBootApplication
@ServletComponentScan
public class WebSocketConfigApp {
    public static void main(String[] args) {
        SpringApplication.run(WebSocketConfigApp.class, args);
    }

}