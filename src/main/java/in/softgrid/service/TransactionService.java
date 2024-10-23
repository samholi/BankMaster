package in.softgrid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.softgrid.entity.Account;
import in.softgrid.entity.Hold;
import in.softgrid.entity.Transaction;
import in.softgrid.repositary.HoldRepository;
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
    private HoldRepository holdRepository;

    @Autowired
    private AccountService accountService;
    
    
    
    public double getLastTransactionTotalAmountByAccountNo(String accountNo) {
        List<Transaction> transactions = transactionRepository.findTopByAccount_AccountNoOrderByTransactionDateDesc(accountNo);
        if (!transactions.isEmpty()) {
            return transactions.get(0).getTotalAmount() != null ? transactions.get(0).getTotalAmount() : 0.0;
        }
        return 0.0; // Return 0.0 if no transactions found
    }
    
    
    
    
    public List<Transaction> findByAccountNo(String accountNo) {
        return transactionRepository.findByAccount_AccountNo(accountNo);
    }
    
    public List<Transaction> findTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccountId(accountId);
    }
    
    public Transaction findLatestTransactionForAccount(String accountNo) {
        Pageable pageable = PageRequest.of(0, 1); 
        List<Transaction> transactions = transactionRepository.findLatestTransactionForAccount(accountNo, pageable);
        return transactions.isEmpty() ? null : transactions.get(0);
    }
    
    public void save(Transaction transaction) {
        transactionRepository.save(transaction); 
    }
    
    

    @Transactional 
    public void creditTransaction(String accountNo, long amount,String transactionType,LocalDate transactionDate) {
        
        Account account = accountService.findAccountByAccountNo(accountNo);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNo);
        }

        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

        long totalAmount;
        if (previousTransactions.isEmpty()) {
            totalAmount = account.getDepositAmount() + amount;
        } else {
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
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

        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

        long totalAmount;
        if (previousTransactions.isEmpty()) {
            totalAmount = account.getDepositAmount() + amount;
        } else {
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
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

    	
        Account account = accountService.findAccountByAccountNo(accountNo);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNo);
        }

        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

        long totalAmount;
        if (previousTransactions.isEmpty()) {
            totalAmount = account.getDepositAmount();
        } else {
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
            totalAmount = lastTransaction.getTotalAmount();
        }
        
        
        List<Hold> holds = holdRepository.findByAccountId(account.getId());
        long totalHoldAmount = holds.stream()
                .filter(hold -> "Active".equals(hold.getHoldStatus()))
                .mapToLong(Hold::getHoldAmount) 
                .sum();

        long remainingBalance = totalAmount - amount;

        if (remainingBalance < totalHoldAmount) {
            throw new RuntimeException("Withdrawal denied! The remaining balance after withdrawal cannot be less than the total of the hold amounts: " + totalHoldAmount);
        }

        totalAmount = totalAmount - amount;

        Transaction transaction=new Transaction();
        transaction.setTransactionType("WithDraw");
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDate(transactionDate);
        transaction.setTotalAmount(totalAmount); 
        transaction.setAccount(account); 

        
        transactionRepository.save(transaction);

    }

    
    
    
    
    @Transactional
    public void debitTransaction(String accountNo, long amount, String transactionType, LocalDate transactionDate) {

    	
        Account account = accountService.findAccountByAccountNo(accountNo);
        if (account == null) {
            throw new RuntimeException("Account not found with account number: " + accountNo);
        }

        List<Transaction> previousTransactions = transactionRepository.findByAccountId(account.getId());

        long totalAmount;
        if (previousTransactions.isEmpty()) {
            totalAmount = account.getDepositAmount();
        } else {
            Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
            totalAmount = lastTransaction.getTotalAmount();
        }

        List<Hold> holds = holdRepository.findByAccountId(account.getId());
        long totalHoldAmount = holds.stream().mapToLong(Hold::getHoldAmount).sum(); 
        long remainingBalance = totalAmount - amount;

        if (remainingBalance < totalHoldAmount) {
            throw new RuntimeException("Debit denied! The remaining balance after withdrawal cannot be less than the total of the hold amounts: " + totalHoldAmount);
        }
        totalAmount = totalAmount - amount;

        Transaction transaction=new Transaction();
        transaction.setTransactionType("WithDraw");
        transaction.setAmount(amount);
        transaction.setTransactionType(transactionType);
        transaction.setTransactionDate(transactionDate);
        transaction.setTotalAmount(totalAmount); 
        transaction.setAccount(account); 

        
        transactionRepository.save(transaction);

    }


    
    
    
    
}
