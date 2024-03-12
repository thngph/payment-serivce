package main;

import main.model.Bill;
import main.model.ScheduledPayment;
import main.model.Transaction;
import main.model.User;
import main.service.PaymentService;
import main.service.PaymentServiceImpl;

public class Application {
    public static void main(String[] args) {
        User user = new User(100);
        Bill[] bills = {
            new Bill(1, "Electricity", 100, "2021-12-01", "UNPAID", "Energia"),
            new Bill(2, "Water", 50, "2021-12-09", "UNPAID", "Aguas"),
            new Bill(3, "Gas", 75, "2021-12-03", "UNPAID", "Gas"),
        };
        Transaction[] transactions = {
            new Transaction("DEPOSIT", 100, 100),
        };
        ScheduledPayment[] scheduledPayments = {};
        
        System.out.println("Amount: $" + user.getBalance());
        PaymentService shell = new PaymentServiceImpl(user, bills, transactions, scheduledPayments);
        shell.start();
    }
}
