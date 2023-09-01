package com.smartcontact.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;
import com.smartcontact.repository.UserRepo;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	@Autowired
	private UserRepo userRepo;

	@GetMapping("/")
	public String homePageHandler(Model model) {
		model.addAttribute("content", "hello baby");
		model.addAttribute("title", "Home-SmartContactManager");
		return "home";
	}

	@GetMapping("/about")
	public String aboutPageHandler(Model model) {
		model.addAttribute("title", "About-SmartContactManager");
		return "about";
	}

	@GetMapping("/register")
	public String signupPageHandler(Model model) {
		model.addAttribute("title", "Register-SmartContactManager");
		model.addAttribute("user", new User());
		return "signup";
	}

	@PostMapping("/do-register") // there use model to show error if any
	public String doSignup(@Valid @ModelAttribute("user") User user,BindingResult result1, Model model, HttpSession session) throws InterruptedException {
		//bindingresult bind the error with entities to show
		try {
			if(result1.hasErrors()) {
				model.addAttribute("user",user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setActive(true);
			user.setAbout("Hi! I am using SmartContactManager.");
			user.setPassword(passwordEncoder.encode(user.getPassword()));//this password is encoded
			
			userRepo.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Succesfully registered!!", "alert-success"));
			
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Somthing went wrong!!", "alert-danger"));
			return "signup";
		}
		
	}
	
	/*
	 * @GetMapping("/user/index") public String dashBoard(Model model ) {
	 * model.addAttribute("title","User-Dashboard"); return "normal/user_dashboard";
	 * }
	 */
	@GetMapping("/signin")
	public String loginPageHandler(Model model) {
		model.addAttribute("title", "Login-SmartContactManager");
		return "login";
	}
	
	
}






