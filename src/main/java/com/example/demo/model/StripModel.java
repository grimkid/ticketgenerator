package com.example.demo.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class StripModel {
    List<TicketModel> tickets = new ArrayList<>();
}
