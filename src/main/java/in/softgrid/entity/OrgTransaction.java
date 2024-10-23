package in.softgrid.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "org_transaction")
public class OrgTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_type")
    private String orgTransactionType;

    @Column(name = "amount")
    private long orgTAmount;
    
    @Column(name = "total_amount")
    private Long orgTotalAmount;
    
	@Column(name = "transaction_date")
    private LocalDate orgTransactionDate;

   
	 @ManyToOne
	    @JoinColumn(name = "org_account_no",referencedColumnName = "org_account_no" ,nullable = false)
	    private OrgAccount orgAccount;
    
    public OrgAccount getOrgAccount() {
		return orgAccount;
	}

	public void setOrgAccount(OrgAccount orgAccount) {
		this.orgAccount = orgAccount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	


	public LocalDate getTransactionDate() {
		return orgTransactionDate;
	}

	public void setTransactionDate(LocalDate orgTransactionDate) {
		this.orgTransactionDate = orgTransactionDate;
	}







    public OrgTransaction() {}

    public OrgTransaction(String orgTransactionType, long orgAmount, LocalDate orgTransactionDate, OrgAccount orgaccount) {
        this.orgTransactionType = orgTransactionType;
        this.orgTAmount = orgAmount;
        this.orgTransactionDate = orgTransactionDate;
        
        
    }

	public String getOrgTransactionType() {
		return orgTransactionType;
	}

	public void setOrgTransactionType(String orgTransactionType) {
		this.orgTransactionType = orgTransactionType;
	}

	public long getOrgTAmount() {
		return orgTAmount;
	}

	public void setOrgTAmount(long orgTAmount) {
		this.orgTAmount = orgTAmount;
	}

	public Long getOrgTotalAmount() {
		return orgTotalAmount;
	}

	public void setOrgTotalAmount(Long orgTotalAmount) {
		this.orgTotalAmount = orgTotalAmount;
	}

	public LocalDate getOrgTransactionDate() {
		return orgTransactionDate;
	}

	public void setOrgTransactionDate(LocalDate orgTransactionDate) {
		this.orgTransactionDate = orgTransactionDate;
	}

   
}

