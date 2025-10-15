package ui;

import models.*;
import enums.AccountStatus;
import exceptions.AccountBlockedException;
import exceptions.InsufficientFundsException;
import exceptions.MinimumBalanceException;

import java.util.Scanner;
import java.util.ArrayList;


public class ATM {
    private Bank bank;
    private Scanner scanner;
    private Customer currentCustomer;
    private BankAccount selectedAccount;
    

    public ATM(Bank bank) {
        this.bank = bank;
        this.scanner = new Scanner(System.in);
        this.currentCustomer = null;
        this.selectedAccount = null;
    }
    
    public void start() {
        System.out.println("\n========================================");
        System.out.println("   Welcome to " + bank.getBankName() + " ATM");
        System.out.println("========================================\n");
        
        if (login()) {
            showMainMenu();
        }
    }
    
    private boolean login() {
        System.out.print("Enter Customer ID: ");
        String customerId = scanner.nextLine().trim();
        
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine().trim();
        
        try {
            currentCustomer = bank.authenticateCustomer(customerId, pin);
            
            if (currentCustomer == null) {
                System.out.println("\n❌ Invalid Customer ID or PIN.");
                Customer customer = bank.getCustomer(customerId);
                if (customer != null && customer.getLoginStatus() != AccountStatus.BLOCKED) {
                    int attemptsLeft = 3 - customer.getFailedLoginAttempts();
                    if (attemptsLeft > 0) {
                        System.out.println("Attempts remaining: " + attemptsLeft);
                    }
                }
                return false;
            }
            
            System.out.println("\n✓ Login successful! Welcome, " + currentCustomer.getName());
            return true;
            
        } catch (AccountBlockedException e) {
            System.out.println("\n❌ " + e.getMessage());
            return false;
        }
    }
    
    private void showMainMenu() {
        while (true) {
            // Select account if customer has multiple accounts
            if (!selectAccount()) {
                break;
            }
            
            System.out.println("\n========== ATM Main Menu ==========");
            System.out.println("Selected Account: " + selectedAccount.getAccountNumber() + 
                             " (" + selectedAccount.getAccountType() + ")");
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit Funds");
            System.out.println("3. Withdraw Funds");
            System.out.println("4. Transfer Funds");
            System.out.println("5. View Transaction History");
            System.out.println("6. Change Account");
            System.out.println("7. Logout");
            System.out.println("===================================");
            System.out.print("Select an option: ");
            
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    checkBalance();
                    break;
                case "2":
                    depositFunds();
                    break;
                case "3":
                    withdrawFunds();
                    break;
                case "4":
                    transferFunds();
                    break;
                case "5":
                    viewTransactionHistory();
                    break;
                case "6":
                    selectedAccount = null;  // Force account reselection
                    break;
                case "7":
                    logout();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    
    private boolean selectAccount() {
        if (selectedAccount != null) {
            return true;  // Account already selected
        }
        
        ArrayList<BankAccount> accounts = currentCustomer.getAccounts();
        
        if (accounts.isEmpty()) {
            System.out.println("\nNo accounts found. Please contact your bank.");
            return false;
        }
        
        if (accounts.size() == 1) {
            selectedAccount = accounts.get(0);
            return true;
        }
        
        // Multiple accounts - let customer choose
        System.out.println("\n===== Select Account =====");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }
        System.out.print("Select account number: ");
        
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice >= 1 && choice <= accounts.size()) {
                selectedAccount = accounts.get(choice - 1);
                return true;
            } else {
                System.out.println("Invalid selection.");
                return false;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return false;
        }
    }
    
    private void checkBalance() {
        System.out.println("\n===== Balance Inquiry =====");
        System.out.println("Account: " + selectedAccount.getAccountNumber());
        System.out.println("Type: " + selectedAccount.getAccountType());
        System.out.println("Current Balance: $" + String.format("%.2f", selectedAccount.getBalance()));
        
        if (selectedAccount instanceof SavingsAccount) {
            SavingsAccount savings = (SavingsAccount) selectedAccount;
            System.out.println("Minimum Balance Required: $" + String.format("%.2f", savings.getMinimumBalance()));
        } else if (selectedAccount instanceof CheckingAccount) {
            CheckingAccount checking = (CheckingAccount) selectedAccount;
            System.out.println("Overdraft Limit: $" + String.format("%.2f", checking.getOverdraftLimit()));
            double available = selectedAccount.getBalance() + checking.getOverdraftLimit();
            System.out.println("Available Balance (including overdraft): $" + String.format("%.2f", available));
        }
    }
    
    private void depositFunds() {
        System.out.println("\n===== Deposit Funds =====");
        System.out.print("Enter amount to deposit: $");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            selectedAccount.deposit(amount);
            
            System.out.println("\n✓ Deposit successful!");
            System.out.println("Amount Deposited: $" + String.format("%.2f", amount));
            System.out.println("New Balance: $" + String.format("%.2f", selectedAccount.getBalance()));
            
            printReceipt(selectedAccount.getTransactionHistory().get(
                selectedAccount.getTransactionHistory().size() - 1));
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount. Please enter a valid number.");
        } catch (Exception e) {
            System.out.println("❌ Deposit failed: " + e.getMessage());
        }
    }
    
    private void withdrawFunds() {
        System.out.println("\n===== Withdraw Funds =====");
        System.out.print("Enter amount to withdraw: $");
        
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            selectedAccount.withdraw(amount);
            
            System.out.println("\n✓ Withdrawal successful!");
            System.out.println("Amount Withdrawn: $" + String.format("%.2f", amount));
            System.out.println("New Balance: $" + String.format("%.2f", selectedAccount.getBalance()));
            
            printReceipt(selectedAccount.getTransactionHistory().get(
                selectedAccount.getTransactionHistory().size() - 1));
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount. Please enter a valid number.");
        } catch (InsufficientFundsException | MinimumBalanceException e) {
            System.out.println("❌ Withdrawal failed: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ Withdrawal failed: " + e.getMessage());
        }
    }
    
    private void transferFunds() {
        System.out.println("\n===== Transfer Funds =====");
        System.out.println("1. Transfer to my another account");
        System.out.println("2. Transfer to another customer's account");
        System.out.print("Select transfer type: ");
        
        String choice = scanner.nextLine().trim();
        
        if (choice.equals("1")) {
            transferToOwnAccount();
        } else if (choice.equals("2")) {
            transferToCrossCustomer();
        } else {
            System.out.println("Invalid option.");
        }
    }
    
    private void transferToOwnAccount() {
        ArrayList<BankAccount> accounts = currentCustomer.getAccounts();
        
        if (accounts.size() < 2) {
            System.out.println("You need at least 2 accounts to perform intra-account transfer.");
            return;
        }
        
        System.out.println("\n===== Your Accounts =====");
        for (int i = 0; i < accounts.size(); i++) {
            if (!accounts.get(i).getAccountNumber().equals(selectedAccount.getAccountNumber())) {
                System.out.println((i + 1) + ". " + accounts.get(i));
            }
        }
        
        System.out.print("Select destination account number: ");
        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > accounts.size()) {
                System.out.println("Invalid selection.");
                return;
            }
            
            BankAccount destinationAccount = accounts.get(choice - 1);
            
            if (destinationAccount.getAccountNumber().equals(selectedAccount.getAccountNumber())) {
                System.out.println("Cannot transfer to the same account.");
                return;
            }
            
            System.out.print("Enter amount to transfer: $");
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            bank.transferFunds(selectedAccount.getAccountNumber(), 
                             destinationAccount.getAccountNumber(), amount);
            
            System.out.println("\n✓ Transfer successful!");
            System.out.println("Amount Transferred: $" + String.format("%.2f", amount));
            System.out.println("From Account: " + selectedAccount.getAccountNumber());
            System.out.println("To Account: " + destinationAccount.getAccountNumber());
            System.out.println("New Balance: $" + String.format("%.2f", selectedAccount.getBalance()));
            
            printReceipt(selectedAccount.getTransactionHistory().get(
                selectedAccount.getTransactionHistory().size() - 1));
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid input.");
        } catch (Exception e) {
            System.out.println("❌ Transfer failed: " + e.getMessage());
        }
    }
    
    private void transferToCrossCustomer() {
        System.out.print("Enter destination account number: ");
        String destinationAccountNumber = scanner.nextLine().trim();
        
        // Verify destination account exists
        BankAccount destinationAccount = bank.getAccount(destinationAccountNumber);
        if (destinationAccount == null) {
            System.out.println("❌ Destination account not found.");
            return;
        }
        
        if (destinationAccount.getAccountNumber().equals(selectedAccount.getAccountNumber())) {
            System.out.println("❌ Cannot transfer to the same account.");
            return;
        }
        
        System.out.print("Enter amount to transfer: $");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            
            bank.transferFunds(selectedAccount.getAccountNumber(), 
                             destinationAccountNumber, amount);
            
            System.out.println("\n✓ Transfer successful!");
            System.out.println("Amount Transferred: $" + String.format("%.2f", amount));
            System.out.println("To Account: " + destinationAccountNumber);
            System.out.println("New Balance: $" + String.format("%.2f", selectedAccount.getBalance()));
            
            printReceipt(selectedAccount.getTransactionHistory().get(
                selectedAccount.getTransactionHistory().size() - 1));
            
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid amount.");
        } catch (Exception e) {
            System.out.println("❌ Transfer failed: " + e.getMessage());
        }
    }

    private void viewTransactionHistory() {
        selectedAccount.displayTransactionHistory();
    }

    private void printReceipt(Transaction transaction) {
        System.out.println(transaction.toReceipt());
    }
    
    private void logout() {
        System.out.println("\nThank you for using " + bank.getBankName() + " ATM.");
        System.out.println("Goodbye, " + currentCustomer.getName() + "!");
        currentCustomer = null;
        selectedAccount = null;
    }
}