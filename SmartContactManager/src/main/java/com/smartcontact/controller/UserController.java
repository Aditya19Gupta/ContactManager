package com.smartcontact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.smartcontact.entities.Contact;
import com.smartcontact.entities.PaymentOrder;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;
import com.smartcontact.repository.ContactRepo;
import com.smartcontact.repository.PaymentOrderRpo;
import com.smartcontact.repository.UserRepo;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private PaymentOrderRpo payRepo;
	@Autowired
	private UserRepo repo;
	@Autowired
	private ContactRepo contactRepo;
	
	@ModelAttribute
	public void commoData(Model m, Principal p) {
		String name = p.getName();

		m.addAttribute("title", "User-Dashboard");
		User user = repo.getUserByUsername(name);

		m.addAttribute("user", user);
	}

	@GetMapping("/index")
	public String dashBoard(Model model, Principal principal) {
		// Pricipal :This interface represents the abstract notion of a principal, which
		// can be used to represent any entity, such as an individual, acorporation, and
		// a login id.

		model.addAttribute("title", "User-Dashboard");

		return "normal/user_dashboard";
	}

	@GetMapping("/add_contact")
	public String addContactHandler(Model model) {
		model.addAttribute("title", "Add Contact-Smart Contact Manager");
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}

	@PostMapping("/process-contact")
	public String processAddContact(@ModelAttribute("contact") Contact contact,
			@RequestParam("profileImg") MultipartFile file, Principal principal, HttpSession session) {
		try {

			// file uploading
			if (file.isEmpty()) {
				contact.setImageUrl("default.jpg");
				System.out.println("empty");
			} else {
				// file is not empty
				contact.setImageUrl(file.getOriginalFilename());

				// create path where file upload
				File uFile = new ClassPathResource("/static/img").getFile();
				Path path = Paths.get(uFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				// use Files class in java.nio.*package
				// Files.copy(file.inputStream, complete path,Option when file not exists)
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

				System.out.println("Uploaded..");
			}

			String name = principal.getName();
			User user = this.repo.getUserByUsername(name);
			contact.setUser(user);
			user.getContact().add(contact);
			this.repo.save(user);
			session.setAttribute("message", new Message("Contact added successfully!!", "success"));
			System.out.println("User contact:" + contact);

		} catch (Exception e) {
			session.setAttribute("message", new Message("Something went wrong..", "danger"));
			e.printStackTrace();
		}
		return "normal/add_contact";
	}

	// show contacts
	@GetMapping("/contacts")
	public String showContacts(Model m, Principal p) {
		m.addAttribute("title", "SmartContactManager-Contacts");
		String userName = p.getName();
		User user = repo.getUserByUsername(userName);
		List<Contact> contacts = contactRepo.findContactsByUserId(user.getId());
		m.addAttribute("contacts", contacts);
		return "normal/show_contacts";
	}

	// search bar handler
	/*
	 * @GetMapping("/contacts/search") public String
	 * searchContact(@RequestParam("username") String name, Model m, Principal p) {
	 * m.addAttribute("title", "SmartContactManager-Contacts"); String userName =
	 * p.getName(); User user = repo.getUserByUsername(userName); List<Contact>
	 * searchContact = contactRepo.getContactByUsername(name, user.getId());
	 * m.addAttribute("contacts", searchContact); System.out.println(searchContact);
	 * 
	 * return "normal/show_contacts"; }
	 */

	@GetMapping("/contacts/profile")
	public String getProfileByContacts(@ModelAttribute("contacts") Contact c, Model m) {

		return "normal/show_contacts";
	}

	@GetMapping("/contacts/{cid}")
	public String getContactsProfile(@PathVariable("cid") int id, Model m, Principal p) {

		Optional<Contact> contactOptional = this.contactRepo.findById(id);
		Contact contact = contactOptional.get();
		String name = p.getName();
		repo.getUserByUsername(name);
		/*
		 * if(user.getId()==contact.getUser().getId())
		 * 
		 */
		m.addAttribute("contact", contact);
		return "normal/contact_profile";
	}

	// update contact
	@GetMapping("/contacts/update/{cid}")
	public String updateContact(@PathVariable("cid") int id, Model m) {
		Optional<Contact> optionalContact = contactRepo.findById(id);
		Contact contact = optionalContact.get();
		m.addAttribute("ucontact", contact);
		return "normal/update_profile";
	}

	@PostMapping("/contacts/update-process")
	public String updateContact(@ModelAttribute("ucontact") Contact c, @RequestParam("profileImg") MultipartFile file,
			Principal p, HttpSession session) {
		try {
			// get old contact detail
			Optional<Contact> optional = contactRepo.findById(c.getCid());
			Contact contact = optional.get();
			System.out.println(c.getCid());
			if (!file.isEmpty()) {
				// rewrite file
				File uFile = new ClassPathResource("/static/img").getFile();
				Path path = Paths.get(uFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

				// use Files class in java.nio.*package
				// Files.copy(file.inputStream, complete path,Option when file not exists)
				Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				c.setImageUrl(file.getOriginalFilename());
				// delete existing file
				File deleteFile=new ClassPathResource("static/img/").getFile();
				File file1=new File(deleteFile, contact.getImageUrl());
				file1.delete();
			} else {
				c.setImageUrl(contact.getImageUrl());
			}

			User user = repo.getUserByUsername(p.getName());
			c.setUser(user);
			contactRepo.save(c);
			session.setAttribute("message", new Message("Contact updated successfully!!", "success"));

		} catch (Exception e) {
			e.printStackTrace();
			session.setAttribute("message", new Message("Something went wrong!!", "danger"));
		}

		return "redirect:/user/contacts/"+c.getCid();
	}

	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int id, HttpSession session) {

		Optional<Contact> contactOptional = contactRepo.findById(id);
		Contact contact = contactOptional.get();
		contact.setUser(null);
		this.contactRepo.deleteById(id);
		session.setAttribute("message", new Message("Contact deleted successfully!!", "success"));

		return "redirect:/user/contacts";
	}
	@GetMapping("/account")
	public String settingTab(Model m) {
		m.addAttribute("title","SmartContactManager-Account");
		return "normal/setting";
	}
	
	//payment integration
	
	@PostMapping("/create_order")
	@ResponseBody
	public String paymentInitialise(@RequestBody Map<String, Object> data,Principal p) throws RazorpayException {
		
		System.out.println("payment amount: "+data);
		
		int amt= Integer.parseInt(data.get("payment").toString());
		RazorpayClient razorpayClient = new RazorpayClient("rzp_test_t6zL8kbFOseA5k", "sb3ikCqHiexYkQqN6nIOljkv");
		
		//create order request
		JSONObject orderReq=new JSONObject();
		orderReq.put("amount", amt*100);
		orderReq.put("currency", "INR");
		orderReq.put("receipt", "order_rcptid_1973");
		Order order = razorpayClient.orders.create(orderReq);
		
		PaymentOrder payOrder= new PaymentOrder();
		payOrder.setAmount(order.get("amount")+"");
		payOrder.setOrderId(order.get("id"));
		payOrder.setReceipt(order.get("receipt"));
		payOrder.setStatus("created");
		payOrder.setUser(repo.getUserByUsername(p.getName()));
		payOrder.setPaymentId(null);
		payRepo.save(payOrder);
	    return order.toString();
	}
	
	@PostMapping("/update_order")
	public ResponseEntity<?> updatePaymentOrderOnServer(@RequestBody Map<String , Object> data){
		
		PaymentOrder order = payRepo.findByOrderId(data.get("order_id").toString());
		
		order.setPaymentId(data.get("payment_id").toString());
		order.setStatus(data.get("status").toString());
		payRepo.save(order);
		System.out.println(data);
		return ResponseEntity.ok("done");
	}
}

















