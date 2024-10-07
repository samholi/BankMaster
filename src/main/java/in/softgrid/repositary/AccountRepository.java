package in.softgrid.repositary;

import org.springframework.data.jpa.repository.JpaRepository;

import in.softgrid.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	 Account findByAccountNo(String accountNo);
}

