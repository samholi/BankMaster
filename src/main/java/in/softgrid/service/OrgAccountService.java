package in.softgrid.service;

import in.softgrid.entity.OrgAccount;
import in.softgrid.repositary.OrgAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgAccountService {
    @Autowired
    private OrgAccountRepository orgAccountRepository;

    public void saveOrgAccount(OrgAccount orgAccount) {
        orgAccountRepository.save(orgAccount);
    }
    
    public OrgAccount findAccountByAccountNo(String orgAccountNo) {
        return orgAccountRepository.findByOrgAccountNo(orgAccountNo); 
    }
    
    

}
