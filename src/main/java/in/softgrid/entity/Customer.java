package in.softgrid.entity;

import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name="customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String firstname;

    @Column
    private String lastname;

    @Column(unique = true)
    private String email;
    
	@Column
    private String phone;
  
	@Column
    private LocalDate dob;
	
	@Column
	private String address;
	//--------------------------Account-------------------------//
	
	 
      
    //--------------------------Education-------------------------//
      @Column(name = "ssc_school")
      private String sscSchool;

      @Column(name = "hsc_school")
      private String hscSchool;

      @Column
      private String university;
      
      //--------------------------Documents-------------------------//
      @Column(name = "document_id", unique = true)
      private String documentId;

      @Column(name = "document_type1")
      private String documentType1;

      @Lob
      @Column(columnDefinition = "LONGBLOB")
      private byte[] documentFile1;

     

	@Column(name = "document_type2")
      private String documentType2;

      @Lob
      @Column(columnDefinition = "LONGBLOB")
      private byte[] documentFile2;

      @Column(name = "upload_date")
      private LocalDate uploadDate;
      
    

	

	//--------------------------Profile image-------------------------//
      @Lob
      @Column(name = "profile_image")
      private byte[] profileImage;
      
      
      
      
      
      @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
      private List<Account> accounts;
      
      public List<Account> getAccounts() {
          return accounts;
      }

      public void setAccounts(List<Account> accounts) {
          this.accounts = accounts;
      }
//-------------------------------------------------------------------------------------------------------------------------------//
//-------------------------------------------------------------------------------------------------------------------------------//
      

	@Override
	public String toString() {
		return firstname + " " +  lastname;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	

	public String getSscSchool() {
		return sscSchool;
	}

	public void setSscSchool(String sscSchool) {
		this.sscSchool = sscSchool;
	}

	public String getHscSchool() {
		return hscSchool;
	}

	public void setHscSchool(String hscSchool) {
		this.hscSchool = hscSchool;
	}

	public String getUniversity() {
		return university;
	}

	public void setUniversity(String university) {
		this.university = university;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentType1() {
		return documentType1;
	}

	public void setDocumentType1(String documentType1) {
		this.documentType1 = documentType1;
	}

	public byte[] getDocumentFile1() {
		return documentFile1;
	}

	public void setDocumentFile1(byte[] documentFile1) {
		this.documentFile1 = documentFile1;
	}

	public String getDocumentType2() {
		return documentType2;
	}

	public void setDocumentType2(String documentType2) {
		this.documentType2 = documentType2;
	}

	public byte[] getDocumentFile2() {
		return documentFile2;
	}

	public void setDocumentFile2(byte[] documentFile2) {
		this.documentFile2 = documentFile2;
	}

	public LocalDate getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(LocalDate uploadDate) {
		this.uploadDate = uploadDate;
	}

	public byte[] getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}
}


