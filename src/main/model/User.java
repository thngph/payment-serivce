package main.model;

public class User {
    private double balance;

    public User(double balance) {
        this.balance = balance;
    }

    public User() {
        this.balance = 0;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    
}
