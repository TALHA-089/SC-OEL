package models;

import enums.AccountStatus;
import enums.TransactionType;
import enums.TransactionStatus;
import exceptions.AccountBlockedException;
import exceptions.InsufficientFundsException;
import exceptions.InvalidAccountException;
import exceptions.MinimumBalanceException;

import java.util.HashMap;
import java.util.ArrayList;


public class Bank {
    private String bankName;
    private HashMap<String, Customer> customers;  // Key: customerId
    private HashMap<String, BankAccount> accounts;  // Key: accountNumber
    private static int accountNumberCounter = 10000;
    
    public Bank(String bankName) {
        this.bankName = bankName;
        this.customers = new HashMap<>();
        this.accounts = new HashMap<>();
    }
    
    public String getBankName() {
        return bankName;
    }
    
    public Customer registerCustomer(String customerId, String name, String pin) {
        if (customers.containsKey(customerId)) {
            System.out.println("Customer with ID " + customerId + " already exists.");
            return null;
        }
        
        Customer customer = new Customer(customerId, name, pin);
        customers.put(customerId, customer);
        return customer;
    }
    
    public SavingsAccount createSavingsAccount(String customerId, double initialBalance) 
            throws Exception {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new InvalidAccountException("Customer not found: " + customerId);
        }
        
        String accountNumber = "SAV" + (++accountNumberCounter);
        SavingsAccount account = new SavingsAccount(accountNumber, customerId, initialBalance);
        
        customer.addAccount(account);
        accounts.put(accountNumber, account);
        
        return account;
    }
    
    public CheckingAccount createCheckingAccount(String customerId, double initialBalance) 
            throws Exception {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new InvalidAccountException("Customer not found: " + customerId);
        }
        
        String accountNumber = "CHK" + (++accountNumberCounter);
        CheckingAccount account = new CheckingAccount(accountNumber, customerId, initialBalance);
        
        customer.addAccount(account);
        accounts.put(accountNumber, account);
        
        return account;
    }
    
    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }

    public BankAccount getAccount(String accountNumber) {
        return accounts.get(accountNumber);
    }
    
    public Customer authenticateCustomer(String customerId, String pin) 
            throws AccountBlockedException {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            return null;
        }
        
        if (customer.getLoginStatus() == AccountStatus.BLOCKED) {
            throw new AccountBlockedException(
                "Your account has been blocked due to multiple failed login attempts. " +
                "Please contact bank administrator."
            );
        }
        
        if (customer.validatePin(pin)) {
            return customer;
        }
        
        return null;
    }
    
    public void transferFunds(String sourceAccountNumber, String destinationAccountNumber, 
                             double amount) throws Exception {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive.");
        }
        
        BankAccount sourceAccount = accounts.get(sourceAccountNumber);
        BankAccount destinationAccount = accounts.get(destinationAccountNumber);
        
        if (sourceAccount == null) {
            throw new InvalidAccountException("Source account not found: " + sourceAccountNumber);
        }
        
        if (destinationAccount == null) {
            Transaction failedTxn = new Transaction(
                TransactionType.TRANSFER_OUT, amount, sourceAccountNumber, 
                destinationAccountNumber, TransactionStatus.FAILED_INVALID_ACCOUNT, 
                sourceAccount.getBalance()
            );
            sourceAccount.addTransaction(failedTxn);
            throw new InvalidAccountException("Destination account not found: " + destinationAccountNumber);
        }
        
        if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
            throw new AccountBlockedException("Destination account is not active.");
        }
        
        // Attempt withdrawal from source account
        try {
            sourceAccount.withdraw(amount);
            
            // Remove the withdrawal transaction and replace with transfer transaction
            ArrayList<Transaction> sourceHistory = sourceAccount.getTransactionHistory();
            sourceHistory.remove(sourceHistory.size() - 1);
            
            // Add transfer-out transaction to source
            Transaction transferOut = new Transaction(
                TransactionType.TRANSFER_OUT, amount, sourceAccountNumber,
                destinationAccountNumber, TransactionStatus.SUCCESS, 
                sourceAccount.getBalance()
            );
            sourceAccount.addTransaction(transferOut);
            
            // Deposit to destination account
            destinationAccount.deposit(amount);
            
            // Remove the deposit transaction and replace with transfer transaction
            ArrayList<Transaction> destHistory = destinationAccount.getTransactionHistory();
            destHistory.remove(destHistory.size() - 1);
            
            // Add transfer-in transaction to destination
            Transaction transferIn = new Transaction(
                TransactionType.TRANSFER_IN, amount, sourceAccountNumber,
                destinationAccountNumber, TransactionStatus.SUCCESS, 
                destinationAccount.getBalance()
            );
            destinationAccount.addTransaction(transferIn);
            
        } catch (InsufficientFundsException | MinimumBalanceException e) {
            // Transaction already recorded as failed in the withdraw method
            throw e;
        }
    }

    public ArrayList<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    /**
     * Returns all accounts in the bank.
     */
    public ArrayList<BankAccount> getAllAccounts() {
        return new ArrayList<>(accounts.values());
    }
    
    public void unblockCustomer(String customerId) throws InvalidAccountException {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new InvalidAccountException("Customer not found: " + customerId);
        }
        
        customer.resetFailedAttempts();
        System.out.println("Customer " + customerId + " has been unblocked successfully.");
    }
    
    public void initializeSampleData() {
        try {
            // Create sample customers
            Customer c1 = registerCustomer("C001", "Alice Johnson", "1234");
            Customer c2 = registerCustomer("C002", "Bob Smith", "5678");
            Customer c3 = registerCustomer("C003", "Charlie Brown", "9012");
            
            // Create accounts for customers
            createSavingsAccount("C001", 5000.00);
            createCheckingAccount("C001", 2000.00);
            
            createSavingsAccount("C002", 10000.00);
            createCheckingAccount("C002", 1500.00);
            
            createSavingsAccount("C003", 3000.00);
            
            System.out.println("Sample data initialized successfully.");
            System.out.println("Sample Customers:");
            System.out.println("  Customer ID: C001, PIN: 1234 (Alice Johnson)");
            System.out.println("  Customer ID: C002, PIN: 5678 (Bob Smith)");
            System.out.println("  Customer ID: C003, PIN: 9012 (Charlie Brown)");
            
        } catch (Exception e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }
    
    @Override
    public String toString() {
        return String.format("Bank: %s, Customers: %d, Total Accounts: %d",
                           bankName, customers.size(), accounts.size());
    }
}