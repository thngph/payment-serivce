package main.service;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;

import main.model.Bill;
import main.model.ScheduledPayment;
import main.model.State;
import main.model.Transaction;
import main.model.User;
import main.util.CustomDate;
import main.util.Printer;

public class PaymentServiceImpl implements PaymentService {
    private Scanner scanner;
    public User user;
    public Bill[] bills;
    public Transaction[] transactions;
    public ScheduledPayment[] scheduledPayments;

    public PaymentServiceImpl(User user, Bill[] bills, Transaction[] transactions,
            ScheduledPayment[] scheduledPayments) {
        scanner = new Scanner(System.in);
        this.user = user;
        this.bills = bills;
        this.transactions = transactions;
        this.scheduledPayments = scheduledPayments;
    }

    public void start() {
        System.out.println("Welcome to the payment system");
        System.out.println("Type 'exit' to close the application");
        System.out.println(
            "Available commands:\n" +
            "- CHECK_BALANCE\n" +
            "- CASH_IN <amount>\n" +
            "- LIST_ALL\n" +
            "- LIST_UNPAID\n" +
            "- ADD_BILL <id> <type> <amount> <dueDate> <state> <provider>\n" +
            "- PAY_BILL <id>\n" +
            "- PAY_BILLS <id1> <id2> ...\n" +
            "- SCHEDULE_PAYMENT <id> <date>\n" +
            "- SEARCH_BILLS <provider>\n" +
            "- SHOW_TRANS\n"
        );
        boolean running = true;

        while (running) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("exit")) {
                running = false;
            } else {
                processCommand(input);
            }
        }

        scanner.close();
    }

    public User addFund(User user, double amount) {
        user.setBalance(user.getBalance() + amount);
        return user;
    }

    private Transaction[] addTransaction(Transaction[] transactions, Transaction transaction) {
        Transaction[] newTransactions = Arrays.copyOf(transactions, transactions.length + 1);
        newTransactions[transactions.length] = transaction;
        return newTransactions;
    }

    public Bill[] listBills(Bill[] bills) {
        Bill[] sortedBills = Arrays.copyOf(bills, bills.length);
        Arrays.sort(sortedBills, Comparator.comparing(bill -> bill.dueDate));
        return sortedBills;
    }

    public Bill[] listUnpaid(Bill[] bills) {
        Bill[] unpaidBills = Arrays.stream(this.listBills(bills))
                .filter(bill -> bill.state.toString().equals("UNPAID"))
                .toArray(Bill[]::new);
        return unpaidBills;
    }

    public Bill[] addBill(Bill[] bills, Bill bill) {
        Bill[] newBills = Arrays.copyOf(bills, bills.length + 1);
        newBills[bills.length] = bill;
        return newBills;
    }

    public Bill payBill(User user, Bill[] bills, int id) {
        Bill billToPay = Arrays.stream(bills)
                .filter(bill -> bill.id == id)
                .findFirst()
                .orElse(null);
        if (billToPay != null && billToPay.state == State.PAID)
            System.out.println("Bill already paid");
        else if ((billToPay.state == State.UNPAID || billToPay.state == State.PROCCESSED)
                && user.getBalance() >= billToPay.amount) {
            billToPay.state = State.PAID;
            this.user = addFund(this.user, -billToPay.amount);
            this.transactions = addTransaction(this.transactions,
                    new Transaction("PAYMENT", -billToPay.amount, this.user.getBalance()));
        }
        return billToPay;
    }

    public Bill[] payBills(Bill[] bills, int[] ids) {
        List<Integer> idList = Arrays.stream(ids).boxed().collect(Collectors.toList());
        for (int id : ids) {
            try {
                payBill(this.user, bills, id);
            } catch (NullPointerException e) {
                System.out.println("Bill " + id + " not found or already paid.");
            }
        }
        return Arrays.stream(bills)
                .filter(bill -> idList.contains(bill.id))
                .toArray(Bill[]::new);
    }

    public ScheduledPayment schedulePayment(Bill[] bills, ScheduledPayment[] scheduledPayments, int id, String date) {
        ScheduledPayment scheduledPayment = Arrays.stream(scheduledPayments)
                .filter(sp -> sp.billId == id)
                .findFirst()
                .orElse(null);
        Bill billToSchedule = Arrays.stream(bills)
                .filter(bill -> bill.id == id)
                .findFirst()
                .orElse(null);
        if (billToSchedule != null && billToSchedule.state == State.PAID)
            System.out.println("Bill already paid");
        else if (billToSchedule.state == State.PROCCESSED)
            System.out.println("Bill already scheduled");
        else if (billToSchedule.state == State.UNPAID) {
            billToSchedule.state = State.PROCCESSED;
            if (scheduledPayment == null) {
                scheduledPayment = new ScheduledPayment(id, date);
                this.scheduledPayments = Arrays.copyOf(scheduledPayments, scheduledPayments.length + 1);
                this.scheduledPayments[scheduledPayments.length] = scheduledPayment;
            } else {
                scheduledPayment.scheduledDate = CustomDate.parseDate(date);
            }
        }
        return scheduledPayment;
    }

    public Bill[] searchBills(Bill[] bills, String provider) {
        Bill[] billsByProvider = Arrays.stream(bills)
                .filter(bill -> bill.provider.equals(provider))
                .toArray(Bill[]::new);
        return billsByProvider;
    }

    public void processCommand(String command) {
        String[] parts = command.split(" ");
        String cmd = parts[0];
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);

        System.out.println("Executing command: " + cmd);
        switch (cmd) {
            case "CHECK_BALANCE":
                System.out.println("Checking balance");
                System.out.println("Amount: $" + this.user.getBalance());
                break;

            case "CASH_IN":
                System.out.println("Cashing in");
                if (args.length > 0) {
                    try {
                        double amount = Double.parseDouble(args[0]);
                        this.user = addFund(user, amount);
                        this.transactions = addTransaction(this.transactions,
                                new Transaction("DEPOSIT", amount, this.user.getBalance()));

                        System.out.println("Amount: $" + this.user.getBalance());
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid amount for CASH_IN command");
                    }
                } else {
                    System.out.println("No amount specified for CASH_IN command");
                }
                break;

            case "LIST_ALL":
                System.out.println("Listing all bills");
                Printer.printTable(Arrays.asList(listBills(this.bills)));
                break;

            case "LIST_UNPAID":
                System.out.println("Listing unpaid bills");
                Printer.printTable(Arrays.asList(listUnpaid(this.bills)));
                break;

            case "ADD_BILL":
                System.out.println("Adding bill");
                if (args.length == 6) {
                    try {
                        int id = Integer.parseInt(args[0]);
                        String name = args[1];
                        double amount = Double.parseDouble(args[2]);
                        String dueDate = args[3];
                        String state = args[4];
                        String provider = args[5];
                        this.bills = addBill(this.bills, new Bill(id, name, amount, dueDate, state, provider));
                        Printer.printTable(Arrays.asList(listBills(this.bills)));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid arguments for ADD_BILL command");
                    }
                } else {
                    System.out.println("Invalid number of arguments for ADD_BILL command");
                }
                break;

            case "PAY_BILL":
                System.out.println("Paying bill");
                if (args.length > 0) {
                    try {
                        int id = Integer.parseInt(args[0]);
                        Printer.printTable(Arrays.asList(payBill(this.user, this.listUnpaid(this.bills), id)));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid arguments for PAY_BILL command");
                    } catch (NullPointerException e) {
                        System.out.println("Bill not found");
                    }
                } else {
                    System.out.println("No id specified for PAY_BILL command");
                }
                break;

            case "PAY_BILLS":
                System.out.println("Paying bills");
                if (args.length > 0) {
                    try {
                        int[] ids = Arrays.stream(args).mapToInt(Integer::parseInt).toArray();
                        Bill[] unpaidBills = this.listUnpaid(this.bills);
                        Printer.printTable(Arrays.asList(payBills(unpaidBills, ids)));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid arguments for PAY_BILLS command");
                    } catch (NullPointerException e) {
                        System.out.println("Bill not found");
                    }
                } else {
                    System.out.println("No ids specified for PAY_BILLS command");
                }
                break;

            case "SHOW_TRANS":
                System.out.println("Showing transactions");
                Printer.printTable(Arrays.asList(this.transactions));
                break;

            case "SCHEDULE_PAYMENT":
                System.out.println("Scheduling payment");
                if (args.length == 2) {
                    try {
                        int id = Integer.parseInt(args[0]);
                        String date = args[1];
                        Printer.printTable(
                                Arrays.asList(schedulePayment(this.bills, this.scheduledPayments, id, date)));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid arguments for SCHEDULE_PAYMENT command");
                    }
                } else {
                    System.out.println("Invalid number of arguments for SCHEDULE_PAYMENT command");
                }
                break;

            case "SEARCH_BILLS":
                System.out.println("Searching bills");
                if (args.length > 0) {
                    String provider = args[0];
                    Printer.printTable(Arrays.asList(searchBills(this.bills, provider)));
                } else {
                    System.out.println("No provider specified for SEARCH_BILLS command");
                }
                break;

            default:
                break;
        }
    }
}
