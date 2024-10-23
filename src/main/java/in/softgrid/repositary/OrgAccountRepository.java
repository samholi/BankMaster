package in.softgrid.repositary;

import in.softgrid.entity.OrgAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgAccountRepository extends JpaRepository<OrgAccount, Long> {
	OrgAccount findByOrgAccountNo(String orgAccountNo);
	 
}
