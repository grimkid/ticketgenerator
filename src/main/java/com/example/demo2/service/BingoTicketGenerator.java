package com.example.demo2.service;

import com.example.demo2.model.Ticket;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static com.example.demo2.Constants.*;
import static com.example.demo2.model.Ticket.printTicket;

@Service
public class BingoTicketGenerator {
    public static int ticketsInStrip = 6;

    public static Ticket[] generateStrip() {
        while (true) {
            try {
                return generateStripAttempt();
            } catch (IllegalStateException e) {
                // Retry if generation fails
                continue;
            }
        }
    }

    private static Ticket[] generateStripAttempt() {
       final Ticket[] strip = new Ticket[TICKETS_IN_STRIP];
        for (int i = 0; i < TICKETS_IN_STRIP; i++) {
            strip[i] = new Ticket();
        }

        // Create and distribute all numbers
        List<Integer>[] columnNumbers = createColumnNumbers();
        distributeNumbers(strip, columnNumbers);

        // Create empty spaces while maintaining constraints
        createEmptySpaces(strip);

        // Sort numbers within columns
        for (Ticket ticket : strip) {
            for (int col = 0; col < COLUMNS; col++) {
                ticket.sortColumn(col);
            }
        }

        // Validate the strip
        if (!validateStrip(strip)) {
            throw new IllegalStateException("Invalid strip generated");
        }

        return strip;
    }

    @SuppressWarnings("unchecked")
    private static List<Integer>[] createColumnNumbers() {
        List<Integer>[] columns = new List[COLUMNS];

        // Initialize columns
        for (int i = 0; i < COLUMNS; i++) {
            columns[i] = new ArrayList<>();
            int start = i == 0 ? 1 : i * 10;
            int end = i == 0 ? 9 : (i * 10 + 9);

            for (int num = start; num <= end; num++) {
                columns[i].add(num);
            }
            Collections.shuffle(columns[i]);
        }

        return columns;
    }

    private static void distributeNumbers(Ticket[] strip, List<Integer>[] columnNumbers) {
        // Distribute numbers column by column
        for (int col = 0; col < COLUMNS; col++) {
            List<Integer> numbers = columnNumbers[col];
            int numbersPerTicket = numbers.size() / TICKETS_IN_STRIP;
            int extraNumbers = numbers.size() % TICKETS_IN_STRIP;

            int numberIndex = 0;
            for (int ticketIndex = 0; ticketIndex < TICKETS_IN_STRIP; ticketIndex++) {
                int numbersToAdd = numbersPerTicket + (extraNumbers > 0 ? 1 : 0);
                if (extraNumbers > 0) extraNumbers--;

                Ticket ticket = strip[ticketIndex];
                for (int i = 0; i < numbersToAdd; i++) {
                    // Find least filled row that can accept a number
                    int row = findSuitableRow(ticket);
                    ticket.getNumbers()[row][col] = numbers.get(numberIndex++);
                    ticket.getFilled()[row][col] = true;
                }
            }
        }
    }

    private static int findSuitableRow(Ticket ticket) {
        int[] rowCounts = new int[ROWS_PER_TICKET];
        for (int row = 0; row < ROWS_PER_TICKET; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                if (ticket.getFilled()[row][col]) rowCounts[row]++;
            }
        }

        // Find row with fewest numbers that isn't full
        int minNumbers = Integer.MAX_VALUE;
        int selectedRow = -1;

        for (int row = 0; row < ROWS_PER_TICKET; row++) {
            if (rowCounts[row] < NUMBERS_PER_ROW && rowCounts[row] < minNumbers) {
                minNumbers = rowCounts[row];
                selectedRow = row;
            }
        }

        if (selectedRow == -1) {
            throw new IllegalStateException("Cannot find suitable row");
        }

        return selectedRow;
    }

    private static void createEmptySpaces(Ticket[] strip) {
        for (Ticket ticket : strip) {
            for (int row = 0; row < ROWS_PER_TICKET; row++) {
                // Count numbers in row
                int numbersInRow = 0;
                for (int col = 0; col < COLUMNS; col++) {
                    if (ticket.getFilled()[row][col]) numbersInRow++;
                }

                // Remove numbers until we have exactly 5 numbers (4 empty spaces)
                while (numbersInRow > NUMBERS_PER_ROW) {
                    // Find removable number
                    List<Integer> removableCols = new ArrayList<>();
                    for (int col = 0; col < COLUMNS; col++) {
                        if (ticket.getFilled()[row][col] && canRemoveNumber(ticket, row, col)) {
                            removableCols.add(col);
                        }
                    }

                    if (removableCols.isEmpty()) {
                        throw new IllegalStateException("Cannot create required empty spaces");
                    }

                    // Remove a random removable number
                    int colToRemove = removableCols.get(new Random().nextInt(removableCols.size()));
                    ticket.getFilled()[row][colToRemove] = false;
                    numbersInRow--;
                }
            }
        }
    }

    private static boolean canRemoveNumber(Ticket ticket, int row, int col) {
        // Count numbers in this column
        int numbersInColumn = 0;
        for (int r = 0; r < ROWS_PER_TICKET; r++) {
            if (ticket.getFilled()[r][col]) numbersInColumn++;
        }

        // Cannot remove if it would leave column empty
        return numbersInColumn > 1;
    }

    private static boolean validateStrip(Ticket[] strip) {
        // Check each ticket
        for (Ticket ticket : strip) {
            // Check each row has exactly 5 numbers
            for (int row = 0; row < ROWS_PER_TICKET; row++) {
                int numbersInRow = 0;
                for (int col = 0; col < COLUMNS; col++) {
                    if (ticket.getFilled()[row][col]) numbersInRow++;
                }
                if (numbersInRow != NUMBERS_PER_ROW) return false;
            }

            // Check no column is empty
            for (int col = 0; col < COLUMNS; col++) {
                boolean hasNumber = false;
                for (int row = 0; row < ROWS_PER_TICKET; row++) {
                    if (ticket.getFilled()[row][col]) hasNumber = true;
                }
                if (!hasNumber) return false;
            }
        }

        return true;
    }

    // Utility method to print an entire strip
    public static void printStrip(Ticket[] strip) {
        for (int i = 0; i < TICKETS_IN_STRIP; i++) {
            System.out.println("\nTicket " + (i + 1) + ":");
            printTicket(strip[i]);
        }
    }
}