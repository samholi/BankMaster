package in.softgrid.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.softgrid.entity.Organization;
import in.softgrid.service.OrgService;


@Controller
public class OrgController {

    @Autowired
    private OrgService orgservice;

    @GetMapping("/add_organization")
    public String showForm(Model model) {
        model.addAttribute("user", new Organization());
        return "add_org.html";
    }
    
    
   
    
    @PostMapping("/add_organization")
    public String registerOrganization(@ModelAttribute Organization organization, Model model) 
    {
        orgservice.saveOrg(organization);
        // Add the organization object to the model
        model.addAttribute("organization", organization);
        return "Success_AddOrg.html";
    }
    
    @GetMapping("/org_search")
    public String openPage()
    {
    	return "org_search.html";
    }
    
    
    @GetMapping("/search_organization")
    public String searchOrganizations(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String organizationname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            Model model) 
    {
        // Check if any search criteria is provided
        boolean hasCriteria = (id != null || organizationname != null || email != null || phone != null);
        
        if (!hasCriteria) {
            // No criteria provided, redirect to a page or show message accordingly
            return "org_search.html"; // Or you can return an empty list or a different view
        }

        List<Organization> organizations = orgservice.searchOrgs(id, organizationname, email, phone);
        if (organizations.isEmpty()) 
        {
            return "no_records_found.html"; // Redirect to a page indicating no records found
        }
        model.addAttribute("organizations", organizations);
        return "search_result_org.html";
    }
}