package in.softgrid.service;

import in.softgrid.entity.Account;
import in.softgrid.entity.Hold;
import in.softgrid.repositary.HoldRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HoldService {

    @Autowired
    private HoldRepository holdRepository;

    public Hold saveHold(Hold hold) {
        return holdRepository.save(hold);
    }

    public List<Hold> findHoldsByAccount(Account account) {
        return holdRepository.findByAccount(account);
    }
    
    
    public Hold getHoldById(Long id) {
        return holdRepository.findById(id).orElse(null);
    }

    public List<Hold> getHoldsByAccountId(Long accountId) {
        return holdRepository.findAll().stream()
                             .filter(hold -> hold.getAccount().getId().equals(accountId))
                             .toList();
    }
    
    
    public List<Hold> findAllActiveHolds() {
        return holdRepository.findByHoldStatus("Active");
    }
    
   

    public void deleteHoldById(Long id) {
        holdRepository.deleteById(id);
    }

    public Hold updateHold(Hold hold) {
        return holdRepository.save(hold);
    }
    public Hold findHoldById(Long id) {
        Optional<Hold> hold = holdRepository.findById(id);
        return hold.orElse(null);  
    }
}
