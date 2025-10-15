# Banking System - Design Documentation

## 1. System Overview

This is a comprehensive Java-based banking system that simulates real-world banking operations including customer management, multiple account types, transaction logging, ATM interface, and administrative functions.

The system is built using advanced Object-Oriented Programming (OOP) principles, complex data structures, and robust error handling mechanisms.

---

## 2. Architecture Design

### 2.1 Multi-Layered Architecture

The system follows a **three-tier architecture**:

1. **Presentation Layer**: ATM and BankAdministrator classes (user interfaces)
2. **Business Logic Layer**: Bank class (orchestrator and business rules)
3. **Data/Domain Layer**: Customer, BankAccount, Transaction classes (entities)

This separation ensures:
- **High Cohesion**: Each class has a single, well-defined responsibility
- **Loose Coupling**: Changes in one layer minimally impact others
- **Maintainability**: Easy to modify and extend functionality

---

## 3. Class Diagram (Text-Based UML)

```
┌─────────────────────────────────────────────────────────────────┐
│                      BankingSystemMain                          │
│  - main(String[] args): void                                    │
└───────────────────┬─────────────────────────────────────────────┘
                    │ creates
                    ├─────────────────┬────────────────────┐
                    │                 │                    │
            ┌───────▼──────┐  ┌──────▼───────┐  ┌────────▼────────┐
            │     Bank     │  │     ATM      │  │ BankAdministrator│
            ├──────────────┤  ├──────────────┤  ├─────────────────┤
            │ -bankName    │◄─┤ -bank        │  │ -bank           │
            │ -customers   │  │ -scanner     │  │ -scanner        │
            │ -accounts    │  │ -current...  │  └─────────────────┘
            ├──────────────┤  │ -selected... │
            │+registerCust │  ├──────────────┤
            │+createSavings│  │+start()      │
            │+createCheckin│  │-login()      │
            │+getCustomer  │  │-showMainMenu │
            │+getAccount   │  │-checkBalance │
            │+authenticate │  │-deposit...   │
            │+transferFunds│  │-withdraw...  │
            │+getAllCustom │  │-transfer...  │
            │+getAllAccoun │  └──────────────┘
            │+unblockCust  │
            │+initialize.. │
            └──────┬───────┘
                   │ aggregates (1:N)
          ┌────────┼────────────────┐
          │                         │
    ┌─────▼────────┐         ┌─────▼──────────┐
    │   Customer   │         │  BankAccount   │ (Abstract)
    ├──────────────┤         ├────────────────┤
    │-customerId   │         │#accountNumber  │
    │-name         │         │#customerId     │
    │-pin          │         │#balance        │
    │-accounts     │◄────────┤#status         │
    │-loginStatus  │         │#transactionHist│
    │-failedLogin..│         ├────────────────┤
    ├──────────────┤         │+deposit()      │ (Abstract)
    │+validatePin  │         │+withdraw()     │ (Abstract)
    │+addAccount   │         │+getBalance()   │
    │+resetFailed..│         │+getAccountType │ (Abstract)
    │+displayAccoun│         │+displayTxnHist │
    └──────────────┘         └────────┬───────┘
                                      │ extends
                              ┌───────┴────────┐
                              │                │
                    ┌─────────▼──────┐  ┌─────▼──────────┐
                    │SavingsAccount  │  │CheckingAccount │
                    ├────────────────┤  ├────────────────┤
                    │-MINIMUM_BALANCE│  │-OVERDRAFT_LIMIT│
                    │-INTEREST_RATE  │  │-TRANSACTION_FEE│
                    ├────────────────┤  ├────────────────┤
                    │+deposit()      │  │+deposit()      │
                    │+withdraw()     │  │+withdraw()     │
                    │+applyInterest()│  │+getOverdraft..│
                    └────────────────┘  └────────────────┘

    ┌──────────────────┐
    │   Transaction    │◄──────────────┐
    ├──────────────────┤                │
    │-transactionId    │                │
    │-type             │                │ aggregates (1:N)
    │-amount           │                │
    │-timestamp        │                │
    │-sourceAccount    │                │
    │-destinationAcct  │     ┌──────────┴─────┐
    │-status           │     │  BankAccount   │
    │-balanceAfter     │     │                │
    ├──────────────────┤     └────────────────┘
    │+toString()       │
    │+toReceipt()      │
    └──────────────────┘

    ┌───────────────────┐      ┌─────────────────┐
    │ TransactionType   │      │ TransactionStatus│
    ├───────────────────┤      ├─────────────────┤
    │ DEPOSIT           │      │ SUCCESS         │
    │ WITHDRAWAL        │      │ FAILED_...      │
    │ TRANSFER_IN       │      │                 │
    │ TRANSFER_OUT      │      └─────────────────┘
    └───────────────────┘

    ┌────────────────────┐
    │  AccountStatus     │
    ├────────────────────┤
    │ ACTIVE             │
    │ BLOCKED            │
    │ CLOSED             │
    └────────────────────┘

    Custom Exceptions:
    ┌────────────────────────┐
    │InsufficientFundsExcept │
    │InvalidAccountException │
    │AccountBlockedException │
    │MinimumBalanceException │
    └────────────────────────┘
```

---

## 4. Key Relationships

### 4.1 Composition Relationships
- **Bank** HAS-A collection of **Customers** (HashMap)
- **Bank** HAS-A collection of **BankAccounts** (HashMap)
- **Customer** HAS-A collection of **BankAccounts** (ArrayList)
- **BankAccount** HAS-A collection of **Transactions** (ArrayList)

### 4.2 Aggregation Relationships
- **ATM** uses **Bank** (dependency)
- **BankAdministrator** uses **Bank** (dependency)

### 4.3 Inheritance Relationships
- **SavingsAccount** extends **BankAccount**
- **CheckingAccount** extends **BankAccount**
- All custom exceptions extend **Exception**

---

## 5. Data Structure Justification

### 5.1 HashMap<String, Customer> in Bank
**Purpose**: Store and retrieve customers by their unique customer ID

**Justification**:
- **O(1) average time complexity** for lookup operations
- Customer IDs are unique, making them ideal hash keys
- Frequent lookups during authentication and admin operations
- No need for ordered traversal of customers

### 5.2 HashMap<String, BankAccount> in Bank
**Purpose**: Store and retrieve accounts by their unique account number

**Justification**:
- **O(1) average time complexity** for account lookup
- Critical for transfer operations where destination accounts must be validated quickly
- Account numbers are unique and immutable
- Enables efficient cross-customer transfers

### 5.3 ArrayList<BankAccount> in Customer
**Purpose**: Store all accounts belonging to a customer

**Justification**:
- **Ordered collection** maintains account creation sequence
- Small number of accounts per customer (typically 1-5)
- Simple iteration when displaying customer accounts
- Dynamic sizing as customers open new accounts

### 5.4 ArrayList<Transaction> in BankAccount
**Purpose**: Maintain chronological transaction history

**Justification**:
- **Preserves insertion order** for chronological display
- Supports sequential access for transaction history viewing
- Dynamic growth as transactions accumulate
- Simple append operation for new transactions

---

## 6. Inheritance and Polymorphism

### 6.1 Abstract BankAccount Class
The `BankAccount` abstract class defines the **common interface** for all account types:

```java
public abstract class BankAccount {
    // Common properties
    protected String accountNumber;
    protected double balance;
    
    // Abstract methods - must be implemented by subclasses
    public abstract void deposit(double amount) throws Exception;
    public abstract void withdraw(double amount) throws Exception;
    public abstract String getAccountType();
}
```

**Benefits**:
- Enforces a contract that all account types must follow
- Allows polymorphic treatment of different account types
- Code reuse through shared properties and methods

### 6.2 Concrete Implementations

**SavingsAccount**:
- Overrides `withdraw()` to enforce minimum balance requirement
- Adds `applyInterest()` method for interest calculation

**CheckingAccount**:
- Overrides `withdraw()` to allow overdraft up to a limit
- Adds transaction fee logic for excessive transactions

### 6.3 Polymorphism in Action

**Example 1: ATM Operations**
```java
BankAccount selectedAccount = customer.getAccounts().get(0);
selectedAccount.deposit(amount);  // Calls appropriate implementation
selectedAccount.withdraw(amount); // Enforces account-specific rules
```

The ATM doesn't need to know the specific account type - it works with the `BankAccount` interface, and the correct behavior is invoked at runtime.

**Example 2: Bank Transfer Operations**
```java
public void transferFunds(String source, String dest, double amount) {
    BankAccount sourceAccount = accounts.get(source);
    BankAccount destAccount = accounts.get(dest);
    
    sourceAccount.withdraw(amount);  // Polymorphic call
    destAccount.deposit(amount);     // Polymorphic call
}
```

---

## 7. Error Handling Strategy

### 7.1 Custom Exceptions

**Purpose**: Provide specific, meaningful error messages for business logic failures

1. **InsufficientFundsException**: Withdrawal/transfer exceeds available balance
2. **InvalidAccountException**: Account not found or invalid
3. **AccountBlockedException**: Account blocked due to security reasons
4. **MinimumBalanceException**: Savings account minimum balance violation

### 7.2 Exception Handling Pattern

**Input Validation**: 
- Try-catch blocks for parsing user input (NumberFormatException)
- Validation of positive amounts, valid account numbers

**Business Logic Errors**:
- Custom exceptions thrown from domain classes
- Caught and handled in UI layer (ATM/Admin)
- Failed transactions recorded in transaction history

**Transaction Recording**:
```java
try {
    balance -= amount;
    addTransaction(new Transaction(..., SUCCESS, balance));
} catch (InsufficientFundsException e) {
    addTransaction(new Transaction(..., FAILED_INSUFFICIENT_FUNDS, balance));
    throw e;
}
```

---

## 8. Security Features

### 8.1 PIN Authentication
- Customers authenticate using Customer ID and PIN
- Failed attempts tracked per customer
- Account blocked after 3 consecutive failed attempts

### 8.2 Account Blocking
- Automatic blocking prevents brute-force attacks
- Block is persistent until admin intervention
- Clear messaging to user about block status

### 8.3 Administrator Access
- Separate admin interface with hardcoded credentials
- Only admins can unblock customer accounts
- Admin functions completely separated from customer functions

---

## 9. Design Patterns Used

### 9.1 Repository Pattern
**Bank class** acts as a repository:
- Centralizes data access logic
- Abstracts storage implementation
- Provides query methods (getCustomer, getAccount)

### 9.2 Template Method Pattern
**BankAccount** defines the template:
- Common behavior in base class
- Specific behavior in subclasses
- Ensures consistent transaction recording

### 9.3 Facade Pattern
**Bank class** provides simplified interface:
- Hides complexity of customer/account management
- Single point of interaction for ATM and Admin
- Orchestrates complex operations (transfers)

---

## 10. Transaction Logging

### 10.1 Comprehensive Tracking
Every transaction records:
- Unique transaction ID (auto-generated)
- Transaction type (DEPOSIT, WITHDRAWAL, TRANSFER_IN, TRANSFER_OUT)
- Amount
- Timestamp (using java.time.LocalDateTime)
- Source and destination accounts
- Final status (SUCCESS or specific failure reason)
- Balance after transaction

### 10.2 Failed Transaction Recording
Even failed transactions are logged with:
- Reason for failure
- Balance at time of attempt
- This provides complete audit trail

---

## 11. Business Rules Implementation

### 11.1 Savings Account Rules
- **Minimum Balance**: $500.00 must be maintained
- Withdrawals that would drop below minimum are rejected
- Interest can be applied periodically (3% annual rate)

### 11.2 Checking Account Rules
- **Overdraft Limit**: Up to $1000 negative balance allowed
- Withdrawals beyond overdraft limit are rejected
- Transaction fees applied after 10 free transactions per month

### 11.3 Transfer Rules
- Both accounts must exist and be active
- Source account must have sufficient funds (respecting account-specific rules)
- Transfer recorded as TRANSFER_OUT in source, TRANSFER_IN in destination

---

## 12. Extensibility and Future Enhancements

### 12.1 Easy Extensions
The design supports easy addition of:
- New account types (MoneyMarketAccount, FixedDeposit)
- Additional transaction types (LOAN_PAYMENT, BILL_PAYMENT)
- More admin functions (account closure, customer reports)
- Interest calculation automation
- Transaction fee structures

### 12.2 Design for Change
- Abstract classes allow new implementations
- Enums for type-safe constants
- HashMap allows efficient scaling to many customers/accounts
- Separated UI from business logic allows different interfaces

---

## 13. Key Features Summary

### Task 1 Features:
✓ Customer management with unique IDs and PINs
✓ Multiple account types (Savings, Checking) with specific business rules
✓ Comprehensive transaction logging with complete details
✓ Secure ATM login with automatic blocking after 3 failed attempts
✓ Account selection for multi-account customers
✓ Core operations: balance check, deposit, withdrawal, transaction history
✓ Receipt printing for all successful transactions
✓ Bank backend with sample data initialization

### Task 2 Features:
✓ Intra-customer fund transfers (between own accounts)
✓ Cross-customer fund transfers (to other customers' accounts)
✓ Bank Administrator interface with separate authentication
✓ Admin functions: view customers, view accounts, create accounts, unblock accounts
✓ Bank statistics and reporting

---

## 14. Testing Recommendations

### Sample Test Cases:
1. **Successful Login**: Use C001/1234
2. **Failed Login Block**: Try 3 wrong PINs, verify account blocks
3. **Minimum Balance**: Try withdrawing from savings below $500
4. **Overdraft**: Withdraw from checking to test negative balance
5. **Transfer**: Transfer between own accounts and to other customers
6. **Admin Functions**: Create new account, unblock customer
7. **Transaction History**: Perform multiple operations and view history

---

## 15. Compilation and Execution

### Compilation:
```bash
javac *.java
```

### Execution:
```bash
java BankingSystemMain
```

### Sample Credentials:
**Customers:**
- Customer ID: C001, PIN: 1234 (Alice Johnson)
- Customer ID: C002, PIN: 5678 (Bob Smith)
- Customer ID: C003, PIN: 9012 (Charlie Brown)

**Administrator:**
- Username: admin
- Password: password

---

## End of Design Documentation
