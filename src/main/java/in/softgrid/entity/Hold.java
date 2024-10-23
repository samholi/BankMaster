package in.softgrid.entity;



import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hold")
public class Hold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hold_amount")
    private long holdAmount;

    @Column(name = "hold_type")
    private String holdType;

    @Column(name = "hold_status")
    private String holdStatus;

    @Column(name = "hold_created_date")
    private LocalDate holdCreatedDate;

    @Column(name = "hold_expire_date")
    private LocalDate holdExpireeDate;

    
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

   

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getHoldAmount() {
        return holdAmount;
    }

    public void setHoldAmount(long holdAmount) {
        this.holdAmount = holdAmount;
    }

    public String getHoldType() {
        return holdType;
    }

    public void setHoldType(String holdType) {
        this.holdType = holdType;
    }

    public String getHoldStatus() {
        return holdStatus;
    }

    public void setHoldStatus(String holdStatus) {
        this.holdStatus = holdStatus;
    }

    public LocalDate getHoldCreatedDate() {
        return holdCreatedDate;
    }

    public void setHoldCreatedDate(LocalDate holdCreatedDate) {
        this.holdCreatedDate = holdCreatedDate;
    }

    public LocalDate getHoldExpireeDate() {
        return holdExpireeDate;
    }

    public void setHoldExpireeDate(LocalDate holdExpireDate) {
        this.holdExpireeDate = holdExpireDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

