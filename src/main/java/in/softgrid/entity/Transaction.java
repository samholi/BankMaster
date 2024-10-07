package in.softgrid.entity;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_type")
    private String transactionType;

    @Column(name = "amount")
    private long amount;
    
    @Column(name = "total_amount")
    private Long totalAmount;

   

    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public long getAmount() {
		return amount;
	}

	public Long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}


	public LocalDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(LocalDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	@Column(name = "transaction_date")
    private LocalDate transactionDate;


    @ManyToOne
    @JoinColumn(name = "account_no", referencedColumnName = "account_no", nullable = false)
    private Account account;

    public Transaction() {}

    public Transaction(String transactionType, long amount, LocalDate transactionDate, Account account) {
        this.transactionType = transactionType;
        this.amount = amount;
        this.transactionDate = transactionDate;
        
        
        this.account = account;
    }

    // Getters and Setters
}

