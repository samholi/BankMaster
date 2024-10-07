package in.softgrid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.softgrid.entity.Account;
import in.softgrid.entity.Transaction;
import in.softgrid.repositary.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;
    
    
    
    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        // Use the repository method to fetch transactions by account ID
        return transactionRepository.findByAccountId(accountId);
    }
    
 // Method to get the latest transaction for a specific account
    public Transaction findLatestTransactionForAccount(String accountNo) {
        Pageable pageable = PageRequest.of(0, 1); // Fetch the latest transaction
        List<Transaction> transactions = transactionRepository.findLatestTransactionForAccount(accountNo, pageable);
        return transactions.isEmpty() ? null : transactions.get(0);
    }
    
    

    @Transactional 
    public void creditTransaction(String accountNo, long amount,String transactionType,LocalDate transactionDate) {
        
        Account account = accountService.findAccountByAccountNo(accountNo);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNo);
        }

     // Fetch all previous transactions to calculate total amount
        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

     // Check if there are no previous transactions
        long totalAmount;
        if (previousTransactions.isEmpty()) {
            // If no previous transactions, use the deposit amount from the account and add the new amount
            totalAmount = account.getDepositAmount() + amount;
        } else {
            // Get the last transaction to find the previous total amount
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
            // Set the new total amount based on the last transaction's total amount
            totalAmount = lastTransaction.getTotalAmount() + amount;
        }

    
        Transaction transaction = new Transaction();
        transaction.setTransactionType("Credit");
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDate(transactionDate);
        transaction.setTotalAmount(totalAmount); 
        
       
        transaction.setAccount(account);

        
        transactionRepository.save(transaction);
    }
    
    
    
    
    
    
    @Transactional 
    public void depositTransaction(String accountNo, long amount,String transactionType,LocalDate transactionDate) {
        
        Account account = accountService.findAccountByAccountNo(accountNo);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNo);
        }

     // Fetch all previous transactions to calculate total amount
        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

     // Check if there are no previous transactions
        long totalAmount;
        if (previousTransactions.isEmpty()) {
            // If no previous transactions, use the deposit amount from the account and add the new amount
            totalAmount = account.getDepositAmount() + amount;
        } else {
            // Get the last transaction to find the previous total amount
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
            // Set the new total amount based on the last transaction's total amount
            totalAmount = lastTransaction.getTotalAmount() + amount;
        }

    
        Transaction transaction = new Transaction();
        transaction.setTransactionType("Deposit");
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDate(transactionDate);
        transaction.setTotalAmount(totalAmount); 
        
       
        transaction.setAccount(account);

        
        transactionRepository.save(transaction);
    }
    
    
   
    
    
    
    @Transactional
    public void withdrawTransaction(String accountNo, long amount, String transactionType, LocalDate transactionDate) {

        // Fetch the account by account number
    	
        Account account = accountService.findAccountByAccountNo(accountNo);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNo);
        }

        // Fetch all previous transactions to calculate the total amount
        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

        // Calculate the current total amount
        long totalAmount;
        if (previousTransactions.isEmpty()) {
            // If no previous transactions, use the deposit amount from the account
            totalAmount = account.getDepositAmount();
        } else {
            // Get the last transaction to find the previous total amount
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
            totalAmount = lastTransaction.getTotalAmount();
        }

        // Calculate remaining balance after withdrawal
        long remainingBalance = totalAmount - amount;

        // Ensure that the remaining balance is not less than the hold amount
        if (remainingBalance < account.getHold()) {
            throw new RuntimeException("Withdrawal denied! The remaining balance after withdrawal cannot be less than the hold amount.");
        }

        // Update the total amount after withdrawal
        totalAmount = totalAmount - amount;

        // Create and save a new transaction
        Transaction transaction=new Transaction();
        transaction.setTransactionType("WithDraw");
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDate(transactionDate);
        transaction.setTotalAmount(totalAmount); 
        transaction.setAccount(account); // Associate the account with the transaction

        
        transactionRepository.save(transaction);

    }

    
    
    
    
    @Transactional
    public void debitTransaction(String accountNo, long amount, String transactionType, LocalDate transactionDate) {

        // Fetch the account by account number
    	
        Account account = accountService.findAccountByAccountNo(accountNo);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNo);
        }

        // Fetch all previous transactions to calculate the total amount
        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

        // Calculate the current total amount
        long totalAmount;
        if (previousTransactions.isEmpty()) {
            // If no previous transactions, use the deposit amount from the account
            totalAmount = account.getDepositAmount();
        } else {
            // Get the last transaction to find the previous total amount
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
            totalAmount = lastTransaction.getTotalAmount();
        }

        // Calculate remaining balance after withdrawal
        long remainingBalance = totalAmount - amount;

        // Ensure that the remaining balance is not less than the hold amount
        if (remainingBalance < account.getHold()) {
            throw new RuntimeException("Debit denied! The remaining balance after withdrawal cannot be less than the hold amount.");
        }

        // Update the total amount after withdrawal
        totalAmount = totalAmount - amount;

        // Create and save a new transaction
        Transaction transaction=new Transaction();
        transaction.setTransactionType("WithDraw");
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDate(transactionDate);
        transaction.setTotalAmount(totalAmount); 
        transaction.setAccount(account); // Associate the account with the transaction

        
        transactionRepository.save(transaction);

    }


    
    
    
    
}
