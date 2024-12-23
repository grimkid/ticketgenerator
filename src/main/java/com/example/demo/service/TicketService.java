package com.example.demo.service;

import com.example.demo.model.RowModel;
import com.example.demo.model.TicketModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final RandomService randomService;
    public TicketModel generateTicket(List<TicketModel> previousTickets) {
        final Map<Integer, Integer> emptySeatsInStrip = getEmptySeatsInStrip(previousTickets);
        final Map<Integer, Integer> neededEmptySeats = getNeededEmptySeats(emptySeatsInStrip);
        final Map<Integer, Object> prohibitedEmptySeats = getProhibitedEmptySeats(emptySeatsInStrip);

        final List<Integer>firstRowEmptySeats = generateFourNumbersForEmptySeats(neededEmptySeats, prohibitedEmptySeats, previousTickets.size(), null);
        final RowModel row = new RowModel();
        row.setEmptySeats(firstRowEmptySeats);
        final List<Integer>secondRowEmptySeats = generateFourNumbersForEmptySeats(neededEmptySeats, prohibitedEmptySeats, previousTickets.size(), firstRowEmptySeats);
        final RowModel row2 = new RowModel();
        row2.setEmptySeats(secondRowEmptySeats);

        final List<Integer>thirdRowEmptySeats = generateThirdRowEmptySeats(firstRowEmptySeats, secondRowEmptySeats, getAlmostNoEmptySeatsCol(firstRowEmptySeats, secondRowEmptySeats));
        final RowModel row3 = new RowModel();
        row3.setEmptySeats(thirdRowEmptySeats);
        final TicketModel ticket = new TicketModel();
        ticket.getRows().addAll(Arrays.asList(row, row2, row3));
        return ticket;
    }

    public List<Integer> generateFourNumbersForEmptySeats(final Map<Integer, Integer> neededEmptySeats, final Map<Integer, Object> prohibitedEmptySeats, Integer previousTickets, List<Integer> previousEmptySeatsRow) {
        Map<Integer, Integer> localNeededEmptySeats = new HashMap<>(neededEmptySeats);
        Map<Integer, Object> localProhibitedEmptySeats = new HashMap<>(prohibitedEmptySeats);
        final List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
//        Collections.shuffle(numbers);
        if (!localProhibitedEmptySeats.isEmpty()) {
            numbers.removeAll(localProhibitedEmptySeats.keySet());
        }
//        Collections.shuffle(numbers);

        final List<Integer> toReturn = new ArrayList<>(4);
        localNeededEmptySeats.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
                        .forEach(integerIntegerEntry -> {
                            if (toReturn.size() < 4 && integerIntegerEntry.getValue() != null && integerIntegerEntry.getValue()/2  <= 6 - previousTickets) {
                                toReturn.add(integerIntegerEntry.getKey());
                            }
                        });
        if (toReturn.size() < 4) {
            numbers.forEach(integer -> {
                if (toReturn.size() < 4 && localNeededEmptySeats.get(integer) != null && !toReturn.contains(integer)) {
                    toReturn.add(integer);
                }
            });
        }

        if (!localNeededEmptySeats.isEmpty()) {
            toReturn.forEach(integer -> {
                if (localNeededEmptySeats.get(integer) != null && localNeededEmptySeats.get(integer) == 1) {
                    localProhibitedEmptySeats.put(integer, new Object());
                }
                localNeededEmptySeats.put(integer, localNeededEmptySeats.get(integer) - 1);
            });
        }
        Collections.sort(toReturn);
        if (previousEmptySeatsRow != null) {
            Collections.sort(previousEmptySeatsRow);
        }
        if (!toReturn.equals(previousEmptySeatsRow)) {
            prohibitedEmptySeats.putAll(localProhibitedEmptySeats);
            neededEmptySeats.putAll(localNeededEmptySeats);
            return toReturn;
        }
        return generateFourNumbersForEmptySeats(neededEmptySeats, prohibitedEmptySeats, previousTickets, previousEmptySeatsRow);
    }

    private List<Integer> getAlmostNoEmptySeatsCol(List<Integer> firstRowEmptySeats, List<Integer> secondRowEmptySeats) {
        final List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            numbers.add(i);
        }
        final List<Integer> toReturn = new ArrayList<>();

        numbers.forEach(integer -> {
            if (!firstRowEmptySeats.contains(integer) && !secondRowEmptySeats.contains(integer)) {
                toReturn.add(integer);
            }
        });
        return toReturn;
    }

    private List<Integer> generateThirdRowEmptySeats(List<Integer> firstRowEmptySeats, List<Integer> secondRowEmptySeats, List<Integer> almostNoEmptySeatsCol) {
        final List<Integer> numbers = new ArrayList<>();
        final List<Integer> mandatoryEmptySeats = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            if (almostNoEmptySeatsCol.contains(i)) {
                mandatoryEmptySeats.add(i);
            } else if (!firstRowEmptySeats.contains(i) || !secondRowEmptySeats.contains(i)) {
                numbers.add(i);
            }
        }
        if (mandatoryEmptySeats.size() < 4) {
            Collections.shuffle(numbers);
            mandatoryEmptySeats.addAll(numbers.subList(0, 4 - mandatoryEmptySeats.size()));
        }
        return mandatoryEmptySeats;
    }

    private Map<Integer, Integer> getEmptySeatsInStrip(List<TicketModel> previousTickets) {
        final Map<Integer,Integer> emptySeatsStrip = new HashMap<>();
        for (final TicketModel ticket : previousTickets) {
            final List<RowModel> rows = ticket.getRows();
            for (final RowModel row : rows) {
                final List<Integer> emptySeats = row.getEmptySeats();
                for (int i = 1; i <= 9; i++) {
                    if (emptySeats.contains(i)) {
                        if (emptySeatsStrip.containsKey(i)) {
                            emptySeatsStrip.put(i, emptySeatsStrip.get(i) + 1);
                        } else {
                            emptySeatsStrip.put(i, 1);
                        }
                    }
                }
            }
        }
        return emptySeatsStrip;
    }
    private Map<Integer, Integer> getNeededEmptySeats(Map<Integer, Integer> currentEmptySeats) {
        final Map<Integer, Integer> neededEmptySeats = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            neededEmptySeats.put(i, 9 - (currentEmptySeats.get(i) != null ? currentEmptySeats.get(i) : 0));
        }
        return neededEmptySeats;
    }
    private Map<Integer, Object> getProhibitedEmptySeats(Map<Integer, Integer> currentEmptySeats) {
        final Map<Integer, Object> prohibitedEmptySeats = new HashMap<>();
        for (int i = 1; i <= 9; i++) {
            if (currentEmptySeats.get(i) != null && currentEmptySeats.get(i) == 9) {
                prohibitedEmptySeats.put(i, new Object());
            }
        }
        return prohibitedEmptySeats;
    }
}
