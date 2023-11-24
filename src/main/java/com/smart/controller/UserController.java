package com.smart.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import com.smart.entities.Contact;
import com.smart.entities.User;
import com.smart.helper.Message;
import com.smart.repository.ContactRepository;
import com.smart.repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/normal")
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ContactRepository contactRepository;
	 

	@GetMapping("/firstpage")
	public String newthing(Model model,Principal principal)
	{
		String userName=principal.getName();
		User users=userRepository.findByEmail(userName);
		model.addAttribute("title","User Dashboard");
		model.addAttribute("name", users.getName());
		return "normal/firstpage";
	}
	@GetMapping("/add-contact")
	public String openAddContactForm(Model model,Principal principal) {
		String userName=principal.getName();
		User users=userRepository.findByEmail(userName);
		model.addAttribute("name", users.getName());
		model.addAttribute("title", "Add Contact");
		model.addAttribute("contact", new Contact());
		
		return "normal/add_contact_form";
	}
	
	@GetMapping("/profile")
	public String yourProfilePage(Model model,Principal principal) {
		model.addAttribute("title", "Profile Page");
		String userName=principal.getName();
		User users=userRepository.findByEmail(userName);
		model.addAttribute(users);
		model.addAttribute("name", users.getName());
		return "normal/profile";
	}
	@PostMapping("/savecontact")
	public String savecontact(Model model,@ModelAttribute Contact contact, Principal principal,HttpSession session) {
		model.addAttribute("title", "Profile Page");
		String userName=principal.getName();
		User user=userRepository.findByEmail(userName);
		model.addAttribute(user);
		model.addAttribute("name", user.getName());
		try {
			contact.setUser(user);
			System.out.println(contact.getSecondname());
			System.out.println(contact.getDescription());
			user.getContacts().add(contact); //User store the contact 
			
			this.userRepository.save(user);
			System.out.println("Successfully added to database");
			//message success
			session.setAttribute("message", new Message("Your contact is added !! Add more ", "alert-success"));
			model.addAttribute(contact);
			return "normal/add_successfull";
		}catch(Exception e) {
			System.out.println("ERROR "+e.getMessage());
			e.printStackTrace();
		}
		return "normal/add_contact_form";
	}
	@GetMapping("/add_successfull")
	public String saving(Model model,Principal principal) {
		String userName=principal.getName();
		User user=userRepository.findByEmail(userName);
		model.addAttribute(user);
		model.addAttribute("name", user.getName());
		return "normal/add_successfull";
	}
	@GetMapping("/show-contact")
	public String showContacts(Model model,Principal principal){
		String userName=principal.getName();
		User user=userRepository.findByEmail(userName);
		model.addAttribute(user);
		model.addAttribute("name", user.getName());
		List<Contact> contacts=this.contactRepository.findContactsByUser(user.getId());
		model.addAttribute("contacts",contacts);
		return "normal/show_contacts";
	}
	@RequestMapping("/{cid}/contact")
    public String showContactDetails(@PathVariable("cid") Integer cid, Model model, Principal principal) {
        // System.out.println("Cid:"+cid);
        model.addAttribute("title", "Contact");

        Optional<Contact> contactOptional = this.contactRepository.findById(cid);


        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();

            // get current user
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username);
            
            // show contact only current user
            if (user.getId() == contact.getUser().getId()) {
            	model.addAttribute("name", user.getName());
                model.addAttribute("contact", contact);
            }
        }

        return "normal/contact_details";
    }
//	@PostMapping("/update-contact/{cId}")
//	public String updateContact(@PathVariable("cId")Integer cId,Model model,Principal principal) {
//		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
//		if (contactOptional.isPresent()) {
//            Contact contact = contactOptional.get();
//
//            // get current user
//            String username = principal.getName();
//            User user = this.userRepository.findByEmail(username);
//            
//            // show contact only current user
//            if (user.getId() == contact.getUser().getId()) {
//            	model.addAttribute("name", user.getName());
//                model.addAttribute("contact", contact);
//            }
//        }
//
//		return "normal/update_contact";
//	}
	@GetMapping("/delete/{cId}")
	public RedirectView deleteContact(@PathVariable("cId")Integer cId,Model model,Principal principal) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            this.contactRepository.deleteById(cId);
            System.out.println(contact.getName() + "deleted");
		}
		
		return new RedirectView("/normal/show-contact");
	}
	@PostMapping("/deleteContact/{cId}")
	public RedirectView deleteContactTwo(@PathVariable("cId")Integer cId,Model model,Principal principal) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            this.contactRepository.deleteById(cId);
            System.out.println(contact.getName() + "deleted");
		}
		
		return new RedirectView("/normal/show-contact");
	}
	@PostMapping("/updatecontact/{cId}")
	public String updatingContact(@PathVariable("cId")Integer cId,Model model,Principal principal) {
		Optional<Contact> contactOptional = this.contactRepository.findById(cId);
		if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            model.addAttribute("title","Update Contact");
            String username = principal.getName();
            User user = this.userRepository.findByEmail(username);
            if (user.getId() == contact.getUser().getId()) {
            	model.addAttribute("title","Update Contact");
            	model.addAttribute("name", user.getName());
                model.addAttribute("contact", contact);
                model.addAttribute("update_contact",new Contact());
            }
		}
		else {
			System.out.println("no contact found");
		}
		return "normal/update_contact";
	}
	@PostMapping("/updatingcontact")
	public RedirectView updatingthecontact(@ModelAttribute("update_contact") Contact update_contact,@ModelAttribute("contact") Contact contact,Model model,Principal principal,HttpSession session) {
		Optional<Contact> contactOptional=this.contactRepository.findById(contact.getcId());
		System.out.println(contact.getcId());
		System.out.println(update_contact.getcId());
		System.out.println(update_contact.getDescription());
		String username = principal.getName();
        User user = this.userRepository.findByEmail(username);
		if(contactOptional.isPresent()) {
			Contact contact1=contactOptional.get();
			contact1.setDescription(contact.getDescription());
			contact1.setEmail(contact.getEmail());
			contact1.setName(contact.getName());
			contact1.setPhone(contact.getPhone());
			contact1.setSecondname(contact.getSecondname());
			contact1.setWork(contact.getWork());
			this.contactRepository.save(contact1);
			model.addAttribute("contact", contact1);
			model.addAttribute("title","Updated Contact");
			model.addAttribute("name",user.getName());
			System.out.println("Updated");
		}
		String s=Integer.toString(contact.getcId());
		return new RedirectView("/normal/"+s+"/contact");
	}
	 @GetMapping("/search")
	    public String search(@RequestParam("query") String query,Model model, Principal principal){
	        User user = this.userRepository.findByEmail(principal.getName());
	        List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query,user);
			model.addAttribute(user);
			model.addAttribute("name", user.getName());
			model.addAttribute("contacts",contacts);
	        return "normal/show_contacts";
	    }
}
