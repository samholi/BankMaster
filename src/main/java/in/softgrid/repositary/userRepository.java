package in.softgrid.repositary;




import org.springframework.data.jpa.repository.JpaRepository;

import in.softgrid.entity.User;

public interface userRepository extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
   
}
