package in.softgrid.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
import in.softgrid.entity.Customer;
import in.softgrid.entity.Transaction;
import in.softgrid.entity.User;
import in.softgrid.repositary.AccountRepository;
import in.softgrid.service.AccountService;
import in.softgrid.service.TransactionService;
import in.softgrid.service.CustomerService;
import jakarta.servlet.http.HttpSession;


@Controller
public class CustomerController {

    @Autowired
    private CustomerService customerservice;
    @Autowired
    private AccountService accountservice;
    @Autowired
    private TransactionService transactionservice;
    
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
    	
    	
    	
    	
    	 // Check if any search criteria is provided
        boolean hasCriteria = (id != null || firstname != null || lastname != null || email != null || phone != null);
        
        if (!hasCriteria) 
        {
            // No criteria provided, redirect to a page or show message accordingly
            return "customer_search.html"; // Or you can return an empty list or a different view
        }

        List<Customer> customers = customerservice.searchCustomers(id, firstname, lastname, email, phone);
        if (customers.isEmpty()) 
        {
            return "no_records_found.html";              // Redirect to a page indicating no records found
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

        // Retrieve customer information based on the ID
        Customer customer = customerservice.findCustomerById(id); 
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        // Create account holder name from first and last name
        String accountHolderName = customer.getFirstname() + " " + customer.getLastname();

        // Assuming a customer has multiple accounts
        List<Account> accounts = customer.getAccounts();
        if (accounts != null && !accounts.isEmpty()) {
            Account account = accounts.get(0); // Just using the first account as an example
            model.addAttribute("account", account); // Pass the account object to the model
        }
        
        // Add attributes to the model
        model.addAttribute("customer", customer);
        model.addAttribute("accountHolderName", accountHolderName);
        model.addAttribute("accounts", accounts); // Pass the list of accounts to the model

        System.out.println("All Done");
        return "overview.html"; // Thymeleaf template name without the .html extension
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
        // Format LocalDate to String
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

    
    
    @GetMapping("/edit_account_customer/{accountNo}/{customerId}")
    public String editAccount(@PathVariable String accountNo, @PathVariable long customerId, HttpSession session, Model model) {
        
        User user = (User) session.getAttribute("loggedInUser");
        if (user != null) {
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }

        // Retrieve the account using accountNo
        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null) {
            return "redirect:/no-records-found";
        }

        // Retrieve the associated customer using customerId
        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        // Add both account and customer to the model
        model.addAttribute("account", account);
        model.addAttribute("customer", customer); // Ensure customer is added

        return "edit_account.html"; // Thymeleaf template name without .html
    }

    
    @PostMapping("/update_account_customer/{accountNo}/{customerId}")
    public String updateAccount(@PathVariable String accountNo, 
                                 @PathVariable Long customerId, 
                                 @ModelAttribute Account account, // Assuming you have an Account model
                                 HttpSession session) {
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        // Find the existing account by accountNo (if you have a method for this)
        Account existingAccount = accountservice.findAccountByAccountNo(accountNo);
        if (existingAccount == null) {
            return "redirect:/no-records-found";
        }
        
        
        Customer existingCustomer = customerservice.findCustomerById(customerId);
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        // Update the fields with new values
        existingAccount.setAccountNo(account.getAccountNo());
        existingAccount.setBranch(account.getBranch());
        existingAccount.setDepositAmount(account.getDepositAmount());

        // Save the updated account
        accountservice.saveAccount(existingAccount);

        return "redirect:/overview/" + customerId; // Redirect to the customer's overview page
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

        // Retrieve the existing customer by ID
        Customer existingCustomer = customerservice.findCustomerById(id);
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        // Update the document fields with new values
        existingCustomer.setDocumentId(customer.getDocumentId());
        existingCustomer.setDocumentType1(customer.getDocumentType1());
        existingCustomer.setDocumentType2(customer.getDocumentType2());
        existingCustomer.setUploadDate(customer.getUploadDate());

        // Save the updated customer details
        customerservice.saveCustomer(existingCustomer);
        
      


        return "redirect:/overview/" + existingCustomer.getId(); // Redirect to the overview page of the updated customer
    }

    
    
    @PostMapping("/updateCustomer_account/{customerId}/{accountId}")
    public String updateCustomerAccount(
            @PathVariable Long customerId,
            @PathVariable Long accountId,
            @ModelAttribute Account account, // Change to Account since you're updating account details
            HttpSession session) {

        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        // Retrieve the customer by ID
        Customer existingCustomer = customerservice.findCustomerById(customerId);
        if (existingCustomer == null) {
            return "redirect:/error"; 
        }

        // Retrieve the account associated with the customer by accountId
        Account accountToUpdate = existingCustomer.getAccounts() // Assuming getAccounts() returns a List<Account>
                .stream()
                .filter(acc -> acc.getId().equals(accountId))
                .findFirst()
                .orElse(null);

        if (accountToUpdate == null) {
            return "redirect:/error"; // Handle case where account is not found
        }

        // Update the fields of the account
        accountToUpdate.setAccountHolderName(account.getAccountHolderName());
        accountToUpdate.setAccountType(account.getAccountType());
        accountToUpdate.setBranch(account.getBranch());
        accountToUpdate.setDepositAmount(account.getDepositAmount());

        // Save the updated account
        accountservice.saveAccount(accountToUpdate);

        return "redirect:/overview/" + customerId; // Redirect to overview
    }

    
    
    
    
    
    @GetMapping("/account/{id}/{accountNo}")
    public String openAccountoverview(@PathVariable Long id, @PathVariable String accountNo, HttpSession session, Model model) {
        // Check if the user is logged in
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


        // Calculate unique account types and total amount
        Set<String> accountNos = accounts.stream()
                .map(Account::getAccountNo) 
                .collect(Collectors.toSet()); 

        
        
        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("totalAccountNos", accountNos.size());
        
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
        @RequestParam("hold") long hold,
        @RequestParam("branch") String branch,
        HttpSession session, Model model) {

        // Check if user is logged in
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login"; 
        }

        // Retrieve the customer using the ID
        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        Account account = new Account();
        account.setAccountHolderName(accountHolderName);
        account.setAccountNo(accountNo);
        account.setAccountType(accountType);
        account.setDepositAmount(depositAmount);
        account.setHold(hold);
        account.setBranch(branch);
        account.setCustomer(customer);  

        accountservice.saveAccount(account);

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
        
        

        // Add the latest transaction to the model
        model.addAttribute("transaction", latestTransaction != null ? latestTransaction : new Transaction()); // Default to a new Transaction if null

        model.addAttribute("customer", customer);
        model.addAttribute("account", account);

        
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

        // Check if the user is logged in
        User user = (User) session.getAttribute("loggedInUser");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("user1", user);

        // Check if the customer exists
        Customer customer = customerservice.findCustomerById(customerId);
        if (customer == null) {
            return "redirect:/no-records-found"; 
        }

        // Check if the account exists and is associated with the customer
        Account account = accountservice.findAccountByAccountNo(accountNo);
        if (account == null || !account.getCustomer().getId().equals(customerId)) {
            return "redirect:/no-records-found"; 
        }

        // Fetch all transactions for the account
        List<Transaction> transactions = transactionservice.findTransactionsByAccountId(account.getId());

        // Add customer, account, and transactions to the model
        model.addAttribute("customer", customer);
        model.addAttribute("account", account);
        model.addAttribute("transactions", transactions);  // Add transactions list

        // Return the view for the transaction page
        return "savings_acc_transacation.html";
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

        // Fetch the account using account number
        Account account = accountservice.findAccountByAccountNo(accountNo);

        if (account == null) {
            model.addAttribute("error", "Account not found");
            return "error";
        }

        Long customerId = account.getCustomer().getId(); 
       
        // Perform the withdrawal transaction
        try {
            transactionservice.withdrawTransaction(accountNo, amount, transactionType, transactionDate);
            redirectAttributes.addFlashAttribute("success", "Amount withdrawn successfully!"); // Use flash attribute for success
        } catch (RuntimeException e) {
            // Catch the error if balance is less than the hold amount
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage()); // Pass the error message to the redirect attributes
            redirectAttributes.addFlashAttribute("account", account); // Add account back to the redirect attributes
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

        // Fetch the account using account number
        Account account = accountservice.findAccountByAccountNo(accountNo);

        if (account == null) {
            model.addAttribute("error", "Account not found");
            return "error";
        }

      
       
     
        Long customerId = account.getCustomer().getId(); 
        
        // Perform the withdrawal transaction
        try {
            transactionservice.debitTransaction(accountNo, amount, transactionType, transactionDate);
            redirectAttributes.addFlashAttribute("success", "Amount debited successfully!"); // Use flash attribute for success
        } catch (RuntimeException e) {
            // Catch the error if balance is less than the hold amount
            redirectAttributes.addFlashAttribute("alertMessage", e.getMessage()); // Pass the error message to the redirect attributes
            redirectAttributes.addFlashAttribute("account", account); // Add account back to the redirect attributes
        }  
  

        model.addAttribute("success", "Amount withdrawn successfully!");
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
    


    @PostMapping("/add_customer")
    public String addCustomer(
        @RequestParam("firstname") String firstname,
        @RequestParam("lastname") String lastname,
        @RequestParam("email") String email,
        @RequestParam("phone") String phone,
        @RequestParam("address") String address,
        @RequestParam("dob") String dob,
        @RequestParam("account_holder_name") String accountHolderName,
        @RequestParam("accountType") String accountType,
        @RequestParam("accountNo") String accountNo,
        @RequestParam("branch") String branch,
        @RequestParam("deposite_amount") long depositAmount,
        @RequestParam("hold") long hold,
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

            // Step 1: Save Customer
            Customer customer = new Customer();
            customer.setFirstname(firstname);
            customer.setLastname(lastname);
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

            // Save the customer first
            customerservice.saveCustomer(customer);

            // Step 2: Create and Save Account
            Account account = new Account();
            account.setAccountHolderName(accountHolderName);
            account.setAccountType(accountType);
            account.setAccountNo(accountNo);
            account.setBranch(branch);
            account.setDepositAmount(depositAmount);
            account.setHold(hold);
            // Associate account with the customer
            account.setCustomer(customer);  // Set the customer relationship

            // Save the account
            accountservice.saveAccount(account);

        } catch (IOException e) {
            e.printStackTrace();
            // Handle the error
        }

        return "redirect:/home";
    }

}