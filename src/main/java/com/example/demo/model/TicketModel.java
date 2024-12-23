package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TicketModel {
    //max 6 col with 2 empty seats and 3 col with full seats
    //0 0 0 x x x x x x
    //0 0 x 0 x x x x x
    //x x 0 0 x x x x x
    //5 col with 2 seats,
    //max 6 col with 1 empty seats and 3 col with 2 empty seats
    List<RowModel> rows = new ArrayList<>(3);
}
//x x x x x x x x x
//x x x x x x x x x
//x x x x x x x x x