package in.softgrid.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import in.softgrid.entity.Account;
import in.softgrid.entity.Hold;
import in.softgrid.entity.OrgAccount;
import in.softgrid.entity.OrgHold;
import in.softgrid.entity.OrgTransaction;
import in.softgrid.entity.Transaction;
import in.softgrid.repositary.OrgAccountRepository;
import in.softgrid.repositary.OrgHoldRepository;
import in.softgrid.repositary.OrgTransactionRepository;
import jakarta.persistence.EntityNotFoundException;


@Service
public class OrgTransactionService 
{

	    @Autowired
	    private OrgAccountRepository orgAccountRepository;

	    @Autowired
	    private OrgTransactionRepository orgTransactionRepository; 
	
	    @Autowired
	    private OrgAccountService orgaccountservice;
	    
	    @Autowired
	    private OrgHoldRepository orgholdRepository;
	    
	    
	    
	    public Double getTotalAmountForOrgAccounts(List<OrgAccount> orgAccounts) {
	        double totalSum = 0.0;

	        for (OrgAccount orgAccount : orgAccounts) {
	            // Get the last transaction for this account
	            OrgTransaction lastTransaction = orgTransactionRepository.findLastTransactionByAccountNo(orgAccount.getOrgAccountNo());

	            // If lastTransaction is null or totalAmount is null, use depositAmount from OrgAccount
	            if (lastTransaction == null || lastTransaction.getOrgTotalAmount() == null) {
	                totalSum += orgAccount.getOrgDepositAmount();
	            } else {
	                totalSum += lastTransaction.getOrgTotalAmount();
	            }
	        }

	        return totalSum;
	    }
	    
	    
	    
	
	    public OrgTransaction findLatestTransactionForAccount(String orgAccountNo) {
	        Pageable pageable = PageRequest.of(0, 1); 
	        List<OrgTransaction> transactions = orgTransactionRepository.findLatestTransactionForAccount(orgAccountNo, pageable);
	        return transactions.isEmpty() ? null : transactions.get(0);
	    }
	    
	    
	    public List<OrgTransaction> findTransactionsByAccountId(Long accountId) {
	        return orgTransactionRepository.findByOrgAccountId(accountId);
	    }
	    
	    
	    public void save(OrgTransaction orgtransaction) {
	        orgTransactionRepository.save(orgtransaction); 
	    }
	    
	    
	    public OrgTransaction findLastTransactionByAccountNo(String accountNo) {
	        return orgTransactionRepository.findTopByOrgAccountOrgAccountNoOrderByIdDesc(accountNo);
	    }

	    
	    
	    @Transactional
	    public void creditOrgTransaction(String orgAccountNo, long amount, String transactionType, LocalDate transactionDate) {
	        OrgAccount orgAccount = orgAccountRepository.findByOrgAccountNo(orgAccountNo);
	        if (orgAccount == null) {
	            throw new RuntimeException("Account not found with account number: " + orgAccountNo);
	        }
	        
	        List<OrgTransaction> previousTransactions = orgTransactionRepository.findByOrgAccountId(orgAccount.getId());


	        long totalAmount;
	        if (previousTransactions.isEmpty()) {
	            totalAmount = orgAccount.getOrgDepositAmount() + amount;
	        } else {
	            OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
	            totalAmount = lastTransaction.getOrgTotalAmount() + amount;
	        }

	        // Create and save a new OrgTransaction record
	        OrgTransaction orgTransaction = new OrgTransaction();
	        orgTransaction.setOrgTransactionType("Credit");
	        orgTransaction.setOrgTransactionType(transactionType);
	        orgTransaction.setOrgTAmount(amount);
	        orgTransaction.setOrgTransactionDate(transactionDate);
	        orgTransaction.setOrgTotalAmount(totalAmount); 

	        
	        orgTransaction.setOrgAccount(orgAccount);

	        orgTransactionRepository.save(orgTransaction);
	    }
	    
	    
	    
	    @Transactional
	    public void depositeOrgTransaction(String orgAccountNo, long amount, String transactionType, LocalDate transactionDate) {
	        OrgAccount orgAccount = orgAccountRepository.findByOrgAccountNo(orgAccountNo);
	        if (orgAccount == null) {
	            throw new RuntimeException("Account not found with account number: " + orgAccountNo);
	        }
	        
	        List<OrgTransaction> previousTransactions = orgTransactionRepository.findByOrgAccountId(orgAccount.getId());


	        long totalAmount;
	        if (previousTransactions.isEmpty()) {
	            totalAmount = orgAccount.getOrgDepositAmount() + amount;
	        } else {
	            OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
	            totalAmount = lastTransaction.getOrgTotalAmount() + amount;
	        }

	        // Create and save a new OrgTransaction record
	        OrgTransaction orgTransaction = new OrgTransaction();
	        orgTransaction.setOrgTransactionType("Deposite");
	        orgTransaction.setOrgTransactionType(transactionType);
	        orgTransaction.setOrgTAmount(amount);
	        orgTransaction.setOrgTransactionDate(transactionDate);
	        orgTransaction.setOrgTotalAmount(totalAmount); 

	        
	        orgTransaction.setOrgAccount(orgAccount);

	        orgTransactionRepository.save(orgTransaction);
	    }
	    
	    
	    @Transactional
	    public void withdrawOrgTransaction(String orgAccountNo, long orgTAmount, String orgTransactionType, LocalDate orgTransactionDate) {

	    	
	        OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	        if (account == null) {
	            throw new RuntimeException("Account not found with account number: " + orgAccountNo);
	        }

	        List<OrgTransaction> previousTransactions = orgTransactionRepository.findByOrgAccountId(account.getId());

	        long totalAmount;
	        if (previousTransactions.isEmpty()) {
	            totalAmount = account.getOrgDepositAmount();
	        } else {
	            OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
	            totalAmount = lastTransaction.getOrgTotalAmount();
	        }
	        
	        
	        List<OrgHold> holds = orgholdRepository.findByOrgAccountId(account.getId());
	        long totalHoldAmount = holds.stream()
	                .filter(hold -> "Active".equals(hold.getOrgHoldStatus()))
	                .mapToLong(OrgHold::getOrgHoldAmount) 
	                .sum();

	        long remainingBalance = totalAmount - orgTAmount;

	        if (remainingBalance < totalHoldAmount) {
	            throw new RuntimeException("Withdrawal denied! The remaining balance after withdrawal cannot be less than the total of the hold amounts: " + totalHoldAmount);
	        }

	        totalAmount = totalAmount - orgTAmount;

	        OrgTransaction transaction=new OrgTransaction();
	        transaction.setOrgTransactionType("WithDraw");
	        transaction.setOrgTAmount(orgTAmount);
	        transaction.setOrgTransactionType(orgTransactionType);
	        transaction.setTransactionDate(orgTransactionDate);
	        transaction.setOrgTotalAmount(totalAmount); 
	        transaction.setOrgAccount(account); 

	        
	        orgTransactionRepository.save(transaction);

	    }
	    
	    
	    
	    
	    @Transactional
	    public void debitOrgTransaction(String orgAccountNo, long orgTAmount, String orgTransactionType, LocalDate orgTransactionDate) {

	    	
	        OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	        if (account == null) {
	            throw new RuntimeException("Account not found with account number: " + orgAccountNo);
	        }

	        List<OrgTransaction> previousTransactions = orgTransactionRepository.findByOrgAccountId(account.getId());

	        long totalAmount;
	        if (previousTransactions.isEmpty()) {
	            totalAmount = account.getOrgDepositAmount();
	        } else {
	            OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
	            totalAmount = lastTransaction.getOrgTotalAmount();
	        }
	        
	        
	        List<OrgHold> holds = orgholdRepository.findByOrgAccountId(account.getId());
	        long totalHoldAmount = holds.stream()
	                .filter(hold -> "Active".equals(hold.getOrgHoldStatus()))
	                .mapToLong(OrgHold::getOrgHoldAmount) 
	                .sum();

	        long remainingBalance = totalAmount - orgTAmount;

	        if (remainingBalance < totalHoldAmount) {
	            throw new RuntimeException("Debit denied! The remaining balance after withdrawal cannot be less than the total of the hold amounts: " + totalHoldAmount);
	        }

	        totalAmount = totalAmount - orgTAmount;

	        OrgTransaction transaction=new OrgTransaction();
	        transaction.setOrgTransactionType("Debit");
	        transaction.setOrgTAmount(orgTAmount);
	        transaction.setOrgTransactionType(orgTransactionType);
	        transaction.setTransactionDate(orgTransactionDate);
	        transaction.setOrgTotalAmount(totalAmount); 
	        transaction.setOrgAccount(account); 

	        
	        orgTransactionRepository.save(transaction);

	    }
	}
