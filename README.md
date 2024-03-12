# Payment Service

This is the repository for the Payment Service, written in Java without any external libraries, in 4hrs straight. However, I haven't experienced much with Java so... 
I tried (my best) to make the solution as functional as possible.


Assume that bills have 3 states:
- PAID
- UNPAID
- PROCESSED: has been scheduled for payment


Assume that transactions has 2 types:
- DEPOSIT: add fund to account
- PAYMENT: kill bills


Assume that users has many attrs, but here we only care about their balance.

Available commands:
- CHECK_BALANCE
- CASH_IN <amount>
- LIST_ALL : List all bills, ordered by due date.
- LIST_UNPAID: list all unpaid bills, ordered by due date.
- ADD_BILL <id> <type> <amount> <dueDate> <state> <provider>
- PAY_BILL <id> : Pay for a bill if exists and the balance is enough.
- PAY_BILLS <id1> <id2> ... : Pay for bills in order of the input, and only if the balance is enough.
- SCHEDULE_PAYMENT <id> <date> : This the mechanism for scheduler has not been implemented, only the info recorded.
- SEARCH_BILLS <provider>
- SHOW_TRANS
