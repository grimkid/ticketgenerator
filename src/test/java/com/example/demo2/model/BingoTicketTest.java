package com.example.demo2.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BingoTicketTest {

    @Test
    void testTicketInitialization() {
        Ticket ticket = new Ticket();
        int[][] numbers = ticket.getNumbers();
        boolean[][] filled = ticket.getFilled();

        // Check ticket is initialized with zeros
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                assertEquals(0, numbers[row][col], "Ticket should be initialized with zeros");
                assertFalse(filled[row][col], "Ticket should be initialized with false filled values");
            }
        }
    }

    @Test
    void testSetNumber() {
        Ticket ticket = new Ticket();
        ticket.getNumbers()[0][0] = 5;
        ticket.getFilled()[0][0] = true;
        assertEquals(5, ticket.getNumbers()[0][0], "Number should be set correctly");
        assertTrue(ticket.getFilled()[0][0], "Filled value should be set correctly");
    }

    @Test
    void testSortColumn() {
        Ticket ticket = new Ticket();
        ticket.getNumbers()[0][0] = 10;
        ticket.getNumbers()[1][0] = 5;
        ticket.getFilled()[0][0] = true;
        ticket.getFilled()[1][0] = true;
        ticket.sortColumn(0);
        assertEquals(5, ticket.getNumbers()[0][0], "Column should be sorted correctly");
        assertEquals(10, ticket.getNumbers()[1][0], "Column should be sorted correctly");
    }
}