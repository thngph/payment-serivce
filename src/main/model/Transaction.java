package main.model;

public class Transaction {
    public Type type;
    public double amount;
    public double balance;

    public Transaction(
        String type,
        double amount,
        double balance
    ) {
        this.type = Type.valueOf(type);
        this.amount = amount;
        this.balance = balance;
    }
}
