package in.softgrid.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import in.softgrid.entity.Account;
import in.softgrid.entity.Customer;
import in.softgrid.entity.Hold;
import in.softgrid.entity.Order;
import in.softgrid.entity.OrgAccount;
import in.softgrid.entity.OrgHold;
import in.softgrid.entity.OrgTransaction;
import in.softgrid.entity.Organization;
import in.softgrid.entity.OrganizationOrder;
import in.softgrid.entity.Transaction;
import in.softgrid.entity.User;
import in.softgrid.repositary.AccountRepository;
import in.softgrid.repositary.HoldRepository;
import in.softgrid.repositary.OrgAccountRepository;
import in.softgrid.repositary.OrgHoldRepository;
import in.softgrid.repositary.OrgTransactionRepository;
import in.softgrid.repositary.TransactionRepository;
import in.softgrid.service.AccountService;
import in.softgrid.service.CustomerService;
import in.softgrid.service.HoldService;
import in.softgrid.service.OrderService;
import in.softgrid.service.OrgAccountService;
import in.softgrid.service.OrgHoldService;
import in.softgrid.service.OrgOrderService;
import in.softgrid.service.OrgTransactionService;
import in.softgrid.service.OrganizationService;
import in.softgrid.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class OrganizationController {

	@Autowired
	OrganizationService orgservice;
	
	@Autowired
	OrgAccountService orgaccountservice;
	
	@Autowired
	OrgHoldService orgholdservice;
	
	@Autowired
	OrgHoldRepository orgholdrepository;
	
	@Autowired
	OrgOrderService orgorderservice;
 
	@Autowired
	OrgTransactionService orgtransactionservice;
	
	@Autowired
	private OrgAccountRepository orgaccountrepository;
	
	@Autowired
	private OrgTransactionRepository orgtransactionrepository;

	@Autowired
    private CustomerService customerservice;
    @Autowired
    private AccountService accountservice;
    @Autowired
    private HoldService holdservice;
    @Autowired
    private HoldRepository holdrepository;
    @Autowired
    private TransactionService transactionservice;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionrepository;
    @Autowired
    private OrderService orderservice;
 
	  @GetMapping("/search-organization")
	  public String searchOrganization(
	            @RequestParam(value = "id", required = false) Long id,
	            @RequestParam(value = "orgName", required = false) String orgName,
	            @RequestParam(value = "email", required = false) String email,
	            @RequestParam(value = "phone", required = false) String phone,
	            Model model, HttpSession session) {
	    	
	    	
	    	
	    	 User user = (User) session.getAttribute("loggedInUser");
	     	if (user != null) {
	             model.addAttribute("user1", user);
	         } else {
	             return "redirect:/login"; 
	         }
	    	
	    	
	    	
	    	
	        boolean hasCriteria = (id != null || orgName != null ||  email != null || phone != null);
	        
	        if (!hasCriteria) 
	        {
	            return "customer_search.html"; 
	        }

	        List<Organization> organizations = orgservice.searchOrganizations(id, orgName, email, phone);
	        if (organizations.isEmpty()) 
	        {
	            return "no_records_found.html";
	        }
	        model.addAttribute("organizations", organizations);
	        return "search_result_org.html";
	    }
	  
	  
	  
	  @GetMapping("/overview-org/{id}")
	  public String getOrganizationOverview(@PathVariable Long id, Model model, HttpSession session) {
	       
		  User user = (User) session.getAttribute("loggedInUser");
	        if (user != null) {
	            model.addAttribute("user1", user);
	        } else {
	            return "redirect:/login"; 
	        }

	        Organization organization = orgservice.findOrganizationById(id); 
	        if (organization == null) {
	            return "redirect:/no-records-found"; 
	        }


	        List<OrgAccount> orgAccounts = organization.getOrgAccounts();
	        if (orgAccounts != null && !orgAccounts.isEmpty()) {
	            OrgAccount orgAccount = orgAccounts.get(0);
	            model.addAttribute("orgAccount", orgAccount); 
	        }
	        
	        model.addAttribute("organization", organization);
	        model.addAttribute("orgAccounts", orgAccounts);

	        
	        System.out.println("Organization Overview: All Done");
	        System.out.println("Org Accounts size: " + orgAccounts.size());

	        return "overview_org.html";
	    }
	  
	  
	  
	  @GetMapping("/edit_basic_org/{id}")
	  public String showEditOrganization(@PathVariable Long id, HttpSession session, Model model) {
	      User user = (User) session.getAttribute("loggedInUser");
	      if (user != null) {
	          model.addAttribute("user1", user);
	      } else {
	          return "redirect:/login";
	      }

	      Organization organization = orgservice.findOrganizationById(id);
	      if (organization == null) {
	          return "redirect:/no-records-found";
	      }

	      model.addAttribute("organization", organization);
	      return "edit_basic_org.html"; 
	  }

	  
	  @PostMapping("/update-org/{id}")
	  public String updateOrganization(@PathVariable Long id, @ModelAttribute Organization organization, HttpSession session) {
	      User user = (User) session.getAttribute("loggedInUser");
	      if (user == null) {
	          return "redirect:/login";
	      }

	      Organization existingOrganization = orgservice.findOrganizationById(organization.getId());
	      if (existingOrganization == null) {
	          return "redirect:/error";
	      }

	      // Update the fields with new values
	      existingOrganization.setOrgName(organization.getOrgName());
	      existingOrganization.setOrgPhone(organization.getOrgPhone());
	      existingOrganization.setOrgEmail(organization.getOrgEmail());
	      existingOrganization.setOrgAddress(organization.getOrgAddress());

	      orgservice.saveOrganization(existingOrganization);

	      return "redirect:/overview-org/" + organization.getId();
	  }
	  
	  
	  @GetMapping("/edit_account_organization/{orgAccountNo}/{organizationId}")
	  public String editOrganizationAccount(@PathVariable String orgAccountNo, @PathVariable long organizationId, HttpSession session, Model model) {
	      
	      User user = (User) session.getAttribute("loggedInUser");
	      if (user != null) {
	          model.addAttribute("user1", user);
	      } else {
	          return "redirect:/login"; 
	      }

	      // Find account by account number
	      OrgAccount orgaccount = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	      if (orgaccount == null) {
	          return "redirect:/no-records-found"; 
	      }

	      // Find organization by ID
	      Organization organization = orgservice.findOrganizationById(organizationId);
	      if (organization == null) {
	          return "redirect:/no-records-found"; 
	      }

	      model.addAttribute("orgaccount", orgaccount);
	      model.addAttribute("organization", organization);

	      return "edit_account_org.html"; 
	  }

	  
	  
	  
	  @PostMapping("/update_account_organization/{orgAccountNo}/{organizationId}")
	  public String updateOrganizationAccount(@PathVariable String orgAccountNo, 
	                                          @PathVariable Long organizationId, 
	                                          @ModelAttribute OrgAccount orgAccount,
	                                          HttpSession session) {

		  User user = (User) session.getAttribute("loggedInUser");
	      if (user == null) {
	          return "redirect:/login"; 
	      }

	      OrgAccount existingOrgAccount = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	      if (existingOrgAccount == null) {
	          return "redirect:/no-records-found"; 
	      }


	      existingOrgAccount.setOrgAccountType(orgAccount.getOrgAccountType());
	      existingOrgAccount.setOrgBranch(orgAccount.getOrgBranch());
	      existingOrgAccount.setOrgDepositAmount(orgAccount.getOrgDepositAmount());

	      orgaccountservice.saveOrgAccount(existingOrgAccount);

	      return "redirect:/overview-org/" + organizationId;
	  }

	  
	  @GetMapping("/edit_document_organization/{organizationid}")
	  public String editDocOrganization(@PathVariable Long organizationid, HttpSession session, Model model) {
	      
	      // Check if the user is logged in
	      User user = (User) session.getAttribute("loggedInUser");
	      if (user != null) {
	          model.addAttribute("user1", user);
	      } else {
	          return "redirect:/login"; 
	      } 
	      
	      // Find the organization by ID
	      Organization organization = orgservice.findOrganizationById(organizationid);
	      if (organization == null) {
	          return "redirect:/no-records-found"; 
	      }
	      
	      // Add organization to the model
	      model.addAttribute("organization", organization);
	      
	      // Return the view for editing the organization's documents
	      return "edit_document_org.html";
	  }
	  
	  
	  @PostMapping("/update_document_organization/{organizationid}")
	  public String updateDocumentOrganization(@PathVariable Long organizationid, 
	                                           @ModelAttribute Organization organization, 
	                                           HttpSession session) {
	      User user = (User) session.getAttribute("loggedInUser");
	      if (user == null) {
	          return "redirect:/login"; 
	      }

	      Organization existingOrganization = orgservice.findOrganizationById(organizationid);
	      if (existingOrganization == null) {
	          return "redirect:/error"; 
	      }

	      existingOrganization.setOrgdocumentId(organization.getOrgdocumentId());
	      existingOrganization.setOrgdocumentType1(organization.getOrgdocumentType1());
	      existingOrganization.setOrgdocumentType2(organization.getOrgdocumentType2());
	      existingOrganization.setOrguploadDate(organization.getOrguploadDate());

	      orgservice.saveOrganization(existingOrganization);
	      

	      return "redirect:/overview-org/" + existingOrganization.getId(); 
	  }

	  
	  
	  
	  @GetMapping("/account-org/{organizationid}/{orgAccountNo}")
	  public String openOrganizationOverview(@PathVariable Long organizationid, 
	                                         @PathVariable String orgAccountNo, 
	                                         HttpSession session, 
	                                         Model model) {
	      User user = (User) session.getAttribute("loggedInUser");
	      if (user != null) {
	          model.addAttribute("user1", user);
	      } else {
	          return "redirect:/login"; 
	      }

	      Organization organization = orgservice.findOrganizationById(organizationid);
	      if (organization == null) {
	          return "redirect:/no-records-found";
	      }

	      OrgAccount orgAccount = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	      if (orgAccount == null || !orgAccount.getOrganization().getId().equals(organizationid)) {
	          return "redirect:/no-records-found";
	      }

	      List<OrgAccount> orgAccounts = organization.getOrgAccounts(); 

	      Set<String> orgAccountNos = orgAccounts.stream()
	              .map(OrgAccount::getOrgAccountNo) 
	              .collect(Collectors.toSet()); 
	      
	      long totalAmountSum = orgAccounts.stream()
	    	        .mapToLong(account -> {
	    	            // Get last transaction for each account
	    	            OrgTransaction lastTransaction = orgtransactionservice.findLastTransactionByAccountNo(account.getOrgAccountNo());

	    	            // Debugging logs
	    	            System.out.println("Account No: " + account.getOrgAccountNo());
	    	            System.out.println("Deposit Amount: " + account.getOrgDepositAmount());
	    	            if (lastTransaction != null) {
	    	                System.out.println("Last Transaction Total Amount: " + lastTransaction.getOrgTotalAmount());
	    	            } else {
	    	                System.out.println("No transactions found for this account.");
	    	            }

	    	            // Fallback to deposit amount if no transaction or total amount is null
	    	            return (lastTransaction != null && lastTransaction.getOrgTotalAmount() != null) 
	    	                    ? lastTransaction.getOrgTotalAmount() 
	    	                    : account.getOrgDepositAmount();
	    	        })
	    	        .sum();

	      model.addAttribute("organization", organization);
	      model.addAttribute("orgAccount", orgAccount);
	      model.addAttribute("totalOrgAccountNos", orgAccountNos.size());
	      model.addAttribute("totalAmountSum", totalAmountSum);  // Add the total sum to the model


	      
	      return "Account_overview_org.html";
	  }

	  
	  
	  @GetMapping("/saving_acc_org/{organizationId}/{orgAccountNo}")   
	  public String openSavingOrg(@PathVariable Long organizationId, 
	                               @PathVariable String orgAccountNo, 
	                               HttpSession session, 
	                               Model model) {
	      User user = (User) session.getAttribute("loggedInUser");
	      if (user != null) {
	          model.addAttribute("user1", user);
	      } else {
	          return "redirect:/login"; 
	      }

	      Organization organization = orgservice.findOrganizationById(organizationId); 
	      if (organization == null) {
	          return "redirect:/no-records-found"; 
	      }
	      
	      OrgAccount orgAccount = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	      if (orgAccount == null || !orgAccount.getOrganization().getId().equals(organizationId)) {
	          return "redirect:/no-records-found"; 
	      }
	      
	        OrgTransaction latestTransaction = orgtransactionservice.findLatestTransactionForAccount(orgAccountNo);

	      
	      List<OrgHold> allHolds = orgholdservice.findHoldsByOrgAccount(orgAccount); 
	      List<OrgHold> activeHolds = allHolds.stream()
	              .filter(hold -> "Active".equals(hold.getOrgHoldStatus())) 
	              .toList();

	        model.addAttribute("orgtransaction", latestTransaction != null ? latestTransaction : new OrgTransaction());
	      
	      model.addAttribute("organization", organization);
	      model.addAttribute("orgAccount", orgAccount);
	      model.addAttribute("holds", activeHolds);  

	      System.out.println("OrgAccount: " + orgAccount);

	      return "savings_acc_org.html"; 
	  }

	  
	  
	  @PostMapping("/account-org/{organizationId}")
	  public String createFDOrgAccount(
	      @PathVariable Long organizationId,
	      @RequestParam("orgAccountNo") String orgAccountNo,
	      @RequestParam("orgAccountType") String orgAccountType,
	      @RequestParam("orgDepositAmount") long orgDepositAmount,
	      @RequestParam("orgBranch") String orgBranch,
	      @RequestParam("orgHoldAmount") long orgHoldAmount,
	      @RequestParam("orgHoldType") String orgHoldType,
	      @RequestParam("orgHoldExpireeDate") LocalDate orgHoldExpireeDate,
	      HttpSession session, Model model) {

	      User user = (User) session.getAttribute("loggedInUser");
	      if (user == null) {
	          return "redirect:/login";
	      }

	      Organization organization = orgservice.findOrganizationById(organizationId);
	      if (organization == null) {
	          return "redirect:/no-records-found";
	      }

	      OrgAccount account = new OrgAccount();
	      account.setOrgAccountNo(orgAccountNo);
	      account.setOrgAccountType(orgAccountType);
	      account.setOrgDepositAmount(orgDepositAmount);
	      account.setOrgBranch(orgBranch);

	      account.setOrganization(organization); 
	      orgaccountservice.saveOrgAccount(account);

	      OrgHold hold = new OrgHold();
	      hold.setOrgHoldAmount(orgHoldAmount);
	      hold.setOrgHoldExpireeDate(orgHoldExpireeDate);
	      hold.setOrgHoldType(orgHoldType);
	      hold.setOrgHoldCreatedDate(LocalDate.now());
	      hold.setOrgHoldStatus("Active");
	      hold.setOrgAccount(account);

	      orgholdservice.saveOrgHold(hold);

	      return "redirect:/account-org/" + organizationId + "/" + orgAccountNo;
	  }


	  
	  
	  @PostMapping("/credit-org")
	  public String creditOrgAmount(
	      @RequestParam("orgAccountNo") String orgAccountNo,
	      @RequestParam("orgTransactionType") String orgTransactionType,
	      @RequestParam("orgTransactionDate") LocalDate orgTransactionDate, 
	      @RequestParam("orgTAmount") long orgTAmount, 
	      @ModelAttribute OrgTransaction orgTransaction,  
	      Model model, RedirectAttributes redirectAttributes) {

	      OrgAccount orgAccount = orgaccountservice.findAccountByAccountNo(orgAccountNo);

	      if (orgAccount == null) {
	          model.addAttribute("error", "Organization Account not found");
	          return "error";
	      }

	      orgtransactionservice.creditOrgTransaction(orgAccountNo, orgTAmount, orgTransactionType, orgTransactionDate);
	      Long organizationId = orgAccount.getOrganization().getId(); 
	        
	      
	      redirectAttributes.addFlashAttribute("success", "Amount credited successfully to organization account!");
	      
	      return "redirect:/saving_acc_org/" + organizationId + "/" + orgAccountNo;
	  }
	  
	  


	
	  @PostMapping("/deposite-org")
	  public String depositeOrgAmount(
	      @RequestParam("orgAccountNo") String orgAccountNo,
	      @RequestParam("orgTransactionType") String orgTransactionType,
	      @RequestParam("orgTransactionDate") LocalDate orgTransactionDate, 
	      @RequestParam("orgTAmount") long orgTAmount, 
	      @ModelAttribute OrgTransaction orgTransaction,  
	      Model model, RedirectAttributes redirectAttributes) {

	      OrgAccount orgAccount = orgaccountservice.findAccountByAccountNo(orgAccountNo);

	      if (orgAccount == null) {
	          model.addAttribute("error", "Organization Account not found");
	          return "error";
	      }

	      orgtransactionservice.depositeOrgTransaction(orgAccountNo, orgTAmount, orgTransactionType, orgTransactionDate);
	      Long organizationId = orgAccount.getOrganization().getId(); 
	        
	      
	      redirectAttributes.addFlashAttribute("success", "Amount deposited successfully to organization account!");
	      
	      return "redirect:/saving_acc_org/" + organizationId + "/" + orgAccountNo;
	  }
	  
	  
	  
	  @PostMapping("/withdraw-org")
	    public String withdrawAmount(
	    		 @RequestParam("orgAccountNo") String accountNo,
	    	        @RequestParam("orgTransactionType") String transactionType,
	    	        @RequestParam("orgTransactionDate") LocalDate transactionDate, 
	    	        @RequestParam("orgTAmount") long amount, 
	    	        @ModelAttribute OrgTransaction orgtransaction,
	    	        Model model,RedirectAttributes redirectAttributes) 
	  {

	        OrgAccount account = orgaccountservice.findAccountByAccountNo(accountNo);

	        if (account == null) {
	            model.addAttribute("error", "Account not found");
	            return "error";
	        }

	        Long organizationId = account.getOrganization().getId(); 
	       
	        try {
	            orgtransactionservice.withdrawOrgTransaction(accountNo, amount, transactionType, transactionDate);
	            redirectAttributes.addFlashAttribute("success", "Amount withdrawn successfully!"); 
	        } catch (RuntimeException e) {
	            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
	            redirectAttributes.addFlashAttribute("account", account);
	        }  
	        

	        model.addAttribute("success", "Amount withdrawn successfully!");
	        return "redirect:/saving_acc_org/" + organizationId + "/" + accountNo;
	  
	  }
	  
	  @PostMapping("/debit-org")
	    public String debitAmount(
	    		 @RequestParam("orgAccountNo") String accountNo,
	    	        @RequestParam("orgTransactionType") String transactionType,
	    	        @RequestParam("orgTransactionDate") LocalDate transactionDate, 
	    	        @RequestParam("orgTAmount") long amount, 
	    	        @ModelAttribute OrgTransaction orgtransaction,
	    	        Model model,RedirectAttributes redirectAttributes) 
	  {

	        OrgAccount account = orgaccountservice.findAccountByAccountNo(accountNo);

	        if (account == null) {
	            model.addAttribute("error", "Account not found");
	            return "error";
	        }

	        Long organizationId = account.getOrganization().getId(); 
	       
	        try {
	            orgtransactionservice.debitOrgTransaction(accountNo, amount, transactionType, transactionDate);
	            redirectAttributes.addFlashAttribute("success", "Amount debited successfully!"); 
	        } catch (RuntimeException e) {
	            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
	            redirectAttributes.addFlashAttribute("account", account);
	        }  
	        

	        model.addAttribute("success", "Amount withdrawn successfully!");
	        return "redirect:/saving_acc_org/" + organizationId + "/" + accountNo;
	  
	  }
	  
	  
	  
	  @PostMapping("/add_orghold")
	    public String addHoldAmount(
	   		 @RequestParam("orgHoldAmount") long holdAmount,
	   	        @RequestParam("orgHoldType") String holdType,
	   	        @RequestParam("orgHoldCreatedDate") LocalDate holdCreatedDate, 
	   	        @RequestParam("orgHoldExpireeDate") LocalDate holdExpireeDate, 
	   	        @RequestParam("orgAccountNo") String accountNo,  
	   	        Model model,RedirectAttributes redirectAttributes) 
	    {
	    	
	        OrgAccount account = orgaccountrepository.findByOrgAccountNo(accountNo);
	        Long OrganizationId = account.getOrganization().getId(); 
	        
	        
	        List<OrgHold> activeHolds = orgholdservice.getHoldsByOrgAccountId(account.getId());

	        long totalHoldAmount = activeHolds.stream()
	                .mapToLong(OrgHold::getOrgHoldAmount)
	                .sum();

	        OrgTransaction lastTransaction = orgtransactionrepository.findLastTransactionByOrgAccountId(account.getId());
	        
	        long totalAmount = (lastTransaction != null) ? lastTransaction.getOrgTotalAmount() : account.getOrgDepositAmount();

	    	
	        if (holdAmount + totalHoldAmount > totalAmount) {
	            redirectAttributes.addFlashAttribute("alertMessage","Hold creation denied! The total hold amount exceeds available balance.");
	            return "redirect:/saving_acc_org/" + OrganizationId + "/" + accountNo; 
	        }
	        
	        OrgHold hold = new OrgHold();
	        hold.setOrgHoldAmount(holdAmount);
	        hold.setOrgHoldType(holdType);
	        hold.setOrgHoldCreatedDate(holdCreatedDate);
	        hold.setOrgHoldExpireeDate(holdExpireeDate);
	        hold.setOrgHoldStatus("Active");
	        
	        hold.setOrgAccount(account);
	        orgholdservice.saveOrgHold(hold);
	        
	        redirectAttributes.addFlashAttribute("success", "Hold Created successfully!"); 

	        

	        model.addAttribute("success", "Hold created successfully!");
	        return "redirect:/saving_acc_org/" + OrganizationId + "/" + accountNo;   
	        
	    }
	  
	  
	  @GetMapping("/org_hold_transacation/{organizationId}/{orgAccountNo}")
	    public String openHoldtransaction(
	        @PathVariable Long organizationId,
	        @PathVariable String orgAccountNo,
	        HttpSession session,
	        Model model) {

	        User user = (User) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/login";
	        }
	        model.addAttribute("user1", user);

	        Organization organization = orgservice.findOrganizationById(organizationId);
	        if (organization == null) {
	            return "redirect:/no-records-found"; 
	        }

	        OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	        if (account == null || !account.getOrganization().getId().equals(organizationId)) {
	            return "redirect:/no-records-found"; 
	        }

	        List<OrgHold> holds = orgholdservice.getHoldsByOrgAccountId(account.getId());

	        model.addAttribute("organization", organization);
	        model.addAttribute("orgAccount", account);
	        model.addAttribute("orghold", holds); 

	        return "hold_transaction_org.html";
	    }
	  
	  
	  @PostMapping("/org_hold_pass_action")
	    public String passHoldAction(
	        @RequestParam Long orgHoldId,
	        @RequestParam String orgAccountNo,
	        HttpSession session) {

	        User user = (User) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/login";
	        }

	        OrgHold hold = orgholdservice.findHoldById(orgHoldId);
	        OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	        if (hold == null || account == null) {
	            return "redirect:/no-records-found";
	        }

	        account.setOrgDepositAmount(account.getOrgDepositAmount() + hold.getOrgHoldAmount());
	        orgaccountservice.saveOrgAccount(account); 

	       
	        hold.setOrgHoldExpireeDate(LocalDate.now());
	        hold.setOrgHoldStatus("Passed"); 
	        orgholdservice.saveOrgHold(hold);  

	        OrgTransaction transaction = new OrgTransaction();
	       
	        OrgTransaction latestTransaction = orgtransactionservice.findLatestTransactionForAccount(orgAccountNo);
	        Long newTransactionId = (latestTransaction != null ? latestTransaction.getId() : 0) + 1;
	        
	        long previousTotalAmount = (latestTransaction != null && latestTransaction.getOrgTotalAmount() != null)
	                ? latestTransaction.getOrgTotalAmount()
	                : 0;
	        
	        transaction.setId(newTransactionId); 
	        transaction.setOrgTransactionType("Hold Passed");
	        transaction.setOrgTAmount(hold.getOrgHoldAmount());
	        transaction.setOrgTotalAmount(previousTotalAmount);
	        transaction.setOrgTransactionDate(LocalDate.now());
	        transaction.setOrgAccount(account);
	        orgtransactionservice.save(transaction);

	        return "redirect:/org_hold_transacation/" + account.getOrganization().getId() + "/" + account.getOrgAccountNo();
	    }


	  @GetMapping("/savings_acc_transacation_org/{organizationId}/{orgAccountNo}")
	    public String opentransaction(
	        @PathVariable Long organizationId,
	        @PathVariable String orgAccountNo,
	        HttpSession session,
	        Model model) {

	        User user = (User) session.getAttribute("loggedInUser");
	        if (user == null) {
	            return "redirect:/login";
	        }
	        model.addAttribute("user1", user);

	        Organization org = orgservice.findOrganizationById(organizationId);
	        if (org == null) {
	            return "redirect:/no-records-found"; 
	        }

	        OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
	        if (account == null || !account.getOrganization().getId().equals(organizationId)) {
	            return "redirect:/no-records-found"; 
	        }

	        List<OrgTransaction> transactions = orgtransactionservice.findTransactionsByAccountId(account.getId());

	        model.addAttribute("organization", org);
	        model.addAttribute("orgAccount", account);
	        model.addAttribute("orgtransactions", transactions); 

	        return "savings_acc_transaction_org.html";
	    }
	    
	  
	  
    
    @GetMapping("/home_search_org")
    public String Openadd_search_customer(HttpSession session,HttpServletRequest request,Model model) 
    {
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
            model.addAttribute("user1", user);
        } else {
            
            return "redirect:/login";
        }
        return "search_org.html";
    }
    
    
    
    @GetMapping("/add_org")   
   	public String openAddCutomer(HttpSession session , Model model)
   	{
   	User user = (User) session.getAttribute("loggedInUser");
   	if (user != null) {
           
           model.addAttribute("user1", user);
       } else {
           
           return "redirect:/login"; 
       } 
   		return "add_org.html";
   	}
    

    
    
    
    
    
    @PostMapping("/order-org/{organizationId}/{orgAccountNo}")
    public String handleOrder(@RequestParam("orderOType") String orderOType,
                              @RequestParam("bankOType") String bankOType,
                              @RequestParam("bankOName") String bankOName,
                              @RequestParam("bankOBranch") String bankOBranch,
                              @RequestParam("bankOIfsc") String bankOIfsc,
                              @RequestParam("orderOAccNo") String orderOAccNo,
                              @RequestParam("oOAmount") Long oOAmount,
                              @PathVariable Long organizationId,
                              @PathVariable String orgAccountNo,
                              Model model,RedirectAttributes redirectAttributes) 
    {
        
    	if(orderOType.equals("Book Order") && bankOType.equals("Internal")) 
    	{
            OrgAccount orderAccount = orgaccountservice.findAccountByAccountNo(orderOAccNo);

            if (orderAccount == null) 
            {
            	redirectAttributes.addFlashAttribute("alertMessage", "Receievers Account not found");
                return "redirect:/saving_acc/" + organizationId + "/" + orgAccountNo;   
            }
            
            OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	

            List<OrgTransaction> previousTransactions = orgtransactionrepository.findByOrgAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getOrgDepositAmount();
            } else {
                OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getOrgTotalAmount();
            }

            List<OrgHold> holds = orgholdrepository.findByOrgAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(OrgHold::getOrgHoldAmount).sum(); 
            long remainingBalance = totalAmount - oOAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oOAmount;
            
            OrgTransaction transaction=new OrgTransaction();
            transaction.setOrgTransactionType("Book Order");
            transaction.setOrgTAmount(oOAmount);
            LocalDate Date = LocalDate.now();
            transaction.setOrgTransactionDate(Date);
            transaction.setOrgTotalAmount(totalAmount); 
            transaction.setOrgAccount(account); 
            orgaccountservice.saveOrgAccount(account);
            
            orgtransactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            OrganizationOrder order= new OrganizationOrder();
            order.setOrderOType(orderOType);
            order.setBankOType(bankOType);
            order.setBankOName("BOM");
            order.setBankOBranch("Kharadi Branch");
            order.setBankOIfsc("MAHB0001399");
            order.setOrderOAccNo(orderOAccNo);
            order.setoOAmount(oOAmount);
            order.setoOTAmount(totalAmount);
            order.setoODate(LocalDate.now());
            order.setOrderOStatus("Transfer");
            order.setOrgAccount(account);
            
            orgorderservice.saveOrder(order);
            
            
            

            
            
            OrgTransaction lastTransactions = orgtransactionrepository.findLastOrderTransactionByOrgAccountNo(orderAccount.getOrgAccountNo());

         long TotalAmount;

         if (lastTransactions != null) {
             TotalAmount = lastTransactions.getOrgTotalAmount();
         } else {
             TotalAmount = orderAccount.getOrgDepositAmount();
         }
         
         System.out.println("previous transaction total AMount:"+ lastTransactions.getOrgTotalAmount());

         long newTotalAmount = TotalAmount + oOAmount;
            
            OrgTransaction creditTransaction = new OrgTransaction();
            creditTransaction.setOrgAccount(orderAccount);
            creditTransaction.setOrgTransactionType("Book Order");
            creditTransaction.setOrgTAmount(oOAmount);
            creditTransaction.setTransactionDate(LocalDate.now());
            creditTransaction.setOrgTotalAmount(newTotalAmount); 
            orgtransactionrepository.save(creditTransaction);
            orgaccountservice.saveOrgAccount(orderAccount);
            
            
            
            
            
            
            OrganizationOrder creditorder= new OrganizationOrder();
            creditorder.setOrderOType(orderOType);
            creditorder.setBankOType(bankOType);
            creditorder.setBankOName("BOM");
            creditorder.setOrderOAccNo(orgAccountNo);
            creditorder.setoOAmount(oOAmount);
            creditorder.setoOTAmount(newTotalAmount);
            creditorder.setoODate(LocalDate.now());
            creditorder.setOrderOStatus("Received");
            creditorder.setOrgAccount(orderAccount);
            
            orgorderservice.saveOrder(creditorder);  
    	}
    	//=======================================================================================================================================//
    	
    	if(orderOType.equals("Collection Order") && bankOType.equals("Internal")) 
    	{
            Account orderAccount = accountservice.findAccountByAccountNo(orderOAccNo);

            if (orderAccount == null) 
            {
            	redirectAttributes.addFlashAttribute("alertMessage", "Receievers Account not found");
                return "redirect:/saving_acc/" + organizationId + "/" + orgAccountNo;   
            }
            
            OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	

            List<OrgTransaction> previousTransactions = orgtransactionrepository.findByOrgAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getOrgDepositAmount();
            } else {
                OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getOrgTotalAmount();
            }

            List<OrgHold> holds = orgholdrepository.findByOrgAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(OrgHold::getOrgHoldAmount).sum(); 
            long remainingBalance = totalAmount - oOAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oOAmount;
            
            OrgTransaction transaction=new OrgTransaction();
            transaction.setOrgTransactionType("Collection Order");
            transaction.setOrgTAmount(oOAmount);
            LocalDate Date = LocalDate.now(); 
            transaction.setOrgTransactionDate(Date);
            transaction.setOrgTotalAmount(totalAmount); 
            transaction.setOrgAccount(account); 
            orgaccountservice.saveOrgAccount(account);
            
            orgtransactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            OrganizationOrder order= new OrganizationOrder();
            order.setOrderOType(orderOType);
            order.setBankOType(bankOType);
            order.setBankOName("BOM");
            order.setBankOBranch("Kharadi Branch");
            order.setBankOIfsc("MAHB0001399");
            order.setOrderOAccNo(orderOAccNo);
            order.setoOAmount(oOAmount);
            order.setoOTAmount(totalAmount);
            order.setoODate(LocalDate.now());
            order.setOrderOStatus("Transfer");
            order.setOrgAccount(account);
            
            orgorderservice.saveOrder(order);
            
            
            

            
            
            Transaction lastTransactions = transactionrepository.findLastOrderTransactionByAccountId(orderAccount.getAccountNo());

         long TotalAmount;

         if (lastTransactions != null) {
             TotalAmount = lastTransactions.getTotalAmount();
         } else {
             TotalAmount = orderAccount.getDepositAmount();
         }
         
         System.out.println("previous transaction total AMount:"+ lastTransactions.getTotalAmount());

         long newTotalAmount = TotalAmount + oOAmount;
            
         Transaction creditTransaction = new Transaction();
         creditTransaction.setAccount(orderAccount);
         creditTransaction.setTransactionType("Collection Order");
         creditTransaction.setAmount(oOAmount);
         creditTransaction.setTransactionDate(LocalDate.now());
         creditTransaction.setTotalAmount(newTotalAmount); 
         transactionrepository.save(creditTransaction);
         accountservice.saveAccount(orderAccount);
            
            
         Order creditorder= new Order();
         creditorder.setOrderType(orderOType);
         creditorder.setBankType(bankOType);
         creditorder.setBankName("BOM");
         creditorder.setBankBranch("Kharadi Branch");
         creditorder.setBankIfsc("MAHB0001399");
         creditorder.setOrderAccNo(orgAccountNo);
         creditorder.setoAmount(oOAmount);
         creditorder.setoTAmount(newTotalAmount);
         creditorder.setoDate(LocalDate.now());
         creditorder.setOrderStatus("Received");
         creditorder.setAccount(orderAccount);
         
         orderservice.saveOrder(creditorder);  
            
    	}
    	//========================================================================================================================================//
    	
    	
    	if(orderOType.equals("Collection Order") && bankOType.equals("External")) 
    	{
    		
            
            OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	 Account orderAccount = accountservice.findAccountByAccountNo(orderOAccNo);

         if (orderAccount != null) 
         {
         	redirectAttributes.addFlashAttribute("alertMessage", "Receivers Account is in Our Bank,Choose Internal Banking");
             return "redirect:/saving_acc/" + organizationId + "/" + orgAccountNo;   
         }
       	

            List<OrgTransaction> previousTransactions = orgtransactionrepository.findByOrgAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getOrgDepositAmount();
            } else {
                OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getOrgTotalAmount();
            }

            List<OrgHold> holds = orgholdrepository.findByOrgAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(OrgHold::getOrgHoldAmount).sum(); 
            long remainingBalance = totalAmount - oOAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oOAmount;
            
            OrgTransaction transaction=new OrgTransaction();
            transaction.setOrgTransactionType("Book Order");
            transaction.setOrgTAmount(oOAmount);
            LocalDate Date = LocalDate.now(); 
            transaction.setOrgTransactionDate(Date);
            transaction.setOrgTotalAmount(totalAmount); 
            transaction.setOrgAccount(account); 
            orgaccountservice.saveOrgAccount(account);
            
            orgtransactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            OrganizationOrder order= new OrganizationOrder();
            order.setOrderOType(orderOType);
            order.setBankOType(bankOType);
            order.setBankOName(bankOName);
            order.setBankOBranch(bankOBranch);
            order.setBankOIfsc(bankOIfsc);
            order.setOrderOAccNo(orderOAccNo);
            order.setoOAmount(oOAmount);
            order.setoOTAmount(totalAmount);
            order.setoODate(LocalDate.now());
            order.setOrderOStatus("Transfer");
            order.setOrgAccount(account);
            
            orgorderservice.saveOrder(order);

    	}
    	
    	
    	
    	
    	//=======================================================================================================================================//
    	if(orderOType.equals("Book Order") && bankOType.equals("External")) 
    	{
    		
            
            OrgAccount account = orgaccountservice.findAccountByAccountNo(orgAccountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	 OrgAccount orderAccount = orgaccountservice.findAccountByAccountNo(orderOAccNo);

         if (orderAccount != null) 
         {
         	redirectAttributes.addFlashAttribute("alertMessage", "Receivers Account is in Our Bank,Choose Internal Banking");
             return "redirect:/saving_acc/" + organizationId + "/" + orgAccountNo;   
         }
       	

            List<OrgTransaction> previousTransactions = orgtransactionrepository.findByOrgAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getOrgDepositAmount();
            } else {
                OrgTransaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getOrgTotalAmount();
            }

            List<OrgHold> holds = orgholdrepository.findByOrgAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(OrgHold::getOrgHoldAmount).sum(); 
            long remainingBalance = totalAmount - oOAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oOAmount;
            
            OrgTransaction transaction=new OrgTransaction();
            transaction.setOrgTransactionType("Book Order");
            transaction.setOrgTAmount(oOAmount);
            LocalDate Date = LocalDate.now(); 
            transaction.setOrgTransactionDate(Date);
            transaction.setOrgTotalAmount(totalAmount); 
            transaction.setOrgAccount(account); 
            orgaccountservice.saveOrgAccount(account);
            
            orgtransactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            OrganizationOrder order= new OrganizationOrder();
            order.setOrderOType(orderOType);
            order.setBankOType(bankOType);
            order.setBankOName(bankOName);
            order.setBankOBranch(bankOBranch);
            order.setBankOIfsc(bankOIfsc);
            order.setOrderOAccNo(orderOAccNo);
            order.setoOAmount(oOAmount);
            order.setoOTAmount(totalAmount);
            order.setoODate(LocalDate.now());
            order.setOrderOStatus("Transfer");
            order.setOrgAccount(account);
            
            orgorderservice.saveOrder(order);

    	}
    	
    	
    	 return "redirect:/saving_acc_org/" + organizationId + "/" + orgAccountNo; 
    }
    
    
    
    
    
    
    
    
    @GetMapping("/savings_acc_order_org/{organizationId}/{orgAccountNo}")
    public String openorder(
        @PathVariable Long organizationId,
        @PathVariable String orgAccountNo,
        HttpSession session,
        Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user1", user);

        Organization organization = orgservice.findOrganizationById(organizationId);
        if (organization == null) {
            return "redirect:/no-records-found"; 
        }

        OrgAccount orgAccount = orgaccountservice.findAccountByAccountNo(orgAccountNo);
        if (orgAccount == null || !orgAccount.getOrganization().getId().equals(organizationId)) {
            return "redirect:/no-records-found"; 
        }

        List<OrganizationOrder> orders = orgorderservice.findByAccountId(orgAccount.getId());

       
      model.addAttribute("organization", organization);
        model.addAttribute("orgAccount", orgAccount);
        model.addAttribute("orders", orders);


        return "savings_acc_order_org.html";
    }
    


    
    
    
    
    
    @PostMapping("/add_org")
    public String addOrganization(
            @RequestParam("orgName") String orgName,
            @RequestParam("orgEmail") String orgEmail,
            @RequestParam("orgPhone") String orgPhone,
            @RequestParam("orgAddress") String orgAddress,
            
            @RequestParam("orgAccountNo") String orgAccountNo,
            @RequestParam("orgAccountType") String orgAccountType,
            @RequestParam("orgBranch") String orgBranch,
            @RequestParam("orgDepositAmount") long orgDepositAmount,
            
            @RequestParam("orgHoldAmount") long orgHoldAmount,
            @RequestParam("orgHoldType") String orgHoldType,
            @RequestParam("orgHoldCreatedDate") LocalDate orgHoldCreatedDate,
            @RequestParam("orgHoldExpireeDate") LocalDate orgHoldExpireeDate,


            @RequestParam("orgdocumentId") String orgdocumentId,
            @RequestParam("orgdocumentType1") String orgdocumentType1,
            @RequestParam("orgdocumentFile1") MultipartFile orgdocumentFile1,
            @RequestParam("orgdocumentType2") String orgdocumentType2,
            @RequestParam("orgdocumentFile2") MultipartFile orgdocumentFile2,
            @RequestParam("orguploaddate") String orguploaddate,
            Model model) {

        try {
        	
        	  byte[] documentFile1Bytes = orgdocumentFile1.getBytes();
              byte[] documentFile2Bytes = orgdocumentFile2.getBytes();
              LocalDate uploadDateParsed = LocalDate.parse(orguploaddate);
        	
            // Create and set the organization details
            Organization organization = new Organization();
            organization.setOrgName(orgName);
            organization.setOrgEmail(orgEmail);
            organization.setOrgPhone(orgPhone);
            organization.setOrgdocumentId(orgdocumentId);
            organization.setOrgAddress(orgAddress);
            organization.setOrgdocumentType1(orgdocumentType1);
            organization.setOrgdocumentType2(orgdocumentType2);
            organization.setOrgdocumentFile1(documentFile1Bytes);
            organization.setOrgdocumentFile2(documentFile2Bytes);
            organization.setOrguploadDate(uploadDateParsed);

            orgservice.saveOrganization(organization);

            OrgAccount orgAccount = new OrgAccount();
            orgAccount.setOrgAccountNo(orgAccountNo);
            orgAccount.setOrgAccountType(orgAccountType);
            orgAccount.setOrgBranch(orgBranch);
            orgAccount.setOrgDepositAmount(orgDepositAmount);
            
            orgAccount.setOrganization(organization);
            orgaccountservice.saveOrgAccount(orgAccount);
            
            
            
            
            OrgHold orghold= new OrgHold();
            orghold.setOrgHoldAmount(orgHoldAmount);
            orghold.setOrgHoldType(orgHoldType);
            orghold.setOrgHoldCreatedDate(orgHoldCreatedDate);
            orghold.setOrgHoldExpireeDate(orgHoldExpireeDate);
            orghold.setOrgHoldStatus("Active");
            
            orghold.setOrgAccount(orgAccount);
            orgholdservice.saveOrgHold(orghold);

            model.addAttribute("successMessage", "Organization created successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to create organization!");
            return "error"; 
        }

        return "redirect:/home"; 
    }

    
    
}

