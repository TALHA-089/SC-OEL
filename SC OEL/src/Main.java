import models.Bank;
import ui.ATM;
import ui.BankAdministrator;

import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        // Initialize the bank system
        Bank bank = new Bank("Global Trust Bank");
        
        // Initialize with sample data for testing
        bank.initializeSampleData();
        
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            displayWelcomeScreen();
            System.out.print("Select an option: ");
            String choice = scanner.nextLine().trim();
            
            switch (choice) {
                case "1":
                    // Customer ATM Interface
                    ATM atm = new ATM(bank);
                    atm.start();
                    break;
                    
                case "2":
                    // Bank Administrator Interface
                    BankAdministrator admin = new BankAdministrator(bank);
                    admin.start();
                    break;
                    
                case "3":
                    // Exit application
                    System.out.println("\nThank you for using Global Trust Bank System!");
                    System.out.println("Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                    
                default:
                    System.out.println("Invalid option. Please try again.\n");
            }
        }
    }
    
    private static void displayWelcomeScreen() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("          GLOBAL TRUST BANK SYSTEM");
        System.out.println("=".repeat(50));
        System.out.println("1. Customer ATM Login");
        System.out.println("2. Bank Administrator Login");
        System.out.println("3. Exit");
        System.out.println("=".repeat(50));
    }
}
