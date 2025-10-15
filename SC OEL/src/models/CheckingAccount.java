package models;

import enums.AccountStatus;
import enums.TransactionType;
import enums.TransactionStatus;
import exceptions.AccountBlockedException;
import exceptions.InsufficientFundsException;

public class CheckingAccount extends BankAccount {
    private static final double OVERDRAFT_LIMIT = 1000.0;
    private static final double TRANSACTION_FEE = 1.50;
    private static final int FREE_TRANSACTIONS_PER_MONTH = 10;
    private int transactionCount;
    
    public CheckingAccount(String accountNumber, String customerId, double initialBalance) {
        super(accountNumber, customerId, initialBalance);
        this.transactionCount = 0;
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
        transactionCount++;
        
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
        
        // Check if withdrawal would exceed overdraft limit
        double potentialBalance = balance - amount;
        if (potentialBalance < -OVERDRAFT_LIMIT) {
            Transaction failedTxn = new Transaction(
                TransactionType.WITHDRAWAL, amount, accountNumber,
                TransactionStatus.FAILED_OVERDRAFT_EXCEEDED, balance
            );
            addTransaction(failedTxn);
            throw new InsufficientFundsException(
                "Withdrawal denied. Overdraft limit of $" + OVERDRAFT_LIMIT + 
                " would be exceeded. Current balance: $" + String.format("%.2f", balance) +
                ", Requested withdrawal: $" + String.format("%.2f", amount) +
                ", Available (including overdraft): $" + String.format("%.2f", balance + OVERDRAFT_LIMIT)
            );
        }
        
        balance -= amount;
        transactionCount++;
        
        // Apply transaction fee if free transactions exceeded
        if (transactionCount > FREE_TRANSACTIONS_PER_MONTH && balance >= TRANSACTION_FEE) {
            balance -= TRANSACTION_FEE;
        }
        
        Transaction txn = new Transaction(
            TransactionType.WITHDRAWAL, amount, accountNumber,
            TransactionStatus.SUCCESS, balance
        );
        addTransaction(txn);
    }
    
    public void resetTransactionCount() {
        this.transactionCount = 0;
    }
    
    @Override
    public String getAccountType() {
        return "Checking";
    }
    
    public double getOverdraftLimit() {
        return OVERDRAFT_LIMIT;
    }
    
    public int getTransactionCount() {
        return transactionCount;
    }
    
    @Override
    public String toString() {
        return super.toString() + String.format(", Overdraft Limit: $%.2f", OVERDRAFT_LIMIT);
    }
}
