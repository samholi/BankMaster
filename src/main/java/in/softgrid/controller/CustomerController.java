package in.softgrid.controller;

import java.util.function.Function;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; // For using Set
import java.util.stream.Collectors; // For using Collectors


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
import in.softgrid.entity.Contact;
import in.softgrid.entity.Customer;
import in.softgrid.entity.Hold;
import in.softgrid.entity.Order;
import in.softgrid.entity.OrgAccount;
import in.softgrid.entity.OrgTransaction;
import in.softgrid.entity.OrganizationOrder;
import in.softgrid.entity.Transaction;
import in.softgrid.entity.User;
import in.softgrid.repositary.AccountRepository;
import in.softgrid.repositary.HoldRepository;
import in.softgrid.repositary.OrgAccountRepository;
import in.softgrid.repositary.OrgTransactionRepository;
import in.softgrid.repositary.TransactionRepository;
import in.softgrid.service.AccountService;
import in.softgrid.service.ContactService;
import in.softgrid.service.TransactionService;
import in.softgrid.service.CustomerService;
import in.softgrid.service.HoldService;
import in.softgrid.service.OrderService;
import in.softgrid.service.OrgAccountService;
import in.softgrid.service.OrgOrderService;
import jakarta.servlet.http.HttpSession;


@Controller
public class CustomerController {

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
    @Autowired
    private ContactService contactservice;
    
    @Autowired
	OrgAccountService orgaccountservice;
    @Autowired
	private OrgAccountRepository orgaccountrepository;
    @Autowired
	OrgOrderService orgorderservice;
	@Autowired
	private OrgTransactionRepository orgtransactionrepository;
    
    @GetMapping("/search")
    public String searchCustomers(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,HttpSession session,
            Model model) 
    {
    	
    	
    	
    	 User user = (User) session.getAttribute("loggedInUser");
     	if (user != null) {
             model.addAttribute("user1", user);
         } else {
             return "redirect:/login"; 
         }
    	
    	
    	
    	
        boolean hasCriteria = (id != null || firstname != null || lastname != null || email != null || phone != null);
        
        if (!hasCriteria) 
        {
            return "customer_search.html"; 
        }

        List<Customer> customers = customerservice.searchCustomers(id, firstname, lastname, email, phone);
        if (customers.isEmpty()) 
        {
            return "no_records_found.html";
        }
        model.addAttribute("customers", customers);
        return "search_result_cust.html";
    }
    
    
    
    
    
    
    @GetMapping("/overview/{id}")
    public String getCustomerOverview(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }

        Customer customer = customerservice.findCustomerById(id); 
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        String accountHolderName = customer.getFirstname() + " " + customer.getLastname();

        List<Account> accounts = customer.getAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            Account account = accounts.get(0);
            model.addAttribute("account", account); 
        }
        
        
        model.addAttribute("customer", customer);
        model.addAttribute("accountHolderName", accountHolderName);
        model.addAttribute("accounts", accounts); 

        System.out.println("All Done");
        return "overview.html";
    }
    
    
    @GetMapping("/profile/{id}")
    public String getProfileOverview(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }

        Customer customer = customerservice.findCustomerById(id); 
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        String accountHolderName = customer.getFirstname() + " " + customer.getLastname();

        List<Account> accounts = customer.getAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            Account account = accounts.get(0);
            model.addAttribute("account", account); 
        }
        
        
        model.addAttribute("customer", customer);
        model.addAttribute("accountHolderName", accountHolderName);
        model.addAttribute("accounts", accounts); 

        System.out.println("All Done");
        return "profile.html";
    }
    
    
    @GetMapping("/contact/{id}")
    public String CustomerContactOverview(@PathVariable Long id, Model model, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }

        Customer customer = customerservice.findCustomerById(id); 
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        String accountHolderName = customer.getFirstname() + " " + customer.getLastname();

        List<Account> accounts = customer.getAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            Account account = accounts.get(0);
            model.addAttribute("account", account); 
        }
        
        
        
        List<Contact> contacts = customer.getContacts();
        if (contacts != null && !contacts.isEmpty()) {
            model.addAttribute("contacts", contacts); 
        }
        
        
        
        model.addAttribute("customer", customer);
        model.addAttribute("accounts", accounts); 

        System.out.println("All Done");
        return "contact.html";
    }
    
    
    @PostMapping("/addPhone/{id}")
    public String addPh(
        @RequestParam("alternatePhone") String alternatePhone,
        @PathVariable Long id,
       
        @ModelAttribute Transaction transaction,
        Model model,RedirectAttributes redirectAttributes) {

        Customer customer = customerservice.findCustomerById(id);

        if (customer == null) {
            model.addAttribute("error", "customer not found");
            return "error";
        }

       Contact addcontact = new Contact();
       addcontact.setAlternatePhone(alternatePhone);
       
       addcontact.setCustomer(customer);
       
       contactservice.saveContact(addcontact);
       
        
        redirectAttributes.addFlashAttribute("success", "Phone No added successfully!");
        return "redirect:/contact/" + id ;
    }
    
    @PostMapping("/delete_phoneee/{contactId}/{customerId}")
    public String deletePhone(
            @PathVariable Long contactId,
            @PathVariable Long customerId,
            Model model,
            RedirectAttributes redirectAttributes)  {

        // Call the service to delete the contact
        contactservice.deleteContact(contactId);

        // Add success message
        redirectAttributes.addFlashAttribute("success", "Phone No deleted successfully!");

        // Redirect to the contact overview page for the customer
        return "redirect:/contact/" + customerId;
    }

    
    @PostMapping("/addEmail/{id}")
    public String addEmail(
        @RequestParam("alternateEmail") String alternateEmail,
        @PathVariable Long id,
       
        @ModelAttribute Transaction transaction,
        Model model,RedirectAttributes redirectAttributes) {

        Customer customer = customerservice.findCustomerById(id);

        if (customer == null) {
            model.addAttribute("error", "customer not found");
            return "error";
        }

       Contact addcontact = new Contact();
       addcontact.setAlternateEmail(alternateEmail);
       
       addcontact.setCustomer(customer);
       
       contactservice.saveContact(addcontact);
       
        
        redirectAttributes.addFlashAttribute("success", "Email added successfully!");
        return "redirect:/contact/" + id ;
    }
    
    
    @PostMapping("/delete_email/{contactId}/{customerId}")
    public String deleteEmail(
            @PathVariable Long contactId,
            @PathVariable Long customerId,
            Model model,
            RedirectAttributes redirectAttributes)  {

        // Call the service to delete the contact
        contactservice.deleteContact(contactId);

        // Add success message
        redirectAttributes.addFlashAttribute("success", "Email deleted successfully!");

        // Redirect to the contact overview page for the customer
        return "redirect:/contact/" + customerId;
    }
    
    @PostMapping("/uploadImage/{id}")
    public String uploadImage(@PathVariable Long id, @RequestParam("file") MultipartFile file, HttpSession session) {
        // Get the logged-in user from session
        User user = (User) session.getAttribute("loggedInUser");

        // Ensure the user is logged in
        if (user == null) {
            return "redirect:/login"; // Redirect to login if not logged in
        }

        // Find the customer by the ID provided in the URL
        Customer customer = customerservice.findCustomerById(id);

        // Ensure the customer exists
        if (customer == null) {
            return "redirect:/no-records-found";
        }

        try {
            // Convert the file to bytes
            byte[] imageBytes = file.getBytes();
            customer.setProfileImage(imageBytes);  // Set profile image bytes in the customer entity

            // Save the updated customer with the profile image
            customerservice.saveCustomer(customer);

        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/error"; // Redirect in case of an error
        }

        // Redirect back to the overview page
        return "redirect:/overview/" + customer.getId();
    }
    
    
   



    
    @GetMapping("/edit_basic_customer/{id}")
    public String showEditCustomer(@PathVariable Long id,HttpSession session , Model model) 
    {
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }
        Customer customer = customerservice.findCustomerById(id); 
        if (customer == null) {
            return "redirect:/no-records-found";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDob = customer.getDob().format(formatter);

        model.addAttribute("customer", customer);
        model.addAttribute("formattedDob", formattedDob);
        return "edit_basic.html";
    }
    
    @PostMapping("/updateCustomer/{id}")
    public String updateCustomer(@PathVariable Long id,@ModelAttribute Customer customer, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        
        Customer existingCustomer = customerservice.findCustomerById(customer.getId());
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        // Update the fields with new values
        existingCustomer.setFirstname(customer.getFirstname());
        existingCustomer.setLastname(customer.getLastname());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setDob(customer.getDob());

        customerservice.saveCustomer(existingCustomer);

        return "redirect:/overview/" + customer.getId();
    }
    
    
    @GetMapping("/edit_customer/{id}")
    public String editCustomer(@PathVariable Long id,HttpSession session , Model model) 
    {
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }
        Customer customer = customerservice.findCustomerById(id); 
        if (customer == null) {
            return "redirect:/no-records-found";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDob = customer.getDob().format(formatter);

        model.addAttribute("customer", customer);
        model.addAttribute("formattedDob", formattedDob);
        return "edit_customer.html";
    }
    
    
    @PostMapping("/edit_customer/{id}")
    public String editCustomer(@PathVariable Long id,@ModelAttribute Customer customer, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        
        Customer existingCustomer = customerservice.findCustomerById(customer.getId());
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        // Update the fields with new values
        existingCustomer.setFirstname(customer.getFirstname());
        existingCustomer.setLastname(customer.getLastname());
        existingCustomer.setGender(customer.getGender());
        existingCustomer.setDob(customer.getDob());

        customerservice.saveCustomer(existingCustomer);

        return "redirect:/profile/" + customer.getId();
    }

    
    
    @GetMapping("/edit_account_customer/{accountNo}/{customerId}")
    public String editAccount(@PathVariable String accountNo, @PathVariable long customerId, HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }

        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null) {
            return "redirect:/no-records-found";
        }

        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        model.addAttribute("account", account);
        model.addAttribute("customer", customer);

        return "edit_account.html"; 
    }

    
    @PostMapping("/update_account_customer/{accountNo}/{customerId}")
    public String updateAccount(@PathVariable String accountNo, 
                                 @PathVariable Long customerId, 
                                 @ModelAttribute Account account,
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        Account existingAccount = accountservice.findAccountByAccountNo(accountNo);
        if (existingAccount == null) {
            return "redirect:/no-records-found";
        }
        
        
        Customer existingCustomer = customerservice.findCustomerById(customerId);
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        existingAccount.setAccountNo(account.getAccountNo());
        existingAccount.setBranch(account.getBranch());
        existingAccount.setDepositAmount(account.getDepositAmount());

        accountservice.saveAccount(existingAccount);

        return "redirect:/overview/" + customerId; 
    }

    
    
    
    
    
    @GetMapping("/edit_document_customer/{id}")
    public String editDocCustomer(@PathVariable Long id,HttpSession session , Model model) 
    {
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
        
            model.addAttribute("user1", user);
        } else {
         
            return "redirect:/login"; 
        } 
    	
        Customer customer = customerservice.findCustomerById(id);
        if (customer == null) {
            return "redirect:/no-records-found";
        }
       
        model.addAttribute("customer", customer);
        
        return "edit_document.html";
    }
    
    
    
    
    @PostMapping("/update_document_customer/{id}")
    public String updateDocument(@PathVariable Long id, @ModelAttribute Customer customer, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        Customer existingCustomer = customerservice.findCustomerById(id);
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        existingCustomer.setDocumentId(customer.getDocumentId());
        existingCustomer.setDocumentType1(customer.getDocumentType1());
        existingCustomer.setDocumentType2(customer.getDocumentType2());
        existingCustomer.setUploadDate(customer.getUploadDate());

        customerservice.saveCustomer(existingCustomer);
        
      


        return "redirect:/overview/" + existingCustomer.getId(); 
    }

    
    @GetMapping("/edit_education/{id}")
    public String editEdu(@PathVariable Long id,HttpSession session , Model model) 
    {
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
        
            model.addAttribute("user1", user);
        } else {
         
            return "redirect:/login"; 
        } 
    	
        Customer customer = customerservice.findCustomerById(id);
        if (customer == null) {
            return "redirect:/no-records-found";
        }
       
        model.addAttribute("customer", customer);
        
        return "edit_education.html";
    }
    
    @PostMapping("/update_education/{id}")
    public String editEduC(@PathVariable Long id, @ModelAttribute Customer customer, HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        Customer existingCustomer = customerservice.findCustomerById(id);
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        existingCustomer.setSscSchool(customer.getSscSchool());
        existingCustomer.setHscSchool(customer.getHscSchool());
        existingCustomer.setUniversity(customer.getUniversity());

        customerservice.saveCustomer(existingCustomer);
        
      


        return "redirect:/profile/" + existingCustomer.getId(); 
    }
    
    
    @PostMapping("/updateCustomer_account/{customerId}/{accountId}")
    public String updateCustomerAccount(
            @PathVariable Long customerId,
            @PathVariable Long accountId,
            @ModelAttribute Account account, 
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

       
        Customer existingCustomer = customerservice.findCustomerById(customerId);
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        Account accountToUpdate = existingCustomer.getAccounts()
                .stream()
                .filter(acc -> acc.getId().equals(accountId))
                .findFirst()
                .orElse(null);

        if (accountToUpdate == null) {
            return "redirect:/error";
        }

        
        accountToUpdate.setAccountHolderName(account.getAccountHolderName());
        accountToUpdate.setAccountType(account.getAccountType());
        accountToUpdate.setBranch(account.getBranch());
        accountToUpdate.setDepositAmount(account.getDepositAmount());

       
        accountservice.saveAccount(accountToUpdate);

        return "redirect:/overview/" + customerId;
    }

    
    
    
    
    
    @GetMapping("/account/{id}/{accountNo}")
    public String openAccountoverview(@PathVariable Long id, @PathVariable String accountNo, HttpSession session, Model model) {
       
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }

        Customer customer = customerservice.findCustomerById(id);
        if (customer == null) {
            return "redirect:/no-records-found";
        }

        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null || !account.getCustomer().getId().equals(id)) {
            return "redirect:/no-records-found";
        }
        

       
        List<Account> accounts = customer.getAccounts(); 


        Set<String> accountNos = accounts.stream()
                .map(Account::getAccountNo) 
                .collect(Collectors.toSet()); 


        
        double totalAmount = 0.0; 
        for (Account acc : accounts) {
            double lastTransactionTotalAmount = transactionservice.getLastTransactionTotalAmountByAccountNo(acc.getAccountNo());
            
            if (lastTransactionTotalAmount == 0.0) {
                lastTransactionTotalAmount = acc.getDepositAmount();
            }

            totalAmount += lastTransactionTotalAmount;
        }

        
        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("totalAccountNos", accountNos.size());
        model.addAttribute("totalAmount", totalAmount);

        return "Account_overview.html";
    }

    
    

    
    @PostMapping("/account/{id}")
    public String createFDAccount(
        @PathVariable Long id,
        @RequestParam("customerId") Long customerId,   
        @RequestParam("accountHolderName") String accountHolderName,
        @RequestParam("accountNo") String accountNo,
        @RequestParam("accountType") String accountType,
        @RequestParam("depositAmount") long depositAmount,
        @RequestParam("branch") String branch,
        @RequestParam("holdAmount") long holdAmount,
        @RequestParam("holdType") String holdType,
        @RequestParam("holdExpireeDate") LocalDate holdExpireeDate,
        HttpSession session, Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        Account account = new Account();
        account.setAccountHolderName(accountHolderName);
        account.setAccountNo(accountNo);
        account.setAccountType(accountType);
        account.setDepositAmount(depositAmount);
        account.setBranch(branch);
        
        account.setCustomer(customer);  
        accountservice.saveAccount(account);
        
        
        Hold hold1=new Hold();
        hold1.setHoldAmount(holdAmount);
        hold1.setHoldExpireeDate(holdExpireeDate);
        hold1.setHoldType(holdType);
        LocalDate holdCreatedDate = LocalDate.now(); 
        hold1.setHoldCreatedDate(holdCreatedDate); 
        hold1.setHoldStatus("Active");
        
        hold1.setAccount(account);
        holdservice.saveHold(hold1);


       

        
        hold1.setHoldStatus("Active");
        
        

        return "redirect:/account/" + id + "/" +accountNo; 
    }


    
    
    
    
    
    @GetMapping("/Success_Customer")
    public String showSuccessCustomer() {
        
        return "Success_AddCustom.html";
    }
    
   
    
    
    
    
    
    
    
    
   
    
    
    @GetMapping("/saving_acc/{customerId}/{accountNo}")   
    public String opensaving(@PathVariable Long customerId, @PathVariable String accountNo, HttpSession session, Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }

        Customer customer = customerservice.findCustomerById(customerId); 
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }
        
        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null || !account.getCustomer().getId().equals(customerId)) {
            return "redirect:/no-records-found"; 
        }
        
        Transaction latestTransaction = transactionservice.findLatestTransactionForAccount(accountNo);
        
        List<Hold> allHolds = holdservice.findHoldsByAccount(account); 
        List<Hold> activeHolds = allHolds.stream()
                .filter(hold -> "Active".equals(hold.getHoldStatus())) 
                .toList();

        model.addAttribute("transaction", latestTransaction != null ? latestTransaction : new Transaction()); // Default to a new Transaction if null

        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("holds", activeHolds);  


        
        System.out.println("Account: " + account);
        System.out.println("Latest Transaction: " + latestTransaction);


        return "savings_acc.html";
    }

  
    
    @GetMapping("/savings_acc_transacation/{customerId}/{accountNo}")
    public String opentransaction(
        @PathVariable Long customerId,
        @PathVariable String accountNo,
        HttpSession session,
        Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user1", user);

        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null || !account.getCustomer().getId().equals(customerId)) {
            return "redirect:/no-records-found"; 
        }

        List<Transaction> transactions = transactionservice.findTransactionsByAccountId(account.getId());

        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions); 

        return "savings_acc_transacation.html";
    }
    
    
    
    
    @PostMapping("/hold_pass_action")
    public String passHoldAction(
        @RequestParam Long holdId,
        @RequestParam String accountNo,
        HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }

        Hold hold = holdservice.findHoldById(holdId);
        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (hold == null || account == null) {
            return "redirect:/no-records-found";
        }

        account.setDepositAmount(account.getDepositAmount() + hold.getHoldAmount());
        accountservice.saveAccount(account); 

       
        hold.setHoldExpireeDate(LocalDate.now());
        hold.setHoldStatus("Passed"); 
        holdservice.saveHold(hold);  

        Transaction transaction = new Transaction();
       
        Transaction latestTransaction = transactionservice.findLatestTransactionForAccount(accountNo);
        Long newTransactionId = (latestTransaction != null ? latestTransaction.getId() : 0) + 1;
        
        long previousTotalAmount = (latestTransaction != null && latestTransaction.getTotalAmount() != null)
                ? latestTransaction.getTotalAmount()
                : 0;
        
        transaction.setId(newTransactionId); 
        transaction.setTransactionType("Hold Passed");
        transaction.setAmount(hold.getHoldAmount());
        transaction.setTotalAmount(previousTotalAmount);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setAccount(account);
        transactionservice.save(transaction);

        return "redirect:/savings_acc_transacation/" + account.getCustomer().getId() + "/" + account.getAccountNo();
    }

    
    
    @GetMapping("/hold_transacation/{customerId}/{accountNo}")
    public String openHoldtransaction(
        @PathVariable Long customerId,
        @PathVariable String accountNo,
        HttpSession session,
        Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user1", user);

        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null || !account.getCustomer().getId().equals(customerId)) {
            return "redirect:/no-records-found"; 
        }

        List<Hold> holds = holdservice.getHoldsByAccountId(account.getId());

        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("holds", holds); 

        return "hold_transaction.html";
    }


    
    @PostMapping("/credit")
    public String creditAmount(
        @RequestParam("accountNo") String accountNo,
        @RequestParam("transactionType") String transactionType,
        @RequestParam("transactionDate") LocalDate transactionDate, 
        @RequestParam("amount") long amount, 
        @ModelAttribute Transaction transaction,
        Model model,RedirectAttributes redirectAttributes) {

        Account account = accountservice.findAccountByAccountNo(accountNo);

        if (account == null) {
            model.addAttribute("error", "Account not found");
            return "error";
        }

        transactionservice.creditTransaction(accountNo, amount,transactionType, transactionDate); 
        Long customerId = account.getCustomer().getId(); 
        
        redirectAttributes.addFlashAttribute("success", "Amount credited successfully!");
        return "redirect:/saving_acc/" + customerId + "/" + accountNo;
    }

    
    @PostMapping("/deposit")
    public String depositAmount(
        @RequestParam("accountNo") String accountNo,
        @RequestParam("transactionType") String transactionType,
        @RequestParam("transactionDate") LocalDate transactionDate, 
        @RequestParam("amount") long amount, 
        @ModelAttribute Transaction transaction,
        Model model,RedirectAttributes redirectAttributes) {

        Account account = accountservice.findAccountByAccountNo(accountNo);

        if (account == null) {
            model.addAttribute("error", "Account not found");
            return "error";
        }

        transactionservice.depositTransaction(accountNo, amount,transactionType, transactionDate);
        Long customerId = account.getCustomer().getId(); 

        
        redirectAttributes.addFlashAttribute("success", "Amount deposited successfully!");
        return "redirect:/saving_acc/" + customerId + "/" + accountNo;
    }
    
    
    
    @PostMapping("/withdraw")
    public String withdrawAmount(
    		 @RequestParam("accountNo") String accountNo,
    	        @RequestParam("transactionType") String transactionType,
    	        @RequestParam("transactionDate") LocalDate transactionDate, 
    	        @RequestParam("amount") long amount, 
    	        @ModelAttribute Transaction transaction,
    	        Model model,RedirectAttributes redirectAttributes) {

        Account account = accountservice.findAccountByAccountNo(accountNo);

        if (account == null) {
            model.addAttribute("error", "Account not found");
            return "error";
        }

        Long customerId = account.getCustomer().getId(); 
       
        try {
            transactionservice.withdrawTransaction(accountNo, amount, transactionType, transactionDate);
            redirectAttributes.addFlashAttribute("success", "Amount withdrawn successfully!"); 
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage());
            redirectAttributes.addFlashAttribute("account", account);
        }  
  
        
        

        model.addAttribute("success", "Amount withdrawn successfully!");
        return "redirect:/saving_acc/" + customerId + "/" + accountNo;
    }

    
    @PostMapping("/debit")
    public String debitAmount(
    		 @RequestParam("accountNo") String accountNo,
    	        @RequestParam("transactionType") String transactionType,
    	        @RequestParam("transactionDate") LocalDate transactionDate, 
    	        @RequestParam("amount") long amount, 
    	        @ModelAttribute Transaction transaction,
    	        Model model,RedirectAttributes redirectAttributes) {

        Account account = accountservice.findAccountByAccountNo(accountNo);

        if (account == null) {
            model.addAttribute("error", "Account not found");
            return "error";
        }

      
       
     
        Long customerId = account.getCustomer().getId(); 
        
        try {
            transactionservice.debitTransaction(accountNo, amount, transactionType, transactionDate);
            redirectAttributes.addFlashAttribute("success", "Amount debited successfully!"); 
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage()); 
            redirectAttributes.addFlashAttribute("account", account); 
        }  
  

        model.addAttribute("success", "Amount withdrawn successfully!");
        return "redirect:/saving_acc/" + customerId + "/" + accountNo;
    }

    
    @PostMapping("/add_hold")
    public String addHoldAmount(
   		 @RequestParam("holdAmount") long holdAmount,
   	        @RequestParam("holdType") String holdType,
   	        @RequestParam("holdCreatedDate") LocalDate holdCreatedDate, 
   	        @RequestParam("holdExpireeDate") LocalDate holdExpireeDate, 
   	        @RequestParam("accountNo") String accountNo,  
   	        Model model,RedirectAttributes redirectAttributes) 
    {
    	
        Account account = accountRepository.findByAccountNo(accountNo);
        Long customerId = account.getCustomer().getId(); 

        
        List<Hold> activeHolds = holdservice.getHoldsByAccountId(account.getId());
        long totalHoldAmount = activeHolds.stream()
                .mapToLong(Hold::getHoldAmount)
                .sum();

        Transaction lastTransaction = transactionrepository.findLastTransactionByAccountId(account.getId());
        
        long totalAmount = (lastTransaction != null) ? lastTransaction.getTotalAmount() : account.getDepositAmount();

        if (holdAmount + totalHoldAmount > totalAmount) {
            redirectAttributes.addFlashAttribute("alertMessage", "Hold creation denied! The total hold amount exceeds available balance.");
            return "redirect:/saving_acc/" + customerId + "/" + accountNo; 
        }
        
        
        Hold hold = new Hold();
        hold.setHoldAmount(holdAmount);
        hold.setHoldType(holdType);
        hold.setHoldCreatedDate(holdCreatedDate);
        hold.setHoldExpireeDate(holdExpireeDate);
        hold.setHoldStatus("Active");
        
        hold.setAccount(account);
        holdservice.saveHold(hold);
        
        redirectAttributes.addFlashAttribute("success", "Hold Created successfully!"); // Use flash attribute for success

        

        model.addAttribute("success", "Hold created successfully!");
        return "redirect:/saving_acc/" + customerId + "/" + accountNo;   
        
    }
    
    
    
    @PostMapping("/order/{customerId}/{accountNo}")
    public String handleOrder(@RequestParam("orderType") String orderType,
                              @RequestParam("bankType") String bankType,
                              @RequestParam("bankName") String bankName,
                              @RequestParam("bankBranch") String bankBranch,
                              @RequestParam("bankIfsc") String bankIfsc,
                              @RequestParam("orderAccNo") String orderAccNo,
                              @RequestParam("oAmount") Long oAmount,
                              @PathVariable Long customerId,
                              @PathVariable String accountNo,
                              Model model,RedirectAttributes redirectAttributes) 
    {
        
    	if(orderType.equals("Book Order") && bankType.equals("Internal")) 
    	{
            Account orderAccount = accountservice.findAccountByAccountNo(orderAccNo);

            if (orderAccount == null) 
            {
            	redirectAttributes.addFlashAttribute("alertMessage", "Receievers Account not found");
                return "redirect:/saving_acc/" + customerId + "/" + accountNo;   
            }
            
            Account account = accountservice.findAccountByAccountNo(accountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	

            List<Transaction> previousTransactions = transactionrepository.findByAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getDepositAmount();
            } else {
                Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getTotalAmount();
            }

            List<Hold> holds = holdrepository.findByAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(Hold::getHoldAmount).sum(); 
            long remainingBalance = totalAmount - oAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oAmount;
            
            Transaction transaction=new Transaction();
            transaction.setTransactionType("Book Order");
            transaction.setAmount(oAmount);
            LocalDate Date = LocalDate.now(); 
            transaction.setTransactionDate(Date);
            transaction.setTotalAmount(totalAmount); 
            transaction.setAccount(account); 
            accountservice.saveAccount(account);
            
            transactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            Order order= new Order();
            order.setOrderType(orderType);
            order.setBankType(bankType);
            order.setBankName("BOM");
            order.setBankBranch("Kharadi Branch");
            order.setBankIfsc("MAHB0001399");
            order.setOrderAccNo(orderAccNo);
            order.setoAmount(oAmount);
            order.setoTAmount(totalAmount);
            order.setoDate(LocalDate.now());
            order.setOrderStatus("Transfer");
            order.setAccount(account);
            
            orderservice.saveOrder(order);
            
            
            

            
            
            Transaction lastTransactions = transactionrepository.findLastOrderTransactionByAccountId(orderAccount.getAccountNo());

         long TotalAmount;

         if (lastTransactions != null) {
             TotalAmount = lastTransactions.getTotalAmount();
         } else {
             TotalAmount = orderAccount.getDepositAmount();
         }
         
         System.out.println("previous transaction total AMount:"+ lastTransactions.getTotalAmount());

         long newTotalAmount = TotalAmount + oAmount;
            
            Transaction creditTransaction = new Transaction();
            creditTransaction.setAccount(orderAccount);
            creditTransaction.setTransactionType("Book Order");
            creditTransaction.setAmount(oAmount);
            creditTransaction.setTransactionDate(LocalDate.now());
            creditTransaction.setTotalAmount(newTotalAmount); 
            transactionrepository.save(creditTransaction);
            accountservice.saveAccount(orderAccount);
            
            
            
            
            
            
            Order creditorder= new Order();
            creditorder.setOrderType(orderType);
            creditorder.setBankType(bankType);
            creditorder.setBankName("BOM");
            creditorder.setBankBranch("Kharadi Branch");
            creditorder.setBankIfsc("MAHB0001399");
            creditorder.setOrderAccNo(accountNo);
            creditorder.setoAmount(oAmount);
            creditorder.setoTAmount(newTotalAmount);
            creditorder.setoDate(LocalDate.now());
            creditorder.setOrderStatus("Received");
            creditorder.setAccount(orderAccount);
            
            orderservice.saveOrder(creditorder);  
    	}
    	//=======================================================================================================================================//
    	
    	
    	if(orderType.equals("Collection Order") && bankType.equals("Internal")) 
    	{
            OrgAccount orderAccount = orgaccountservice.findAccountByAccountNo(orderAccNo);

            if (orderAccount == null) 
            {
            	redirectAttributes.addFlashAttribute("alertMessage", "Receievers Account not found");
                return "redirect:/saving_acc/" + customerId + "/" + accountNo;   
            }
            
            Account account = accountservice.findAccountByAccountNo(accountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	

            List<Transaction> previousTransactions = transactionrepository.findByAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getDepositAmount();
            } else {
                Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getTotalAmount();
            }

            List<Hold> holds = holdrepository.findByAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(Hold::getHoldAmount).sum(); 
            long remainingBalance = totalAmount - oAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oAmount;
            
            Transaction transaction=new Transaction();
            transaction.setTransactionType("Collection Order");
            transaction.setAmount(oAmount);
            LocalDate Date = LocalDate.now(); 
            transaction.setTransactionDate(Date);
            transaction.setTotalAmount(totalAmount); 
            transaction.setAccount(account); 
            accountservice.saveAccount(account);
            
            transactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            Order order= new Order();
            order.setOrderType(orderType);
            order.setBankType(bankType);
            order.setBankName("BOM");
            order.setOrderAccNo(orderAccNo);
            order.setBankBranch("Kharadi Branch");
            order.setBankIfsc("MAHB0001399");
            order.setoAmount(oAmount);
            order.setoTAmount(totalAmount);
            order.setoDate(LocalDate.now());
            order.setOrderStatus("Transfer");
            order.setAccount(account);
            
            orderservice.saveOrder(order);
            
            
            

            
            
            OrgTransaction lastTransactions = orgtransactionrepository.findLastOrderTransactionByOrgAccountNo(orderAccount.getOrgAccountNo());

         long TotalAmount;

         if (lastTransactions != null) {
             TotalAmount = lastTransactions.getOrgTotalAmount();
         } else {
             TotalAmount = orderAccount.getOrgDepositAmount();
         }
         
         System.out.println("previous transaction total AMount:"+ lastTransactions.getOrgTotalAmount());

         long newTotalAmount = TotalAmount + oAmount;
            
         OrgTransaction creditTransaction = new OrgTransaction();
         creditTransaction.setOrgAccount(orderAccount);
         creditTransaction.setOrgTransactionType("Collection Order");
         creditTransaction.setOrgTAmount(oAmount);
         creditTransaction.setTransactionDate(LocalDate.now());
         creditTransaction.setOrgTotalAmount(newTotalAmount); 
         orgtransactionrepository.save(creditTransaction);
         orgaccountservice.saveOrgAccount(orderAccount);
            
            
            
            
            
            
         OrganizationOrder creditorder= new OrganizationOrder();
         creditorder.setOrderOType(orderType);
         creditorder.setBankOType(bankType);
         creditorder.setBankOName("BOM");
         creditorder.setBankOBranch("Kharadi Branch");
         creditorder.setBankOIfsc("MAHB0001399");
         creditorder.setOrderOAccNo(accountNo);
         creditorder.setoOAmount(oAmount);
         creditorder.setoOTAmount(newTotalAmount);
         creditorder.setoODate(LocalDate.now());
         creditorder.setOrderOStatus("Received");
         creditorder.setOrgAccount(orderAccount);
         
         orgorderservice.saveOrder(creditorder); 
    	}
    	
    	
    	//=======================================================================================================================================//
    	
    	if(orderType.equals("Collection Order") && bankType.equals("External")) 
    	{
    		
            
            Account account = accountservice.findAccountByAccountNo(accountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	 OrgAccount orderAccount = orgaccountservice.findAccountByAccountNo(orderAccNo);

         if (orderAccount != null) 
         {
         	redirectAttributes.addFlashAttribute("alertMessage", "Receivers Account is in Our Bank,Choose Internal Banking");
             return "redirect:/saving_acc/" + customerId + "/" + accountNo;   
         }
       	

            List<Transaction> previousTransactions = transactionrepository.findByAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getDepositAmount();
            } else {
                Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getTotalAmount();
            }

            List<Hold> holds = holdrepository.findByAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(Hold::getHoldAmount).sum(); 
            long remainingBalance = totalAmount - oAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oAmount;
            
            Transaction transaction=new Transaction();
            transaction.setTransactionType("Collection Order");
            transaction.setAmount(oAmount);
            LocalDate Date = LocalDate.now(); 
            transaction.setTransactionDate(Date);
            transaction.setTotalAmount(totalAmount); 
            transaction.setAccount(account); 
            accountservice.saveAccount(account);
            
            transactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            Order order= new Order();
            order.setOrderType(orderType);
            order.setBankType(bankType);
            order.setBankName(bankName);
            order.setBankBranch(bankBranch);
            order.setBankIfsc(bankIfsc);
            order.setOrderAccNo(orderAccNo);
            order.setoAmount(oAmount);
            order.setoTAmount(totalAmount);
            order.setoDate(LocalDate.now());
            order.setOrderStatus("Transfer");
            order.setAccount(account);
            
            orderservice.saveOrder(order);

    	}
    	
    	//========================================================================================================================================//
    	
    	if(orderType.equals("Book Order") && bankType.equals("External")) 
    	{
    		
            
            Account account = accountservice.findAccountByAccountNo(accountNo);
            
       	   if (account == null) 
       	   {
                model.addAttribute("error", "Account not found");
                return "error";
           }

       	 Account orderAccount = accountservice.findAccountByAccountNo(orderAccNo);

         if (orderAccount != null) 
         {
         	redirectAttributes.addFlashAttribute("alertMessage", "Receivers Account is in Our Bank,Choose Internal Banking");
             return "redirect:/saving_acc/" + customerId + "/" + accountNo;   
         }
       	

            List<Transaction> previousTransactions = transactionrepository.findByAccountId(account.getId());

            long totalAmount;
            if (previousTransactions.isEmpty()) {
                totalAmount = account.getDepositAmount();
            } else {
                Transaction lastTransaction = previousTransactions.get(previousTransactions.size() - 1);
                totalAmount = lastTransaction.getTotalAmount();
            }

            List<Hold> holds = holdrepository.findByAccountId(account.getId());
            long totalHoldAmount = holds.stream().mapToLong(Hold::getHoldAmount).sum(); 
            long remainingBalance = totalAmount - oAmount;

            if (remainingBalance < totalHoldAmount) {
                throw new RuntimeException("BooK Order denied! The remaining balance after Order cannot be less than the total of the hold amounts: " + totalHoldAmount);
            }
            totalAmount = totalAmount - oAmount;
            
            Transaction transaction=new Transaction();
            transaction.setTransactionType("Book Order");
            transaction.setAmount(oAmount);
            LocalDate Date = LocalDate.now();
            transaction.setTransactionDate(Date);
            transaction.setTotalAmount(totalAmount); 
            transaction.setAccount(account); 
            accountservice.saveAccount(account);
            
            transactionrepository.save(transaction);
            
            redirectAttributes.addFlashAttribute("success", "Order Placed successfully!");
            
            
            Order order= new Order();
            order.setOrderType(orderType);
            order.setBankType(bankType);
            order.setBankName(bankName);
            order.setBankBranch(bankBranch);
            order.setBankIfsc(bankIfsc);
            order.setOrderAccNo(orderAccNo);
            order.setoAmount(oAmount);
            order.setoTAmount(totalAmount);
            order.setoDate(LocalDate.now());
            order.setOrderStatus("Transfer");
            order.setAccount(account);
            
            orderservice.saveOrder(order);

    	}
    	 return "redirect:/saving_acc/" + customerId + "/" + accountNo; 
    }
    
    
    
    
    
    
    
    @GetMapping("/add_customer")   
   	public String openAddCutomer(HttpSession session , Model model)
   	{
   	User user = (User) session.getAttribute("loggedInUser");
   	if (user != null) {
           
           model.addAttribute("user1", user);
       } else {
           
           return "redirect:/login"; 
       } 
   		return "add_customer.html";
   	}
    
    
    
    
    @GetMapping("/savings_acc_order/{customerId}/{accountNo}")
    public String openorder(
        @PathVariable Long customerId,
        @PathVariable String accountNo,
        HttpSession session,
        Model model) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user1", user);

        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null || !account.getCustomer().getId().equals(customerId)) {
            return "redirect:/no-records-found"; 
        }

        List<Order> orders = orderservice.findByAccountId(account.getId());

       
      model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("orders", orders);


        return "savings_acc_order.html";
    }
    


    @PostMapping("/add_customer")
    public String addCustomer(
        @RequestParam("firstname") String firstname,
        @RequestParam("lastname") String lastname,
        @RequestParam("gender") String gender,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("address") String address,
        @RequestParam("dob") String dob,
        @RequestParam("account_holder_name") String accountHolderName,
        @RequestParam("accountType") String accountType,
        @RequestParam("accountNo") String accountNo,
        @RequestParam("branch") String branch,
        @RequestParam("deposite_amount") long depositAmount,
        @RequestParam("holdAmount") long holdAmount,
        @RequestParam("holdType") String holdType,
        @RequestParam("holdCreatedDate") LocalDate holdCreatedDate,
        @RequestParam("holdExpireeDate") LocalDate holdExpireeDate,
        @RequestParam("ssc_school") String sscSchool,
        @RequestParam("hsc_school") String hscSchool,
        @RequestParam("university") String university,
        @RequestParam("document_id") String documentId,
        @RequestParam("document_type1") String documentType1,
        @RequestParam("documentFile1") MultipartFile documentFile1,
        @RequestParam("document_type2") String documentType2,
        @RequestParam("documentFile2") MultipartFile documentFile2,
        @RequestParam("upload_date") String uploadDate,
        @RequestParam("note") String note,
        Model model) {

        try {
            byte[] documentFile1Bytes = documentFile1.getBytes();
            byte[] documentFile2Bytes = documentFile2.getBytes();
            LocalDate dobDate = LocalDate.parse(dob);
            LocalDate uploadDateParsed = LocalDate.parse(uploadDate);

            Customer customer = new Customer();
            customer.setFirstname(firstname);
            customer.setLastname(lastname);
            customer.setGender(gender);
            customer.setEmail(email);
            customer.setPhone(phone);
            customer.setAddress(address);
            customer.setDob(dobDate);
            customer.setSscSchool(sscSchool);
            customer.setHscSchool(hscSchool);
            customer.setUniversity(university);
            customer.setDocumentId(documentId);
            customer.setDocumentType1(documentType1);
            customer.setDocumentFile1(documentFile1Bytes);
            customer.setDocumentType2(documentType2);
            customer.setDocumentFile2(documentFile2Bytes);
            customer.setUploadDate(uploadDateParsed);

            customerservice.saveCustomer(customer);

            Account account = new Account();
            account.setAccountHolderName(accountHolderName);
            account.setAccountType(accountType);
            account.setAccountNo(accountNo);
            account.setBranch(branch);
            account.setDepositAmount(depositAmount);
          
            account.setCustomer(customer); 
            accountservice.saveAccount(account);
            
            
            Hold hold1 = new Hold();
            hold1.setHoldAmount(holdAmount);
            hold1.setHoldType(holdType);
            hold1.setHoldCreatedDate(holdCreatedDate);
            hold1.setHoldExpireeDate(holdExpireeDate);
            hold1.setHoldStatus("Active");
            
            hold1.setAccount(account);
            holdservice.saveHold(hold1);
            
 
            model.addAttribute("successMessage", "Customer created successfully!");

        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("errorMessage", "Failed to create customer!");
            return "error";
        }

        return "redirect:/home";
    }

}