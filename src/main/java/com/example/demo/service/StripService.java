package com.example.demo.service;

import com.example.demo.model.RowModel;
import com.example.demo.model.StripModel;
import com.example.demo.model.TicketModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripService {
    private final TicketService ticketService;
    private final RandomService randomService;
    public StripModel generateStrip() {
        log.info("Generating strip");
        final StripModel strip = generateSkeletonStrip();
        for (int i = 0; i < 9; i++) {//iterate through cols
            final List<Integer> numbers = randomService.getTicketNumbers();
            if (i == 0) numbers.remove(Integer.valueOf(0));//first col does not have 0

            //iterate through tickets
            for (int j = 0; j < 6; j++) {
                log.info("Iterating through tickets to add numbers to strip");
                log.info("Current col: " + i + " Current ticket: " + j);

                final List<RowModel> rows = strip.getTickets().get(i).getRows();
                List<Integer>emptySeatsRow1 = rows.get(0).getEmptySeats();
                List<Integer>emptySeatsRow2 = rows.get(1).getEmptySeats();
                List<Integer>emptySeatsRow3 = rows.get(2).getEmptySeats();
                log.info("Current empty seats in row 1: " + emptySeatsRow1);
                log.info("Current empty seats in row 2: " + emptySeatsRow2);
                log.info("Current empty seats in row 3: " + emptySeatsRow3);
                final List<Integer> currentColNumbers = new ArrayList<>();
                if (!emptySeatsRow1.contains(Integer.valueOf(i))) {
                    currentColNumbers.add(numbers.removeFirst());
                }
                if (!emptySeatsRow2.contains(Integer.valueOf(i))) {
                    currentColNumbers.add(numbers.removeFirst());
                }
                if (!emptySeatsRow3.contains(Integer.valueOf(i))) {
                    currentColNumbers.add(numbers.removeFirst());
                }
                Collections.sort(currentColNumbers);
                for (int k = 0; k < 3; k++) {
                    strip.getTickets().get(i).getRows().get(k).getNumberSeats().add(currentColNumbers.get(k));
                }
            }
        }
        return strip;
    }

    public StripModel generateSkeletonStrip () {
        StripModel strip = new StripModel();
        for (int i = 0; i < 6; i++) {
            strip.getTickets().add(ticketService.generateTicket(strip.getTickets()));
        }
        while (!validateSkeletonStrip(strip)) {
            strip =  generateSkeletonStrip();
        }
        return strip;
    }

    private boolean validateSkeletonStrip(StripModel stripModel) {
        Map<Integer, Integer> emptySeatsPerCol = new HashMap<>();
        for(int i=0; i<6; i++) {
            for(int j=0; j<3; j++) {
                for(int k=0; k<4; k++){
                    final int currentNumber = stripModel.getTickets().get(i).getRows().get(j).getEmptySeats().get(k);
                    if (emptySeatsPerCol.containsKey(currentNumber)) {
                        if (emptySeatsPerCol.get(currentNumber) > 9) {
                            return false;
                        }
                        emptySeatsPerCol.put(currentNumber, emptySeatsPerCol.get(currentNumber) + 1);
                    } else {
                        emptySeatsPerCol.put(currentNumber, 1);
                    }
                }
            }
        }
        final AtomicBoolean result = new AtomicBoolean(true);
        emptySeatsPerCol.forEach((integer, integer2) -> {
            if (integer2 != 9) {
                result.set(false);
            }
        });
        return result.get();
    }
}
