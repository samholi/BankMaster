package in.softgrid.repositary;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.softgrid.entity.Organization;

public interface orgRepository extends JpaRepository<Organization, Long> 
{

	@Query("SELECT o FROM Organization o WHERE " +
		       "(:id IS NULL OR o.id = :id) AND " +
		       "(:organizationname IS NULL OR o.organizationname LIKE :organizationname%) AND " +
		       "(:email IS NULL OR o.email LIKE :email%) AND " +
		       "(:phone IS NULL OR o.phone LIKE :phone%)")
		List<Organization> findByCriteria(@Param("id") Long id,
		                                  @Param("organizationname") String organizationname,
		                                  @Param("email") String email,
		                                  @Param("phone") String phone);

	
   
}
