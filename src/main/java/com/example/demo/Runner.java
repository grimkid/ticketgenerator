package com.example.demo;

import com.example.demo.service.RandomService;
import com.example.demo.service.StripService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final StripService stripService;
    private final RandomService randomService;
    @Override
    public void run(String... args) throws Exception {

//        for (String arg : args) {
//            System.out.println(arg);
//        }
        final int numberOfStrips = args.length > 0 ? Integer.parseInt(args[0]) : 1;
        randomService.hardInit(Math.min(numberOfStrips, 100));
        log.info(stripService.generateStrip().toString());
        log.info(String.valueOf(args.length));
        log.info("Leaving Runner");
    }
}
//./gradlew bootRun --args='--arg1=value1 --arg2=value2'