package in.softgrid.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.*;

@Entity
@Table(name="organization")
public class Organization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String organizationname;

   
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

	public String getOrganizationname() {
		return organizationname;
	}

	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
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


	
	
}



