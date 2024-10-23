package in.softgrid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.softgrid.entity.Account;
import in.softgrid.repositary.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }
    
    public Account findAccountByAccountNo(String accountNo) {
        return accountRepository.findByAccountNo(accountNo);
    }
    
}

