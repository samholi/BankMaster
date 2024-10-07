package in.softgrid.entity;


import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "account_holder_name")
    private String accountHolderName;

    @Column(name = "account_no", unique = true)
    private String accountNo;
    
    @Column(name = "Hold")
    private long hold;
    
    public long getHold() {
		return hold;
	}

	public void setHold(long hold) {
		this.hold = hold;
	}

	@Column()
    private String branch;


    public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	@Column(name = "account_type")
    private String accountType;
    
    @Column(name = "deposit_amount")
    private long depositAmount;

    public long getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(long depositAmount) {
		this.depositAmount = depositAmount;
	}

	@ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

	 // One-to-Many Relationship with Transaction
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Transaction> transactions;


    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}

