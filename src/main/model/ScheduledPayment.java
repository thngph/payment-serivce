package main.model;

import java.util.Date;

import main.util.CustomDate;

public class ScheduledPayment {
    public int billId;
    public Date scheduledDate;

    public ScheduledPayment(int billId, String scheduledDate) {
        this.billId = billId;
        this.scheduledDate = CustomDate.parseDate(scheduledDate);
    }
}
