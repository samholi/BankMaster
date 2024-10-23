package in.softgrid.service;

import in.softgrid.entity.Organization;
import in.softgrid.entity.OrganizationOrder;
import in.softgrid.repositary.OrganizationRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService {
    @Autowired
    private OrganizationRepository organizationRepository;

    public void saveOrganization(Organization organization) {
        organizationRepository.save(organization);
    }

    
    
    public List<Organization> searchOrganizations(Long id, String orgName, String email, String phone) {
        return organizationRepository.findByCriteria(id, orgName, email, phone);
    }
    
    
    public Organization findOrganizationById(Long id) {
        Optional<Organization> optionalOrganization = organizationRepository.findById(id);

        // Return the organization if found, otherwise return null or throw an exception
        return optionalOrganization.orElse(null);  // Or handle the absence of the record as needed
    }
}


