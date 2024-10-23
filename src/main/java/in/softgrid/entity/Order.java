package in.softgrid.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_type")
    private String orderType;

    @Column(name = "bank_type")
    private String bankType;
    
    @Column(name = "Order_Acc_No")
    private String orderAccNo;

	@Column(name = "bank_name")
    private String bankName;
	
	@Column(name = "bank_ifsc")
    private String bankIfsc;
	
	@Column(name = "bank_branch")
    private String bankBranch;

    public String getBankIfsc() {
		return bankIfsc;
	}

	public void setBankIfsc(String bankIfsc) {
		this.bankIfsc = bankIfsc;
	}

	public String getBankBranch() {
		return bankBranch;
	}

	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	public void setoTAmount(Long oTAmount) {
		this.oTAmount = oTAmount;
	}

	@Column(name = "amount")
    private long oAmount;
    
    @Column(name = "total_amount")
    private Long oTAmount;


    public Long getoTAmount() {
		return oTAmount;
	}

	public void setoTAmount(long oTAmount) {
		this.oTAmount = oTAmount;
	}

	@Column(name = "order_status")  
    private String orderStatus;
    
    public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Column(name = "Date")
    private LocalDate oDate;

    public LocalDate getoDate() {
		return oDate;
	}

	public void setoDate(LocalDate oDate) {
		this.oDate = oDate;
	}

	// Many-to-One relationship with Account
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getBankType() {
        return bankType;
    }

    public String getOrderAccNo() {
		return orderAccNo;
	}

	public void setOrderAccNo(String orderAccNo) {
		this.orderAccNo = orderAccNo;
	}
    
    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

   
    
  

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    // Constructors
    public Order() {}

    public Order(String orderType, String bankType, String bankName, String orderBranch, long oAmount, String orderStatus, Long totalAmount, Account account) {
        this.orderType = orderType;
        this.bankType = bankType;
        this.bankName = bankName;
        this.oAmount = oAmount;
        this.account = account;
    }

	public long getoAmount() {
		return oAmount;
	}

	public void setoAmount(long oAmount) {
		this.oAmount = oAmount;
	}
}
