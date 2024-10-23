package in.softgrid.service;


import in.softgrid.entity.OrganizationOrder;
import in.softgrid.repositary.OrgOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrgOrderService {

    @Autowired
    private OrgOrderRepository organizationOrderRepository;

    public List<OrganizationOrder> getAllOrders() {
        return organizationOrderRepository.findAll();
    }

    public Optional<OrganizationOrder> getOrderById(Long id) {
        return organizationOrderRepository.findById(id);
    }

    public OrganizationOrder saveOrder(OrganizationOrder organizationOrder) {
        return organizationOrderRepository.save(organizationOrder);
    }
    
    
    
    
   
    
    
    public List<OrganizationOrder> findByAccountId(Long orgAccountId) {
        return organizationOrderRepository.findByOrgAccountId(orgAccountId);
    }

}

   

