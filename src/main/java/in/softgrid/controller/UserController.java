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
        model.addAttribute("user", new User()); // Add an empty User object to the model
        return "register.html";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user1) {
        userService.registerUser(user1);
        return "redirect:/success";
    }


    @GetMapping("/login")
    public String login(Model model) 
    {
    	model.addAttribute("user1", new User()); // Add an empty User object to the model
        return "login.html";
    }

    @PostMapping("/login")
    public String loginUser(@ModelAttribute("user1") User user, HttpSession session) {
        User V_user = userService.validateUser(user);

        if (V_user != null) {
            session.setAttribute("loggedInUser", V_user); // Store user information in the session
            System.out.println("User logged in: " + V_user.getFirstname()+" "+ V_user.getLastname()); // Log user email

            return "redirect:/home";
        } else {
            return "redirect:/errorr";
        }
    }

  


	
	  @GetMapping("/success") public String Opensuccess(Model model) {
	  model.addAttribute("user", new User()); // Add a new user object if neededfor binding 
	  return "success.html"; }
	 

    @GetMapping("/errorr")
    public String Openerror() {
        return "error.html";
    }
    
    
    @GetMapping("/welcome")
    public String openWelcome(@RequestParam String firstname, HttpSession session, HttpServletRequest request,Model model) {
        User user = (User) session.getAttribute("loggedInUser");
        model.addAttribute("user1", new User());
        // Log request details
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
            // Add the user to the model
            model.addAttribute("user1", user);
        } else {
            // Handle case where user is not found in the session
            return "redirect:/login"; // Or handle as needed
        }
        return "home_search_customer.html";
    }
    
    
    
    

    
    @GetMapping("/logout")
    public String logout(HttpSession session , Model model) {
        // Invalidate the session to log the user out
       // session.invalidate();
    	User user = (User) session.getAttribute("loggedInUser");
    	if (user != null) {
            // Add the user to the model
            model.addAttribute("user1", user);
        } else {
            // Handle case where user is not found in the session
            return "redirect:/login"; // Or handle as needed
        }
    	
    	
        return "overview.html";
    }
}
