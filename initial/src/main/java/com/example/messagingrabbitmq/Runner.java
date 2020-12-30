package com.example.messagingrabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.concurrent.TimeUnit;

@Component
public class Runner implements CommandLineRunner {

    @Value("${spring.rabbitmq.send.topic}") private String topicExchangeName;
    @Value("${spring.rabbitmq.send.routingKey}") private String routingKey;

    @Value("${spring.rabbitmq.disable.queue}") private boolean disableRetrieve;

    @Value("${spring.rabbitmq.disable.send}") private boolean disableSend;

    @Value("${spring.rabbitmq.send.typeId}") private String typeId;

    @Value("${spring.rabbitmq.send.inputFile}") private String inputFile;

    private final Receiver receiver;
    private final RabbitTemplate rabbitTemplate;

    public Runner(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        BufferedReader reader = null;
        try {
            if (!disableSend) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"));
                String line;
                while ((line = reader.readLine()) != null) {
                    try {
                        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, line, m -> {
                            m.getMessageProperties().getHeaders().put("__TypeId__", typeId);
                            m.getMessageProperties().setContentType("application/json");
                            return m;
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!disableRetrieve) {
                receiver.getLatch().await(300000, TimeUnit.MILLISECONDS);
            }
        } finally {
            if(reader != null) {
                reader.close();
            }
        }
    }
}
