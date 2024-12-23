package com.example.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RandomService {
    public final List<List<Integer>> ticketNumbers = Collections.synchronizedList(new ArrayList<>());
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public RandomService() {
        handleLowTicketNumbers();
        startWatcher();
    }

    public void hardInit(Integer limit) {
        for (int i = 0; i < limit; i++) {
            ticketNumbers.add(generateNumbers());
        }
    }

    public List<Integer> getTicketNumbers() {
        log.info("Getting ticket numbers");
        Optional<List<Integer>> numberList =  ticketNumbers.stream().filter(integers -> integers.size() == 10)
                .findAny();
        return numberList.orElseGet(this::getTicketNumbers);
    }

    private void startWatcher() {
        scheduler.scheduleAtFixedRate(() -> {
            if (ticketNumbers.size() < 500) {
                handleLowTicketNumbers();
            }
        }, 0, 1, TimeUnit.SECONDS);
    }

    private static List<Integer> generateNumbers() {
        final List<Integer> numbers = new ArrayList<>(90);
        for (int i = 0; i <= 9; i++) {
            numbers.add(i);
        }
        Collections.shuffle(numbers);
        return numbers;
    }

    private void handleLowTicketNumbers() {
        Thread.ofVirtual().start(() -> {
            while(ticketNumbers.size() < 1000) {
                ticketNumbers.add(generateNumbers());
            }
        });
    }
}
