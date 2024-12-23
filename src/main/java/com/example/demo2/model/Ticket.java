package com.example.demo2.model;

import lombok.Data;

import static com.example.demo2.Constants.COLUMNS;
import static com.example.demo2.Constants.ROWS_PER_TICKET;

@Data
public class Ticket {

    private int[][] numbers;
    private boolean[][] filled;

    public Ticket() {
        numbers = new int[ROWS_PER_TICKET][COLUMNS];
        filled = new boolean[ROWS_PER_TICKET][COLUMNS];
    }

    // Utility method to print a ticket
    public static void printTicket(Ticket ticket) {
        for (int row = 0; row < ROWS_PER_TICKET; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (ticket.filled[row][col]) {
                    System.out.printf("%2d ", ticket.numbers[row][col]);
                } else {
                    System.out.print("   ");
                }
            }
            System.out.println();
        }
    }

    public void sortColumn(int col) {
        int[] colNumbers = new int[ROWS_PER_TICKET];
        boolean[] colFilled = new boolean[ROWS_PER_TICKET];

        // Extract column
        for (int row = 0; row < ROWS_PER_TICKET; row++) {
            colNumbers[row] = numbers[row][col];
            colFilled[row] = filled[row][col];
        }

        // Sort non-empty cells
        for (int i = 0; i < ROWS_PER_TICKET - 1; i++) {
            for (int j = 0; j < ROWS_PER_TICKET - 1 - i; j++) {
                if (colFilled[j] && colFilled[j + 1] && colNumbers[j] > colNumbers[j + 1]) {
                    // Swap numbers
                    int tempNum = colNumbers[j];
                    colNumbers[j] = colNumbers[j + 1];
                    colNumbers[j + 1] = tempNum;
                }
            }
        }

        // Put back sorted column
        for (int row = 0; row < ROWS_PER_TICKET; row++) {
            numbers[row][col] = colNumbers[row];
            filled[row][col] = colFilled[row];
        }
    }
}