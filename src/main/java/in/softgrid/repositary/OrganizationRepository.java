package in.softgrid.repositary;

import in.softgrid.entity.Organization;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

	 @Query("SELECT o FROM Organization o WHERE " +
	            "(:id IS NULL OR o.id = :id) AND " +
	            "(:orgName IS NULL OR o.orgName LIKE CONCAT(:orgName, '%')) AND " +
	            "(:email IS NULL OR o.orgEmail LIKE CONCAT(:email, '%')) AND " +
	            "(:phone IS NULL OR o.orgPhone LIKE CONCAT(:phone, '%'))")
	    List<Organization> findByCriteria(@Param("id") Long id,
	                                       @Param("orgName") String orgName,
	                                       @Param("email") String email,
	                                       @Param("phone") String phone);

}
