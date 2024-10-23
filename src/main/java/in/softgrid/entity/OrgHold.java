package in.softgrid.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "org_hold")
public class OrgHold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hold_amount")
    private long orgHoldAmount;

    @Column(name = "hold_type")
    private String orgHoldType;

    @Column(name = "hold_status")
    private String orgHoldStatus;

    @Column(name = "hold_created_date")
    private LocalDate orgHoldCreatedDate;

    @Column(name = "hold_expire_date")
    private LocalDate orgHoldExpireeDate;

    @ManyToOne
    @JoinColumn(name = "org_account_id", nullable = false) // Foreign key column
    private OrgAccount orgAccount;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getOrgHoldAmount() {
        return orgHoldAmount;
    }

    public void setOrgHoldAmount(long orgHoldAmount) {
        this.orgHoldAmount = orgHoldAmount;
    }

    public String getOrgHoldType() {
        return orgHoldType;
    }

    public void setOrgHoldType(String orgHoldType) {
        this.orgHoldType = orgHoldType;
    }

    public String getOrgHoldStatus() {
        return orgHoldStatus;
    }

    public void setOrgHoldStatus(String orgHoldStatus) {
        this.orgHoldStatus = orgHoldStatus;
    }

    public LocalDate getOrgHoldCreatedDate() {
        return orgHoldCreatedDate;
    }

    public void setOrgHoldCreatedDate(LocalDate orgHoldCreatedDate) {
        this.orgHoldCreatedDate = orgHoldCreatedDate;
    }

    public LocalDate getOrgHoldExpireeDate() {
        return orgHoldExpireeDate;
    }

    public void setOrgHoldExpireeDate(LocalDate orgHoldExpireeDate) {
        this.orgHoldExpireeDate = orgHoldExpireeDate;
    }

    public OrgAccount getOrgAccount() {
        return orgAccount;
    }

    public void setOrgAccount(OrgAccount orgAccount) {
        this.orgAccount = orgAccount;
    }
}
