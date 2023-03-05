package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.repository.UserRepository;

@Controller
public class HomeController {
	@Autowired
	private UserRepository userRepository;
	
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
	
	@GetMapping("/")
	public String home()
	{
		return "home";
	}
	
	@GetMapping("/home")
	public String newhome()
	{
		return "home";
	}
	@GetMapping("/signup")
	public String signup()
	{
		return "signup";
	}
	
}
