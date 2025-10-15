# Quick Start Guide - Banking System

## 🚀 Running the Application

### Step 1: Compile (Already Done!)
```bash
javac *.java
```

### Step 2: Run the Application
```bash
java BankingSystemMain
```

## 🎯 Quick Test Scenarios

### Scenario 1: Customer Banking Operations
1. Run `java BankingSystemMain`
2. Select option **1** (Customer ATM Login)
3. Login with:
   - Customer ID: `C001`
   - PIN: `1234`
4. Try these operations:
   - Check balance
   - Deposit $100
   - Withdraw $50
   - View transaction history

### Scenario 2: Transfer Between Own Accounts
1. Login as Customer C001 (Alice - has both Savings and Checking)
2. Select **4** (Transfer Funds)
3. Select **1** (Transfer to own account)
4. Choose destination account
5. Enter amount: `200`

### Scenario 3: Transfer to Another Customer
1. Login as Customer C001
2. Select **4** (Transfer Funds)
3. Select **2** (Transfer to another customer)
4. Enter destination account (e.g., `SAV10001` - Bob's savings)
5. Enter amount: `100`

### Scenario 4: Test Account Blocking (Security)
1. Select option **1** (Customer ATM Login)
2. Enter Customer ID: `C002`
3. Enter **wrong PIN** three times
4. Account will be blocked
5. Try to login again - should see blocked message

### Scenario 5: Admin - Unblock Customer
1. From main menu, select **2** (Bank Administrator Login)
2. Login with:
   - Username: `admin`
   - Password: `password`
3. Select **4** (Unblock Customer Account)
4. Enter Customer ID: `C002`
5. Confirm unblock

### Scenario 6: Admin - Create New Account
1. Login as admin
2. Select **3** (Create New Account)
3. Enter Customer ID: `C003`
4. Select account type: **2** (Checking)
5. Enter initial deposit: `1000`

### Scenario 7: Test Savings Minimum Balance
1. Login as Customer C003
2. Try to withdraw more than (balance - $500)
3. Should see minimum balance violation error

### Scenario 8: Test Checking Overdraft
1. Login as Customer C001
2. Select Checking account
3. Try to withdraw amount that makes balance go negative
4. Should succeed up to $1000 overdraft
5. Try to exceed $1000 overdraft - should fail

## 📊 Quick Reference

### Sample Accounts Created at Startup

**Customer C001 (Alice Johnson)**
- Savings Account: SAV10001 - Balance: $5,000
- Checking Account: CHK10002 - Balance: $2,000

**Customer C002 (Bob Smith)**
- Savings Account: SAV10003 - Balance: $10,000
- Checking Account: CHK10004 - Balance: $1,500

**Customer C003 (Charlie Brown)**
- Savings Account: SAV10005 - Balance: $3,000

## 💡 Tips

1. **Transaction History**: After any operation, check transaction history to see logged transactions
2. **Receipt Printing**: Every successful transaction prints a receipt automatically
3. **Failed Transactions**: Even failed operations are logged - check history to see them
4. **Multiple Accounts**: Customers with multiple accounts will be prompted to select one
5. **Account Types**: Notice different behaviors:
   - Savings: Can't go below $500
   - Checking: Can go negative up to $1000

## 🔧 Troubleshooting

**Problem**: "Class not found" error
- **Solution**: Make sure you're in the correct directory and have compiled all files

**Problem**: Account is blocked
- **Solution**: Use admin portal to unblock the customer

**Problem**: Transfer fails
- **Solution**: Check account numbers, ensure sufficient funds, verify destination account exists

## 📝 What to Observe

1. **Security**: 3 failed login attempts → account blocks
2. **Business Rules**: 
   - Savings minimum balance enforcement
   - Checking overdraft limits
3. **Transaction Logging**: All operations logged with status
4. **Polymorphism**: Same operations work differently for Savings vs Checking
5. **Data Structures**: Fast lookups for customers and accounts

## 🎓 Learning Points

- **OOP**: Inheritance (SavingsAccount extends BankAccount)
- **Polymorphism**: Different withdraw() behavior per account type
- **Data Structures**: HashMap for O(1) lookups, ArrayList for ordered collections
- **Error Handling**: Custom exceptions with meaningful messages
- **Design Patterns**: Repository (Bank), Facade (Bank interface)

## ⚡ Quick Commands

```bash
# Compile
javac *.java

# Run
java BankingSystemMain

# Clean (remove class files)
rm *.class

# View files
ls -la
```

---

**Enjoy exploring the banking system!** 🏦
