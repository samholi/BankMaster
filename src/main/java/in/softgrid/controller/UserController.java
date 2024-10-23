package in.softgrid.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import in.softgrid.entity.User;
import in.softgrid.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String openRegister(Model model) {
        model.addAttribute("user", new User()); 
        return "register.html";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user1, Model model) {
        userService.registerUser(user1);
        model.addAttribute("successMessage", "Registered Successfully");
        model.addAttribute("user", new User());  
        return "register.html";
    }



    @GetMapping("/login")
    public String login(Model model) 
    {
    	model.addAttribute("user1", new User()); 
        return "index.html";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user1") User user, Model model, HttpSession session) {
        User V_user = userService.validateUser(user);

        if (V_user != null) {
            session.setAttribute("loggedInUser", V_user); 
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid login details.");
            return "index.html"; 
        }
    }

  


	
	  @GetMapping("/success") public String Opensuccess(Model model) {
	  model.addAttribute("user", new User()); 
	  return "success.html"; }
	 

    @GetMapping("/errorr")
    public String Openerror() {
        return "error.html";
    }
    
    
    @GetMapping("/welcome")
    public String openWelcome(@RequestParam String firstname, HttpSession session, HttpServletRequest request,Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user1", new User());

        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Request URL: " + request.getRequestURL());
        System.out.println("Request Method: " + request.getMethod());
        System.out.println("User in session: " + user + ", Timestamp: " + System.currentTimeMillis());
        System.out.println("Session user first name: " + user.getFirstname() + " &&&& URL first name: " + firstname);

        if (user == null || !user.getFirstname().equals(firstname)) {
            return "redirect:/login";
        }
        model.addAttribute("firstname", user.getFirstname());

        return "welcome.html";
    	}
       
 
    
    @GetMapping("/home")
    public String Openadd_search_customer(HttpSession session,HttpServletRequest request,Model model) 
    {
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
            model.addAttribute("user1", user);
        } else {
            
            return "redirect:/login";
        }
        return "home_search_customer.html";
    }
    
    
    
    

    
    @GetMapping("/logout")
    public String logout(HttpSession session , Model model) {
      
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
           
            model.addAttribute("user1", user);
        } else {
            return "redirect:/login"; 
        }
    	
    	
        return "overview.html";
    }
}
