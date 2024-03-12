package test;

import java.util.Arrays;

import main.model.Bill;
import main.model.ScheduledPayment;
import main.model.Transaction;
import main.model.User;
import main.service.PaymentServiceImpl;

public class TestSuite {
    public static void main(String[] args) {
        // Test case 1
        PaymentServiceImpl paymentService = new PaymentServiceImpl(
                new User(100),
                new Bill[] {
                        new Bill(1, "Electricity", 100, "2021-12-01", "UNPAID", "Energia"),
                        new Bill(2, "Water", 50, "2021-12-09", "UNPAID", "Aguas"),
                        new Bill(3, "Gas", 75, "2021-12-03", "UNPAID", "Gas"),
                },
                new Transaction[] {
                        new Transaction("DEPOSIT", 100, 100),
                },
                new ScheduledPayment[] {});

        double fundAdded = paymentService.addFund(new User(100), 5).getBalance();
        if (fundAdded == 105) {
            System.out.println("Test case 1 passed");
        } else {
            System.out.println("Test case 1 failed");
        }

        // Test case 2
        PaymentServiceImpl paymentService2 = new PaymentServiceImpl(
                new User(100),
                new Bill[] {
                        new Bill(1, "Electricity", 100, "2021-12-01", "UNPAID", "Energia"),
                        new Bill(2, "Water", 50, "2021-12-09", "UNPAID", "Aguas"),
                        new Bill(3, "Gas", 75, "2021-12-03", "PAID", "Gas"),
                },
                new Transaction[] {
                        new Transaction("DEPOSIT", 100, 100),
                },
                new ScheduledPayment[] {});

        int[] onlyUnpaids = Arrays.stream(paymentService2.listUnpaid(paymentService2.bills))
                .mapToInt(bill -> bill.id)
                .toArray();
        if (Arrays.equals(onlyUnpaids, new int[] { 1, 2 })) {
            System.out.println("Test case 2 passed");
        } else {
            System.out.println("Test case 2 failed");
        }

        // Test case 3
        PaymentServiceImpl paymentService3 = new PaymentServiceImpl(
                new User(100),
                new Bill[] {
                        new Bill(1, "Electricity", 101, "2021-12-01", "UNPAID", "Energia"),
                        new Bill(2, "Water", 50, "2021-12-09", "UNPAID", "Aguas"),
                        new Bill(3, "Gas", 75, "2021-12-03", "UNPAID", "Gas"),
                },
                new Transaction[] {
                        new Transaction("DEPOSIT", 100, 100),
                },
                new ScheduledPayment[] {});

        Bill notEnoughBalance = paymentService3.payBill(new User(100), paymentService3.bills, 1);
        if (notEnoughBalance.state.toString() == "UNPAID") {
            System.out.println("Test case 3 passed");
        } else {
            System.out.println("Test case 3 failed");
        }

        // Test case 4
        PaymentServiceImpl paymentService4 = new PaymentServiceImpl(
                new User(1000),
                new Bill[] {
                        new Bill(1, "Electricity", 100, "2021-12-01", "UNPAID", "Energia"),
                        new Bill(2, "Water", 50, "2021-12-09", "UNPAID", "Aguas"),
                        new Bill(3, "Gas", 75, "2021-12-03", "UNPAID", "Gas"),
                },
                new Transaction[] {
                        new Transaction("DEPOSIT", 100, 100),
                },
                new ScheduledPayment[] {});
        Bill[] billsAfterPayment = paymentService4.payBills(paymentService4.bills, new int[] { 1, 2 });
        if (Arrays.stream(billsAfterPayment).allMatch(bill -> bill.state.toString() == "PAID")) {
            System.out.println("Test case 4 passed");
        } else {
            System.out.println("Test case 4 failed");
        }

        // Test case 5
        PaymentServiceImpl paymentService5 = new PaymentServiceImpl(
                new User(100),
                new Bill[] {
                        new Bill(1, "Electricity", 100, "2021-12-21", "UNPAID", "Energia"),
                        new Bill(2, "Water", 50, "2021-12-09", "UNPAID", "Aguas"),
                        new Bill(3, "Gas", 75, "2021-12-03", "UNPAID", "Gas"),
                },
                new Transaction[] {
                        new Transaction("DEPOSIT", 100, 100),
                },
                new ScheduledPayment[] {});
        Bill[] notEnoughBalanceForAllBills = paymentService5.payBills(paymentService5.bills, new int[] { 1, 2, 3 });
        int[] paidBills = Arrays.stream(notEnoughBalanceForAllBills)
                .filter(bill -> bill.state.toString() == "PAID")
                .mapToInt(bill -> bill.id)
                .toArray();
        if (Arrays.equals(paidBills, new int[] { 1 })) {
            System.out.println("Test case 5 passed");
        } else {
            System.out.println("Test case 5 failed");
        }
    }
}
