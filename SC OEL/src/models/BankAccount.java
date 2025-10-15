package models;

import enums.AccountStatus;
import enums.TransactionType;
import enums.TransactionStatus;

import java.util.ArrayList;

public abstract class BankAccount {
    protected String accountNumber;
    protected String customerId;
    protected double balance;
    protected AccountStatus status;
    protected ArrayList<Transaction> transactionHistory;
    
    public BankAccount(String accountNumber, String customerId, double initialBalance) {
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = initialBalance;
        this.status = AccountStatus.ACTIVE;
        this.transactionHistory = new ArrayList<>();
        
        // Record initial deposit
        if (initialBalance > 0) {
            Transaction initialTxn = new Transaction(
                TransactionType.DEPOSIT, 
                initialBalance, 
                accountNumber,
                TransactionStatus.SUCCESS,
                balance
            );
            transactionHistory.add(initialTxn);
        }
    }
    
    // Getters
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public void setStatus(AccountStatus status) {
        this.status = status;
    }
    
    public ArrayList<Transaction> getTransactionHistory() {
        return transactionHistory;
    }
    
    public abstract void deposit(double amount) throws Exception;

    public abstract void withdraw(double amount) throws Exception;
    
    public abstract String getAccountType();
    
    protected void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }
    
    public void displayTransactionHistory() {
        if (transactionHistory.isEmpty()) {
            System.out.println("No transactions found.");
            return;
        }
        
        System.out.println("\n===== Transaction History for Account " + accountNumber + " =====");
        for (Transaction txn : transactionHistory) {
            System.out.println(txn);
            System.out.println("------------------------------");
        }
    }

    @Override
    public String toString() {
        return String.format("%s Account - Number: %s, Balance: $%.2f, Status: %s",
                           getAccountType(), accountNumber, balance, status);
    }
}
