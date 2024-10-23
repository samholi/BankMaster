package in.softgrid.entity;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "organization_orders")
public class OrganizationOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_type")
    private String orderOType;

    @Column(name = "bank_type")
    private String bankOType;
    
    @Column(name = "Order_Acc_No")
    private String orderOAccNo;

    @Column(name = "bank_name")
    private String bankOName;
    
    @Column(name = "bank_Ifsc")
    private String bankOIfsc;
    
    @Column(name = "bank_branch")
    private String bankOBranch;

    @Column(name = "amount")
    private long oOAmount;
    
    @Column(name = "total_amount")
    private Long oOTAmount;

    @Column(name = "order_status")  
    private String orderOStatus;

    public String getBankOIfsc() {
		return bankOIfsc;
	}

	public void setBankOIfsc(String bankOIfsc) {
		this.bankOIfsc = bankOIfsc;
	}

	public String getBankOBranch() {
		return bankOBranch;
	}

	public void setBankOBranch(String bankOBranch) {
		this.bankOBranch = bankOBranch;
	}

	@Column(name = "Date")
    private LocalDate oODate;

    // Many-to-One relationship with OrganizationAccount
    @ManyToOne
    @JoinColumn(name = "organization_account_id", nullable = false)
    private OrgAccount orgAccount;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getOrderOType() {
		return orderOType;
	}

	public void setOrderOType(String orderOType) {
		this.orderOType = orderOType;
	}

	public String getBankOType() {
		return bankOType;
	}

	public void setBankOType(String bankOType) {
		this.bankOType = bankOType;
	}

	public String getOrderOAccNo() {
		return orderOAccNo;
	}

	public void setOrderOAccNo(String orderOAccNo) {
		this.orderOAccNo = orderOAccNo;
	}

	public String getBankOName() {
		return bankOName;
	}

	public void setBankOName(String bankOName) {
		this.bankOName = bankOName;
	}

	public long getoOAmount() {
		return oOAmount;
	}

	public void setoOAmount(long oOAmount) {
		this.oOAmount = oOAmount;
	}

	public Long getoOTAmount() {
		return oOTAmount;
	}

	public void setoOTAmount(Long oOTAmount) {
		this.oOTAmount = oOTAmount;
	}

	public String getOrderOStatus() {
		return orderOStatus;
	}

	public void setOrderOStatus(String orderOStatus) {
		this.orderOStatus = orderOStatus;
	}

	public LocalDate getoODate() {
		return oODate;
	}

	public void setoODate(LocalDate oODate) {
		this.oODate = oODate;
	}

	public OrgAccount getOrgAccount() {
		return orgAccount;
	}

	public void setOrgAccount(OrgAccount orgAccount) {
		this.orgAccount = orgAccount;
	}
    

}