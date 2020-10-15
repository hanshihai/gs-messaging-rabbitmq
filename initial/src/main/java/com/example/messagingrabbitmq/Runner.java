package com.example.messagingrabbitmq;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class Runner implements CommandLineRunner {

    private final Receiver receiver;

    public Runner(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void run(String... args) throws Exception {
        receiver.getLatch().await(300000, TimeUnit.MILLISECONDS);
    }
}
