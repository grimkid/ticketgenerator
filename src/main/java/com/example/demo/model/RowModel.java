package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RowModel {
    //5 numbers seats and 4 empty seats
    private  List<Integer> emptySeats = new ArrayList<>();
    private  List<Integer> numberSeats = new ArrayList<>();


}
