package com.example.demo2.service;

import com.example.demo2.model.Ticket;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BingoTicketGeneratorTest {

    @Test
    void testGenerateStrip() {
        Ticket[] strip = BingoTicketGenerator.generateStrip();
        assertNotNull(strip);
        assertEquals(6, strip.length);

        for (Ticket ticket : strip) {
            assertNotNull(ticket);
            validateTicket(ticket);
        }
    }

    private void validateTicket(Ticket ticket) {
        int[][] numbers = ticket.getNumbers();
        boolean[][] filled = ticket.getFilled();

        // Check each row has exactly 5 numbers
        for (int row = 0; row < 3; row++) {
            int numbersInRow = 0;
            for (int col = 0; col < 9; col++) {
                if (filled[row][col]) {
                    numbersInRow++;
                }
            }
            assertEquals(5, numbersInRow, "Each row should have exactly 5 numbers");
        }

        // Check no column is empty
        for (int col = 0; col < 9; col++) {
            boolean hasNumber = false;
            for (int row = 0; row < 3; row++) {
                if (filled[row][col]) {
                    hasNumber = true;
                    break;
                }
            }
            assertTrue(hasNumber, "No column should be empty");
        }

        // Check all numbers are within the correct range
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 3; row++) {
                if (filled[row][col]) {
                    int number = numbers[row][col];
                    int start = col == 0 ? 1 : col * 10;
                    int end = col == 0 ? 9 : (col * 10 + 9);
                    if (col == 8) end = 90; // Last column ends at 90
                    assertTrue(number >= start && number <= end, "Number out of range");
                }
            }
        }
    }
}