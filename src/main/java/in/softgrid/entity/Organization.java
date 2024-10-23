package in.softgrid.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "organizationname", nullable = false)
    private String orgName;

    @Column(unique = true, name = "email", nullable = false)
    private String orgEmail;

    @Column(name = "phone", nullable = false)
    private String orgPhone;

    @Column(name = "address")
    private String orgAddress;
    
    
  //--------------------------Documents-------------------------//
    @Column(name = "document_id", unique = true)
    private String orgdocumentId;

    public String getOrgdocumentId() {
		return orgdocumentId;
	}

	public void setOrgdocumentId(String orgdocumentId) {
		this.orgdocumentId = orgdocumentId;
	}

	public String getOrgdocumentType1() {
		return orgdocumentType1;
	}

	public void setOrgdocumentType1(String orgdocumentType1) {
		this.orgdocumentType1 = orgdocumentType1;
	}

	public byte[] getOrgdocumentFile1() {
		return orgdocumentFile1;
	}

	public void setOrgdocumentFile1(byte[] orgdocumentFile1) {
		this.orgdocumentFile1 = orgdocumentFile1;
	}

	public String getOrgdocumentType2() {
		return orgdocumentType2;
	}

	public void setOrgdocumentType2(String orgdocumentType2) {
		this.orgdocumentType2 = orgdocumentType2;
	}

	public byte[] getOrgdocumentFile2() {
		return orgdocumentFile2;
	}

	public void setOrgdocumentFile2(byte[] orgdocumentFile2) {
		this.orgdocumentFile2 = orgdocumentFile2;
	}

	public LocalDate getOrguploadDate() {
		return orguploadDate;
	}

	public void setOrguploadDate(LocalDate orguploadDate) {
		this.orguploadDate = orguploadDate;
	}

	@Column(name = "document_type1")
    private String orgdocumentType1;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] orgdocumentFile1;

   

	@Column(name = "document_type2")
    private String orgdocumentType2;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] orgdocumentFile2;

    @Column(name = "upload_date")
    private LocalDate orguploadDate;
    

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrgAccount> orgAccounts;
    
    
    public List<OrgAccount> getOrggAccounts() 
    {
        return orgAccounts;
    }

    public void setAccounts(List<OrgAccount> orgAccounts) {
        this.orgAccounts = orgAccounts;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public String getOrgPhone() {
        return orgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public List<OrgAccount> getOrgAccounts() {
        return orgAccounts;
    }

    public void setOrgAccounts(List<OrgAccount> orgAccounts) {
        this.orgAccounts = orgAccounts;
    }
}
