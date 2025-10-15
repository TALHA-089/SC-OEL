package models;

import enums.TransactionType;
import enums.TransactionStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static int transactionCounter = 1000;
    
    private String transactionId;
    private TransactionType type;
    private double amount;
    private LocalDateTime timestamp;
    private String sourceAccountNumber;
    private String destinationAccountNumber;
    private TransactionStatus status;
    private double balanceAfter;
    
    public Transaction(TransactionType type, double amount, String accountNumber, 
                      TransactionStatus status, double balanceAfter) {
        this.transactionId = "TXN" + (++transactionCounter);
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.sourceAccountNumber = accountNumber;
        this.destinationAccountNumber = null;
        this.status = status;
        this.balanceAfter = balanceAfter;
    }
    

    public Transaction(TransactionType type, double amount, String sourceAccount, 
                      String destinationAccount, TransactionStatus status, double balanceAfter) {
        this.transactionId = "TXN" + (++transactionCounter);
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
        this.sourceAccountNumber = sourceAccount;
        this.destinationAccountNumber = destinationAccount;
        this.status = status;
        this.balanceAfter = balanceAfter;
    }
    
    public String getTransactionId() {
        return transactionId;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }
    
    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }
    
    public TransactionStatus getStatus() {
        return status;
    }
    
    public double getBalanceAfter() {
        return balanceAfter;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Type: ").append(type).append("\n");
        sb.append("Amount: $").append(String.format("%.2f", amount)).append("\n");
        sb.append("Date/Time: ").append(timestamp.format(formatter)).append("\n");
        if (destinationAccountNumber != null) {
            sb.append("From Account: ").append(sourceAccountNumber).append("\n");
            sb.append("To Account: ").append(destinationAccountNumber).append("\n");
        } else {
            sb.append("Account: ").append(sourceAccountNumber).append("\n");
        }
        sb.append("Status: ").append(status).append("\n");
        if (status == TransactionStatus.SUCCESS) {
            sb.append("Balance After: $").append(String.format("%.2f", balanceAfter)).append("\n");
        }
        return sb.toString();
    }
    
    public String toReceipt() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== RECEIPT ==========\n");
        sb.append("Transaction ID: ").append(transactionId).append("\n");
        sb.append("Type: ").append(type).append("\n");
        sb.append("Amount: $").append(String.format("%.2f", amount)).append("\n");
        sb.append("Date/Time: ").append(timestamp.format(formatter)).append("\n");
        if (destinationAccountNumber != null) {
            sb.append("To Account: ").append(destinationAccountNumber).append("\n");
        }
        sb.append("Status: ").append(status).append("\n");
        if (status == TransactionStatus.SUCCESS) {
            sb.append("New Balance: $").append(String.format("%.2f", balanceAfter)).append("\n");
        }
        sb.append("=============================\n");
        return sb.toString();
    }
}
