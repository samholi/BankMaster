package in.softgrid.repositary;

import in.softgrid.entity.Transaction;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;


@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountId(Long accountId); // Fetch transactions for a specific account
    
    
    List<Transaction> findByAccountAccountNo(String accountNo);
    

    @Query(value = "SELECT * FROM transaction t WHERE t.account_no = :accountId ORDER BY t.transaction_date DESC LIMIT 1", nativeQuery = true)
    Transaction findLastTransactionByAccountId(@Param("accountId") Long accountId);
    
    
    @Query(value = "SELECT * FROM transaction t WHERE t.account_no = :accountNo ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
    Transaction findLastOrderTransactionByAccountId(@Param("accountNo") String accountNo);

    List<Transaction> findTopByAccount_AccountNoOrderByTransactionDateDesc(String accountNo);


    List<Transaction> findByAccount_AccountNo(String accountNo);


    
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNo = :accountNo ORDER BY t.id DESC")
    List<Transaction> findLatestTransactionForAccount(@Param("accountNo") String accountNo, Pageable pageable);
}

