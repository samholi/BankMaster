package in.softgrid.repositary;

import in.softgrid.entity.Hold;
import in.softgrid.entity.OrgAccount;
import in.softgrid.entity.OrgHold;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrgHoldRepository extends JpaRepository<OrgHold, Long> {
    List<OrgHold> findByOrgAccount(OrgAccount orgAccount);

List<OrgHold> findByOrgAccountId(Long orgAccountId);

Optional<OrgHold> findById(Long id);


}