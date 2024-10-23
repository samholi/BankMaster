package in.softgrid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import in.softgrid.entity.Hold;

import java.time.LocalDate;
import java.util.List;

@Service
public class HoldStatusUpdater {

    @Autowired
    private HoldService holdService;

    @Scheduled(cron = "0 0 * * * ?")  
    public void updateExpiredHolds() {
        LocalDate today = LocalDate.now();
        
      
        List<Hold> activeHolds = holdService.findAllActiveHolds();
        
        for (Hold hold : activeHolds) {
            if (hold.getHoldExpireeDate().isBefore(today)) {
                hold.setHoldStatus("Passed"); 
                holdService.saveHold(hold);
            }
        }
    }
}

