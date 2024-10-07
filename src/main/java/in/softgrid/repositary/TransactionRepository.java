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
    
    
    
    @Query("SELECT t FROM Transaction t WHERE t.account.accountNo = :accountNo ORDER BY t.id DESC")
    List<Transaction> findLatestTransactionForAccount(@Param("accountNo") String accountNo, Pageable pageable);
}

