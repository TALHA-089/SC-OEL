package ui;

import models.*;
import enums.AccountStatus;

import java.util.Scanner;
import java.util.ArrayList;


public class BankAdministrator {
    private Bank bank;
    private Scanner scanner;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "password";

    public BankAdministrator(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("\n========================================");
        System.out.println("   " + bank.getBankName() + " - Admin Portal");
        System.out.println("========================================\n");
        
        if (login()) {
            showAdminMenu();
        }
    }

    private boolean login() {
        System.out.print("Enter Admin Username: ");
        String username = scanner.nextLine().trim();
        
        System.out.print("Enter Admin Password: ");
        String password = scanner.nextLine().trim();
        
        if (username.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)) {
            System.out.println("\n✓ Admin login successful!");
            return true;
        } else {
            System.out.println("\n❌ Invalid admin credentials.");
            return false;
        }
    }

    private void showAdminMenu() {
        while (true) {
            System.out.println("\n========== Admin Menu ==========");
            System.out.println("1. View All Customers");
            System.out.println("2. View All Accounts");
            System.out.println("3. Create New Account");
            System.out.println("4. Unblock Customer Account");
            System.out.println("5. View Bank Statistics");
            System.out.println("6. Logout");
            System.out.println("================================");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    viewAllCustomers();
                    break;
                case "2":
                    viewAllAccounts();
                    break;
                case "3":
                    createNewAccount();
                    break;
                case "4":
                    unblockCustomer();
                    break;
                case "5":
                    viewBankStatistics();
                    break;
                case "6":
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private void viewAllCustomers() {
        ArrayList<Customer> customers = bank.getAllCustomers();
        
        if (customers.isEmpty()) {
            System.out.println("\nNo customers found.");
            return;
        }
        
        System.out.println("\n========== All Customers ==========");
        System.out.println(String.format("%-10s %-20s %-10s %-15s", 
                                       "ID", "Name", "Accounts", "Status"));
        System.out.println("-----------------------------------------------------------");
        
        for (Customer customer : customers) {
            System.out.println(String.format("%-10s %-20s %-10d %-15s",
                                           customer.getCustomerId(),
                                           customer.getName(),
                                           customer.getAccounts().size(),
                                           customer.getLoginStatus()));
        }
        
        System.out.println("\nTotal Customers: " + customers.size());
    }

    private void viewAllAccounts() {
        ArrayList<BankAccount> accounts = bank.getAllAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("\nNo accounts found.");
            return;
        }
        
        System.out.println("\n========== All Accounts ==========");
        System.out.println(String.format("%-15s %-12s %-12s %-12s %-15s", 
                                       "Account No", "Type", "Customer ID", "Balance", "Status"));
        System.out.println("------------------------------------------------------------------------");
        
        for (BankAccount account : accounts) {
            System.out.println(String.format("%-15s %-12s %-12s $%-11.2f %-15s",
                                           account.getAccountNumber(),
                                           account.getAccountType(),
                                           account.getCustomerId(),
                                           account.getBalance(),
                                           account.getStatus()));
        }
        
        System.out.println("\nTotal Accounts: " + accounts.size());
    }
    
    private void createNewAccount() {
        System.out.println("\n===== Create New Account =====");
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine().trim();
        
        Customer customer = bank.getCustomer(customerId);
        if (customer == null) {
            System.out.println("❌ Customer not found with ID: " + customerId);
            return;
        }
        
        System.out.println("Customer: " + customer.getName());
        System.out.println("\nSelect Account Type:");
        System.out.println("1. Savings Account");
        System.out.println("2. Checking Account");
        System.out.print("Enter choice: ");
        
        String typeChoice = scanner.nextLine().trim();
        
        System.out.print("Enter initial deposit amount: $");
        try {
            double initialBalance = Double.parseDouble(scanner.nextLine().trim());
            
            BankAccount newAccount = null;
            
            if (typeChoice.equals("1")) {
                newAccount = bank.createSavingsAccount(customerId, initialBalance);
                System.out.println("\n✓ Savings account created successfully!");
            } else if (typeChoice.equals("2")) {
                newAccount = bank.createCheckingAccount(customerId, initialBalance);
                System.out.println("\n✓ Checking account created successfully!");
            } else {
                System.out.println("❌ Invalid account type selection.");
                return;
            }
            
            if (newAccount != null) {
                System.out.println("Account Number: " + newAccount.getAccountNumber());
                System.out.println("Initial Balance: $" + String.format("%.2f", initialBalance));
            }
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount.");
        } catch (Exception e) {
            System.out.println("❌ Account creation failed: " + e.getMessage());
        }
    }
    
    private void unblockCustomer() {
        System.out.println("\n===== Unblock Customer Account =====");
        System.out.print("Enter Customer ID to unblock: ");
        String customerId = scanner.nextLine().trim();
        
        try {
            Customer customer = bank.getCustomer(customerId);
            if (customer == null) {
                System.out.println("❌ Customer not found with ID: " + customerId);
                return;
            }
            
            System.out.println("Customer: " + customer.getName());
            System.out.println("Current Status: " + customer.getLoginStatus());
            System.out.println("Failed Attempts: " + customer.getFailedLoginAttempts());
            
            if (customer.getLoginStatus() != AccountStatus.BLOCKED) {
                System.out.println("Customer account is not blocked.");
                return;
            }
            
            System.out.print("Confirm unblock? (yes/no): ");
            String confirm = scanner.nextLine().trim().toLowerCase();
            
            if (confirm.equals("yes") || confirm.equals("y")) {
                bank.unblockCustomer(customerId);
                System.out.println("✓ Customer account unblocked successfully!");
            } else {
                System.out.println("Operation cancelled.");
            }
            
        } catch (Exception e) {
            System.out.println("❌ Unblock failed: " + e.getMessage());
        }
    }
    
    private void viewBankStatistics() {
        ArrayList<Customer> customers = bank.getAllCustomers();
        ArrayList<BankAccount> accounts = bank.getAllAccounts();
        
        double totalBalance = 0;
        int savingsCount = 0;
        int checkingCount = 0;
        int blockedCustomers = 0;
        
        for (Customer customer : customers) {
            if (customer.getLoginStatus() == AccountStatus.BLOCKED) {
                blockedCustomers++;
            }
        }
        
        for (BankAccount account : accounts) {
            totalBalance += account.getBalance();
            if (account instanceof SavingsAccount) {
                savingsCount++;
            } else if (account instanceof CheckingAccount) {
                checkingCount++;
            }
        }
        
        System.out.println("\n========== Bank Statistics ==========");
        System.out.println("Bank Name: " + bank.getBankName());
        System.out.println("Total Customers: " + customers.size());
        System.out.println("Blocked Customers: " + blockedCustomers);
        System.out.println("Total Accounts: " + accounts.size());
        System.out.println("  - Savings Accounts: " + savingsCount);
        System.out.println("  - Checking Accounts: " + checkingCount);
        System.out.println("Total Bank Balance: $" + String.format("%.2f", totalBalance));
        System.out.println("=====================================");
    }
    
    private void logout() {
        System.out.println("\nLogging out from Admin Portal...");
        System.out.println("Goodbye!");
    }
}
