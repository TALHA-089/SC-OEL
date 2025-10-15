package models;

import enums.AccountStatus;

import java.util.ArrayList;

public class Customer {
    private String customerId;
    private String name;
    private String pin;
    private ArrayList<BankAccount> accounts;
    private AccountStatus loginStatus;
    private int failedLoginAttempts;
    private static final int MAX_FAILED_ATTEMPTS = 3;
    
    public Customer(String customerId, String name, String pin) {
        this.customerId = customerId;
        this.name = name;
        this.pin = pin;
        this.accounts = new ArrayList<>();
        this.loginStatus = AccountStatus.ACTIVE;
        this.failedLoginAttempts = 0;
    }
    
    // Getters
    public String getCustomerId() {
        return customerId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPin() {
        return pin;
    }
    
    public ArrayList<BankAccount> getAccounts() {
        return accounts;
    }
    
    public AccountStatus getLoginStatus() {
        return loginStatus;
    }
    
    public void setLoginStatus(AccountStatus status) {
        this.loginStatus = status;
    }
    
    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }
    
    public void addAccount(BankAccount account) {
        accounts.add(account);
    }
    
    public boolean validatePin(String inputPin) {
        if (loginStatus == AccountStatus.BLOCKED) {
            return false;
        }
        
        if (pin.equals(inputPin)) {
            failedLoginAttempts = 0;
            return true;
        } else {
            failedLoginAttempts++;
            if (failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
                loginStatus = AccountStatus.BLOCKED;
                System.out.println("\nAccount blocked due to " + MAX_FAILED_ATTEMPTS + 
                                 " consecutive failed login attempts.");
                System.out.println("Please contact bank administrator to unblock your account.");
            }
            return false;
        }
    }
    
    public void resetFailedAttempts() {
        this.failedLoginAttempts = 0;
        this.loginStatus = AccountStatus.ACTIVE;
    }
    
    public BankAccount getAccountByNumber(String accountNumber) {
        for (BankAccount account : accounts) {
            if (account.getAccountNumber().equals(accountNumber)) {
                return account;
            }
        }
        return null;
    }
    
    public void displayAccounts() {
        if (accounts.isEmpty()) {
            System.out.println("No accounts found for this customer.");
            return;
        }
        
        System.out.println("\n===== Accounts for " + name + " (ID: " + customerId + ") =====");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }
    }
    
    @Override
    public String toString() {
        return String.format("Customer ID: %s, Name: %s, Accounts: %d, Status: %s",
                           customerId, name, accounts.size(), loginStatus);
    }
}
