package com.example.demo2;

import com.example.demo2.model.Ticket;
import com.example.demo2.service.BingoTicketGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import static com.example.demo2.service.BingoTicketGenerator.generateStrip;
import static com.example.demo2.service.BingoTicketGenerator.printStrip;

@Component
@Slf4j
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final BingoTicketGenerator bingoTicketGenerator;
    @Override
    public void run(String... args) {

        Ticket[] strip = generateStrip();
        printStrip(strip);
        log.info(String.valueOf(args.length));
        log.info("Leaving Runner");
    }
}