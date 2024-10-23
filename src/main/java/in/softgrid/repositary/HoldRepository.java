package in.softgrid.repositary;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import in.softgrid.entity.Account;
import in.softgrid.entity.Hold;

@Repository
public interface HoldRepository extends JpaRepository<Hold, Long> {
	List<Hold> findByAccount(Account account);
	 List<Hold> findByAccountId(Long accountId);
	 Optional<Hold> findById(Long id);
	 List<Hold> findByHoldStatus(String status);
}
