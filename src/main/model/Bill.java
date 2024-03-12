package main.model;

import java.util.Date;

import main.util.CustomDate;

public class Bill {
    public int id;
    public String type;
    public double amount;
    public Date dueDate;
    public State state;
    public String provider;

    public Bill(int id, String type, double amount, String dueDate, String state, String provider) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dueDate = CustomDate.parseDate(dueDate);
        this.state = State.valueOf(state);
        this.provider = provider;
    }
}