package com.example.demo.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomServiceTest {

    private RandomService randomService;

    @BeforeEach
    void setUp() {
        randomService = new RandomService();
    }

    @Test
    void testHardInit() {
        randomService.hardInit(10);
        assertEquals(10, randomService.ticketNumbers.size(), "Should initialize with 10 ticket numbers");
    }

    @Test
    void testGetTicketNumbers() {
        randomService.hardInit(10);
        List<Integer> ticketNumbers = randomService.getTicketNumbers();
        assertNotNull(ticketNumbers, "Ticket numbers should not be null");
        assertEquals(10, ticketNumbers.size(), "Ticket numbers should have 10 elements");
    }

    @Test
    void testGenerateNumbers() {
        List<Integer> numbers = RandomService.generateNumbers();
        assertNotNull(numbers, "Generated numbers should not be null");
        assertEquals(10, numbers.size(), "Generated numbers should have 10 elements");
    }

    @Test
    void testHandleLowTicketNumbers() {
        randomService.handleLowTicketNumbers();
        assertTrue(randomService.ticketNumbers.size() >= 1000, "Should handle low ticket numbers and ensure at least 1000 tickets");
    }
}