# Banking System - Open Ended Lab

A comprehensive Java-based banking system implementing advanced OOP concepts, complex data structures, and robust error handling.

## 📋 Project Overview

This project implements a full-featured banking system with:
- Multiple customer management
- Different account types (Savings and Checking)
- ATM interface for customer operations
- Bank administrator portal
- Complete transaction logging and history
- Security features (PIN authentication, account blocking)
- Fund transfer capabilities (intra-customer and cross-customer)

## 🏗️ System Architecture

The system follows a **multi-layered architecture**:
- **Presentation Layer**: ATM and BankAdministrator interfaces
- **Business Logic Layer**: Bank orchestrator
- **Domain Layer**: Customer, BankAccount, Transaction entities

## 📁 Project Structure

```
SC OEL/
├── BankingSystemMain.java      # Main application entry point
├── Bank.java                    # Central bank system repository
├── Customer.java                # Customer entity
├── BankAccount.java             # Abstract account base class
├── SavingsAccount.java          # Savings account implementation
├── CheckingAccount.java         # Checking account implementation
├── Transaction.java             # Transaction entity
├── ATM.java                     # Customer ATM interface
├── BankAdministrator.java       # Admin portal interface
├── TransactionType.java         # Transaction type enum
├── TransactionStatus.java       # Transaction status enum
├── AccountStatus.java           # Account status enum
├── InsufficientFundsException.java
├── InvalidAccountException.java
├── AccountBlockedException.java
├── MinimumBalanceException.java
├── DESIGN.md                    # Comprehensive design documentation
└── README.md                    # This file
```

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Terminal/Command Prompt

### Compilation

Navigate to the project directory and compile all Java files:

```bash
cd "/Users/talha/Desktop/SC OEL"
javac -d out src/enums/*.java src/exceptions/*.java src/models/*.java src/ui/*.java src/Main.java
```

### Execution

Run the main application:

```bash
java -cp out Main
```

## 👤 Sample Credentials

### Customer Accounts

The system initializes with three sample customers:

| Customer ID | PIN  | Name           | Accounts                |
|-------------|------|----------------|-------------------------|
| C001        | 1234 | Alice Johnson  | Savings + Checking      |
| C002        | 5678 | Bob Smith      | Savings + Checking      |
| C003        | 9012 | Charlie Brown  | Savings                 |

### Administrator Access

| Username | Password  |
|----------|-----------|
| admin    | password  |

## 💡 Features

### Task 1: Core Banking System

#### Customer Management
- ✓ Unique customer IDs and names
- ✓ PIN-based authentication
- ✓ Multiple accounts per customer

#### Account Types
- **Savings Account**
  - Minimum balance requirement: $500
  - Interest calculation support (3% annual rate)
  - Enforces minimum balance on withdrawals
  
- **Checking Account**
  - Overdraft protection: up to $1000 negative balance
  - Transaction fees after 10 free transactions/month
  - Flexible withdrawal limits

#### Transaction System
- Complete transaction logging with:
  - Transaction ID, type, amount, timestamp
  - Source and destination accounts
  - Success/failure status
  - Balance after transaction
- Chronological transaction history viewing
- Receipt printing for successful transactions

#### ATM Features
- Secure login with PIN
- Account blocking after 3 failed login attempts
- Account selection for multi-account customers
- Balance inquiry
- Deposit funds
- Withdraw funds (with account-specific rules)
- View transaction history
- Receipt printing

### Task 2: Advanced Features

#### Fund Transfers
- **Intra-Customer Transfers**: Transfer between your own accounts
- **Cross-Customer Transfers**: Transfer to other customers' accounts
- Real-time validation and error handling
- Complete transaction logging for both accounts

#### Bank Administrator Portal
- View all customers with account counts and status
- View all accounts with balances
- Create new accounts for existing customers
- Unblock customer accounts
- View bank statistics and reports

## 🎮 How to Use

### Customer ATM Operations

1. **Login**
   - Select "Customer ATM Login" from main menu
   - Enter Customer ID and PIN
   - System validates and grants access or blocks after 3 failed attempts

2. **Check Balance**
   - View current balance, account type, and limits

3. **Deposit Funds**
   - Enter amount to deposit
   - Receive confirmation and receipt

4. **Withdraw Funds**
   - Enter amount to withdraw
   - System enforces account-specific rules
   - Receive confirmation and receipt

5. **Transfer Funds**
   - Choose transfer type (own account or other customer)
   - Enter destination account and amount
   - System validates and processes transfer

6. **View Transaction History**
   - See chronological list of all transactions
   - Includes successful and failed transactions

### Administrator Operations

1. **Login**
   - Select "Bank Administrator Login"
   - Enter admin credentials (admin/password)

2. **View Customers/Accounts**
   - See complete lists with details

3. **Create New Account**
   - Select customer by ID
   - Choose account type
   - Enter initial deposit

4. **Unblock Customer**
   - Enter blocked customer ID
   - Confirm unblock action

## 🔒 Security Features

- **PIN Authentication**: Secure customer verification
- **Account Blocking**: Automatic blocking after 3 failed login attempts
- **Admin-Only Unblocking**: Only administrators can unblock accounts
- **Transaction Validation**: All operations validated before execution
- **Complete Audit Trail**: All transactions (success and failure) logged

## 🏛️ Business Rules

### Savings Account
- Minimum balance: $500.00
- Withdrawals below minimum balance are rejected
- Interest: 3% annual rate (can be applied)

### Checking Account
- Overdraft limit: $1000.00
- Negative balance up to limit allowed
- Transaction fee: $1.50 after 10 free transactions

### Transfers
- Both accounts must exist and be active
- Source must have sufficient funds
- All account-specific rules apply

## 📊 Data Structures Used

| Structure              | Purpose                          | Justification                |
|------------------------|----------------------------------|------------------------------|
| HashMap<String, Customer> | Store customers by ID         | O(1) lookup time             |
| HashMap<String, BankAccount> | Store accounts by number   | O(1) lookup for transfers    |
| ArrayList<BankAccount> | Customer's accounts              | Ordered, small collection    |
| ArrayList<Transaction> | Transaction history              | Chronological order          |

## 🎯 OOP Concepts Demonstrated

- **Inheritance**: BankAccount → SavingsAccount, CheckingAccount
- **Polymorphism**: ATM works with BankAccount interface
- **Encapsulation**: Private fields with public methods
- **Abstraction**: Abstract BankAccount class
- **Composition**: Bank HAS-A Customers, Customer HAS-A Accounts
- **Custom Exceptions**: Business logic error handling

## 🧪 Testing Scenarios

1. **Login Security**
   - Try wrong PIN 3 times → Account should block
   - Try admin login with wrong credentials

2. **Savings Account**
   - Try to withdraw below minimum balance → Should fail
   - Deposit and withdraw within limits → Should succeed

3. **Checking Account**
   - Withdraw into overdraft → Should succeed
   - Withdraw beyond overdraft → Should fail

4. **Transfers**
   - Transfer between own accounts → Should succeed
   - Transfer to non-existent account → Should fail
   - Transfer with insufficient funds → Should fail

5. **Admin Functions**
   - Create new account with valid/invalid customer ID
   - Unblock a blocked customer
   - View statistics

## 📝 Error Handling

The system handles various error scenarios:
- Invalid input (non-numeric amounts)
- Insufficient funds
- Minimum balance violations
- Overdraft limit exceeded
- Invalid account numbers
- Blocked accounts
- Non-existent customers

All errors provide meaningful messages and are logged appropriately.

## 🔄 Extensibility

The system is designed for easy extension:
- Add new account types by extending BankAccount
- Add new transaction types to TransactionType enum
- Add new admin functions to BankAdministrator
- Implement interest calculation automation
- Add more sophisticated fee structures

## 📖 Documentation

See `DESIGN.md` for:
- Detailed class diagrams
- Architecture explanation
- Design pattern justification
- Data structure rationale
- Inheritance and polymorphism details
- Complete feature documentation

## 👨‍💻 Author

This banking system was developed as part of an Object-Oriented Programming lab exercise, demonstrating advanced Java programming concepts and software design principles.

## 📄 License

This is an educational project for learning purposes.

---

**Note**: This is a simulation system for educational purposes. It uses simplified security (hardcoded admin credentials, plain text PINs) which would not be appropriate for a production banking system.
