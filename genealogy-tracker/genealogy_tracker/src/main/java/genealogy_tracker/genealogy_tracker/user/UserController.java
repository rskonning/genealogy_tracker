package genealogy_tracker.genealogy_tracker.user;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import genealogy_tracker.genealogy_tracker.family_tree.FamilyTree;
import genealogy_tracker.genealogy_tracker.family_tree.FamilyTreeRepository;
import genealogy_tracker.genealogy_tracker.person.PersonRepository;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class UserController {
    @Autowired(required = true)
	private UserRepository userRepository;
	@Autowired(required = true)
	private FamilyTreeRepository familyTreeRepository;
	@Autowired(required = true)
	private PersonRepository personRepository;

    @GetMapping("/register")
	public ModelAndView showRegistrationForm() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("signup_form");
    	modelAndView.addObject("user", new User());
		return modelAndView;
	}

	@PostMapping("/process_register")
	public ModelAndView processRegister(User user) {
    	BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    	String encodedPassword = passwordEncoder.encode(user.getPassword());
    	user.setPassword(encodedPassword);
		userRepository.save(user);
     
    	ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("register_success");
		return modelAndView;
	}

    
	// reset password form
	@GetMapping("/forgotPassword")
	public ModelAndView forgotPasswordForm(){
		ModelAndView m = new ModelAndView();
		m.setViewName("forgotPassword");
		m.addObject("user", new User());
		return m;
	}

	// send email to reset password
	@PostMapping("/send_email")
	public ModelAndView resetPassword(User u){
		// check that user has an account
		User user = userRepository.findByUsername(u.getUsername());
		if (user == null){
			ModelAndView m = new ModelAndView();
			m.addObject("user", u);
			m.setViewName("noUser");
			return m;
		}
		// create code that user has to enter to reset email
		RandomString r = new RandomString();
		String code = r.generateString(new Random(), 6);
		// create email message
		EmailServiceImpl email = new EmailServiceImpl();
		CreateJavaMailSender jms = new CreateJavaMailSender();
		email.setSender(jms.getJavaMailSender());
		email.sendSimpleMessage(u.getUsername(), "Family Tree Tracker - Reset Password", "\nHere is the code to reset your password: " 
			+ code);
		// create form for user to enter in code and new password
		ModelAndView m = new ModelAndView();
		PasswordReset pr = new PasswordReset(user, code, "");
		m.addObject("pass", pr);
		m.setViewName("resetPassword");
		return m;
	}

	// update user
	@PostMapping("/reset_password")
	public ModelAndView reset(PasswordReset pr){
		int success = 0;
		if(pr.getCode().equals(pr.getUser_code())){
			success = 1;
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    		String encodedPassword = passwordEncoder.encode(pr.getUser().getPassword());
    		pr.getUser().setPassword(encodedPassword);
			userRepository.save(pr.getUser());
		}
		ModelAndView m = new ModelAndView();
		m.setViewName("password");
		m.addObject("success", success);
		return m;
	}

	// get delete account form
	@GetMapping("/user/{id}")
	public ModelAndView delete(@PathVariable Integer id){
		ModelAndView m = new ModelAndView();
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		if (user.getUserID() == id){
			m.setViewName("deleteUser");
			m.addObject("id", id);
			return m;
		} else {
			m.setViewName("accessDenied");
			return m;
		}
	}

	// delete user
	@PostMapping("/user/{id}")
	public RedirectView deleteUser(@PathVariable Integer id){
		User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException(id));
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		personRepository.deleteAllPeopleFromTree(tree.getTreeID());
		//familyTreeRepository.deleteById(tree.getTreeID());
		userRepository.deleteById(id);
		RedirectView re = new RedirectView();
		re.setUrl("/");
		return re;
	}

}
