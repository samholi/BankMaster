package in.softgrid.repositary;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.softgrid.entity.OrganizationOrder;

@Repository
public interface OrgOrderRepository extends JpaRepository<OrganizationOrder, Long> {

	
    List<OrganizationOrder> findByOrgAccountId(Long orgAccountId);

}

