package in.softgrid.repositary;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import in.softgrid.entity.OrgAccount;
import in.softgrid.entity.OrgTransaction;
import in.softgrid.entity.Transaction;

@Repository
public interface OrgTransactionRepository extends JpaRepository<OrgTransaction, Long> {

    List<OrgTransaction> findByOrgAccountId(Long orgAccountId);

	
    @Query("SELECT t FROM OrgTransaction t WHERE t.orgAccount.id = :accountId ORDER BY t.orgTransactionDate DESC")
    OrgTransaction findLastTransactionByOrgAccountId(@Param("accountId") Long accountId);

    
    @Query(value = "SELECT * FROM org_transaction t WHERE t.org_account_no = :orgAccountNo ORDER BY t.id DESC LIMIT 1", nativeQuery = true)
    OrgTransaction findLastOrderTransactionByOrgAccountNo(@Param("orgAccountNo") String orgAccountNo);
    
    @Query("SELECT t FROM OrgTransaction t WHERE t.orgAccount.orgAccountNo = :orgAccountNo ORDER BY t.orgTransactionDate DESC")
    List<OrgTransaction> findTransactionsByAccountNo(@Param("orgAccountNo") String orgAccountNo, Pageable pageable);

    default OrgTransaction findLastTransactionByAccountNo(String orgAccountNo) {
        Pageable pageable = PageRequest.of(0, 1);  // Fetch the latest transaction only
        List<OrgTransaction> transactions = findTransactionsByAccountNo(orgAccountNo, pageable);
        return transactions.isEmpty() ? null : transactions.get(0);  // Return the first result or null if empty
    }
    
    
    OrgTransaction findTopByOrgAccountOrgAccountNoOrderByIdDesc(String accountNo);

    
    @Query("SELECT t FROM OrgTransaction t WHERE t.orgAccount.orgAccountNo = :orgAccountNo ORDER BY t.id DESC")
    List<OrgTransaction> findLatestTransactionForAccount(@Param("orgAccountNo") String orgAccountNo, Pageable pageable);
}



