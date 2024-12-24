package com.example.demo2;

import com.example.demo2.model.Ticket;
import com.example.demo2.service.BingoTicketGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class RunnerTest {

    private BingoTicketGenerator bingoTicketGenerator;
    private Runner runner;

    @BeforeEach
    void setUp() {
        bingoTicketGenerator = mock(BingoTicketGenerator.class);
        runner = new Runner(bingoTicketGenerator);
    }

    @Test
    void testRun() throws Exception {
        Ticket[] tickets = new Ticket[6];
        for (int i = 0; i < 6; i++) {
            tickets[i] = new Ticket();
        }

        when(BingoTicketGenerator.generateStrip()).thenReturn(tickets);

        runner.run();

        verify(bingoTicketGenerator, times(1)).generateStrip();
        verify(bingoTicketGenerator, times(1)).printStrip(tickets);
    }
}