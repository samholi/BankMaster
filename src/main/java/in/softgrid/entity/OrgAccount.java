package in.softgrid.entity;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "org_account")
public class OrgAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "org_account_no", unique = true)
    private String orgAccountNo;

    @Column(name = "acc_type")
    private String orgAccountType;

    @Column(name = "branch")
    private String orgBranch;

    @Column(name = "deposit_amount")
    private Long orgDepositAmount;

    @ManyToOne
    @JoinColumn(name = "organization_id", nullable = false) // Foreign key column
    private Organization organization;

    
    @OneToMany(mappedBy = "orgAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrgHold> orgHolds;
    
    @OneToMany(mappedBy = "orgAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrgTransaction> orgTransactions;
    
    public List<OrgTransaction> getOrgTransactions() {
		return orgTransactions;
	}

	public void setOrgTransactions(List<OrgTransaction> orgTransactions) {
		this.orgTransactions = orgTransactions;
	}

	public List<OrgHold> getOrgHolds() {
		return orgHolds;
	}

	public void setOrgHolds(List<OrgHold> orgHolds) {
		this.orgHolds = orgHolds;
	}

	// Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgAccountNo() {
        return orgAccountNo;
    }

    public void setOrgAccountNo(String orgAccountNo) {
        this.orgAccountNo = orgAccountNo;
    }

    public String getOrgAccountType() {
        return orgAccountType;
    }

    public void setOrgAccountType(String orgAccountType) {
        this.orgAccountType = orgAccountType;
    }

    public String getOrgBranch() {
        return orgBranch;
    }

    public void setOrgBranch(String orgBranch) {
        this.orgBranch = orgBranch;
    }

    public Long getOrgDepositAmount() {
        return orgDepositAmount;
    }

    public void setOrgDepositAmount(Long orgDepositAmount) {
        this.orgDepositAmount = orgDepositAmount;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
