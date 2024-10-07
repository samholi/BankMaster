package in.softgrid.repositary;




import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.softgrid.entity.Customer;

public interface customerRepository extends JpaRepository<Customer, Long> {

	
	
	@Query("SELECT c FROM Customer c WHERE " +
	        "(:id IS NULL OR c.id = :id) AND " +
	        "(:firstname IS NULL OR c.firstname LIKE :firstname%) AND " +
	        "(:lastname IS NULL OR c.lastname LIKE :lastname%) AND " +
	        "(:email IS NULL OR c.email LIKE :email%) AND " +
	        "(:phone IS NULL OR c.phone LIKE :phone%)")
	List<Customer> findByCriteria(@Param("id") Long id,
	                              @Param("firstname") String firstname,
	                              @Param("lastname") String lastname,
	                              @Param("email") String email,
	                              @Param("phone") String phone);

}
