package com.smart.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BCryptPasswordEncoder PasswordEncoder;
//	@GetMapping("/test")
//	@ResponseBody
//	public String test()
//	{
//		User user=new User();
//		user.setName("Roshan");
//		user.setEmail("roshan.io");
//		Contact contact=new Contact();
//		user.getContacts().add(contact);
//		userRepository.save(user);
//		return "working";
//		
//		
//	}
	
	@GetMapping("/home")
	public String home()
	{
		return "home";
	}
	@GetMapping("/about")
	public String about() {
		return "about";
	}
	@GetMapping("/signup")
	public String signup(Model model)
	{
		model.addAttribute("user", new User());
		return "signup";
	}
	@PostMapping("/do_register")
    public String regidterUser(@ModelAttribute("user") User user,BindingResult result1, @RequestParam(value="agreement",defaultValue = "false") boolean agreement, Model model, HttpSession session) {
    	System.out.println("in register");
    	try {
    		
    		if(!agreement) {
        		System.out.println("you haven't agreed the terms and conditions");
        		model.addAttribute("errorMessage","you haven't agreed the terms and conditions");
        		throw new Exception("you haven't agreed the terms and conditions");
        	}
    		
    		if(result1.hasErrors()) {
    			System.out.println("ERROR "+result1.toString());
    			model.addAttribute("errorMessage",result1.toString());
    			model.addAttribute("user", user);
    			return "signup";
    		}
        	user.setRole("ROLE_USER");
        	user.setEnabled(true);
        	user.setPassword(PasswordEncoder.encode(user.getPassword()));
        	
        	
        	System.out.println(agreement);
        	System.out.println(user);
        	
        	User result=this.userRepository.save(user);
        	model.addAttribute("user", new User() );
        	return "signup";
    	}catch(Exception e) {
    		e.printStackTrace();
    		model.addAttribute("errorMessage",result1.toString());
    		model.addAttribute("user", user);
    		return "signup";
    	}
    }
	
    //handler for custom login
    @GetMapping("/signin")
   public String customLogin(Model model) {System.out.println("2");
    	model.addAttribute("title", "login Page");
	   return "signin"; 
	   }
    @GetMapping("/")
	public RedirectView signin(Model model,Principal principal)
	{
		String userName=principal.getName();
		User users=userRepository.findByEmail(userName);
		System.out.println(users.getRole());
		if((users.getRole()).equals("ROLE_USER"))
		{
			System.out.println("1");
			return new RedirectView("/normal/firstpage");
		}
		model.addAttribute("userName", userName);
		System.out.println("login Successful");
		return null;
	}
	@GetMapping("/signin/error")
	public String failureLogin()
	{
		return "signin";
	}
	@PostMapping("/change-password")
	public String changePassword(Model model, HttpSession session,@RequestParam("username") String username,
	            @RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
		
		User currentUser = this.userRepository.findByEmail(username);
		
		if (this.PasswordEncoder.matches(oldPassword, currentUser.getPassword())) {
			currentUser.setPassword(this.PasswordEncoder.encode(newPassword));
			this.userRepository.save(currentUser);
			
		} else {
			return "redirect:/forgotpassword";
		}
	
	return "redirect:/signin";
	
	}
	@GetMapping("/forgotpassword")
	 public String openSetting(Model model,Principal principal) {
       model.addAttribute("title", "Settings");
       return "forgotpassword";
	}
}
