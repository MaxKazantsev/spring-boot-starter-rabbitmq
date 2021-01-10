package com.github.maxkazantsev.rabbitmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RabbitConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitConsumerApplication.class, args);
    }

}
