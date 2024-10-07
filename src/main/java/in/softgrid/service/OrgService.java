package in.softgrid.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.softgrid.entity.Customer;
import in.softgrid.entity.Organization;
import in.softgrid.repositary.customerRepository;
import in.softgrid.repositary.orgRepository;

@Service
public class OrgService 
{

    @Autowired
    private orgRepository orgRepo;

    public void saveOrg(Organization org) {
        orgRepo.save(org);
    } 
        public List<Organization> searchOrgs(Long id, String organizationname,String email, String phone) {
            return orgRepo.findByCriteria(id,organizationname, email, phone);
        }
    }




