package models;

import enums.AccountStatus;
import enums.TransactionType;
import enums.TransactionStatus;
import exceptions.AccountBlockedException;
import exceptions.InsufficientFundsException;
import exceptions.MinimumBalanceException;

public class SavingsAccount extends BankAccount {
    private static final double MINIMUM_BALANCE = 500.0;
    private static final double INTEREST_RATE = 0.03; // 3% annual interest
    
    public SavingsAccount(String accountNumber, String customerId, double initialBalance) 
            throws MinimumBalanceException {
        super(accountNumber, customerId, initialBalance);
        
        if (initialBalance < MINIMUM_BALANCE) {
            throw new MinimumBalanceException(
                "Initial deposit must be at least $" + MINIMUM_BALANCE + 
                " for a savings account."
            );
        }
    }
    
    @Override
    public void deposit(double amount) throws Exception {
        if (status != AccountStatus.ACTIVE) {
            Transaction failedTxn = new Transaction(
                TransactionType.DEPOSIT, amount, accountNumber,
                TransactionStatus.FAILED_ACCOUNT_BLOCKED, balance
            );
            addTransaction(failedTxn);
            throw new AccountBlockedException("Account is " + status + ". Cannot deposit.");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        
        balance += amount;
        Transaction txn = new Transaction(
            TransactionType.DEPOSIT, amount, accountNumber,
            TransactionStatus.SUCCESS, balance
        );
        addTransaction(txn);
    }
    
    @Override
    public void withdraw(double amount) throws Exception {
        if (status != AccountStatus.ACTIVE) {
            Transaction failedTxn = new Transaction(
                TransactionType.WITHDRAWAL, amount, accountNumber,
                TransactionStatus.FAILED_ACCOUNT_BLOCKED, balance
            );
            addTransaction(failedTxn);
            throw new AccountBlockedException("Account is " + status + ". Cannot withdraw.");
        }
        
        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        
        if (balance - amount < MINIMUM_BALANCE) {
            Transaction failedTxn = new Transaction(
                TransactionType.WITHDRAWAL, amount, accountNumber,
                TransactionStatus.FAILED_MINIMUM_BALANCE_VIOLATION, balance
            );
            addTransaction(failedTxn);
            throw new MinimumBalanceException(
                "Withdrawal denied. Minimum balance of $" + MINIMUM_BALANCE + 
                " must be maintained. Current balance: $" + String.format("%.2f", balance) +
                ", Requested withdrawal: $" + String.format("%.2f", amount)
            );
        }
        
        if (amount > balance) {
            Transaction failedTxn = new Transaction(
                TransactionType.WITHDRAWAL, amount, accountNumber,
                TransactionStatus.FAILED_INSUFFICIENT_FUNDS, balance
            );
            addTransaction(failedTxn);
            throw new InsufficientFundsException(
                "Insufficient funds. Available balance: $" + String.format("%.2f", balance)
            );
        }
        
        balance -= amount;
        Transaction txn = new Transaction(
            TransactionType.WITHDRAWAL, amount, accountNumber,
            TransactionStatus.SUCCESS, balance
        );
        addTransaction(txn);
    }
    
    public void applyInterest() {
        double interest = balance * INTEREST_RATE;
        balance += interest;
        Transaction txn = new Transaction(
            TransactionType.DEPOSIT, interest, accountNumber,
            TransactionStatus.SUCCESS, balance
        );
        addTransaction(txn);
        System.out.println("Interest of $" + String.format("%.2f", interest) + " applied to account.");
    }
    
    @Override
    public String getAccountType() {
        return "Savings";
    }
    
    public double getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Min Balance: $%.2f", MINIMUM_BALANCE);
    }
}
