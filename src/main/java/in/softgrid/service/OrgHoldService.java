package in.softgrid.service;

import in.softgrid.entity.Hold;
import in.softgrid.entity.OrgAccount;
import in.softgrid.entity.OrgHold;
import in.softgrid.repositary.OrgHoldRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrgHoldService {
    @Autowired
    private OrgHoldRepository orgHoldRepository;

    public void saveOrgHold(OrgHold orgHold) {
        orgHoldRepository.save(orgHold);
    }

    public List<OrgHold> findHoldsByOrgAccount(OrgAccount orgAccount) {
        return orgHoldRepository.findByOrgAccount(orgAccount);  
    }
    
    public List<OrgHold> getHoldsByOrgAccountId(Long orgAccountId) {
        return orgHoldRepository.findByOrgAccountId(orgAccountId);
    }
    
    public OrgHold findHoldById(Long id) {
        return orgHoldRepository.findById(id).orElse(null);
    }
}
