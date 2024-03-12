package main.service;

import main.model.Bill;
import main.model.ScheduledPayment;
import main.model.User;


public interface PaymentService {
    public void start();

    public User addFund(User user, double amount);

    public Bill[] listBills(Bill[] bills);

    public Bill[] listUnpaid(Bill[] bills);

    public Bill[] addBill(Bill[] bills, Bill bill);

    public Bill payBill(User user, Bill[] bills, int id);

    public Bill[] payBills(Bill[] bills, int[] ids);

    public ScheduledPayment schedulePayment(Bill[] bills, ScheduledPayment[] scheduledPayments, int id, String date);

    public Bill[] searchBills(Bill[] bills, String provider);

    public void processCommand(String command);
}
