package genealogy_tracker.genealogy_tracker;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import genealogy_tracker.genealogy_tracker.family_tree.FamilyTreeRepository;
import genealogy_tracker.genealogy_tracker.family_tree.FamilyTree;
import genealogy_tracker.genealogy_tracker.user.CustomUserDetails;
import genealogy_tracker.genealogy_tracker.user.User;


@SpringBootApplication
@RestController
@Controller
public class GenealogyTrackerApplication {

	@Autowired(required = true)
	private FamilyTreeRepository familyTreeRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(GenealogyTrackerApplication.class, args);
	}


	@GetMapping("/")
	public ModelAndView displayMain(){
		ModelAndView m = new ModelAndView();
		m.setViewName("mainPage");
		return m;
	}

	@GetMapping("/login/")
	public RedirectView redirectHome2(){
		RedirectView re = new RedirectView();
		re.setUrl("/home");
		return re;
	}

	@GetMapping("/home")
	public RedirectView displayHome(){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		if(tree != null){
			// redirect to user's tree
			RedirectView re = new RedirectView();
			re.setUrl("/tree/" + tree.getTreeID());
			return re;
		} else {
			// redirect to create family tree
			RedirectView re = new RedirectView();
			re.setUrl("/tree");
			return re;
		}
	}

	@PostMapping("/home")
	public RedirectView redirectHome(){
		RedirectView re = new RedirectView();
		re.setUrl("/home");
		return re;
	}

	@GetMapping("/table")
	public RedirectView displayTable(){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		RedirectView re = new RedirectView();
		re.setUrl("/tree/" + tree.getTreeID() + "/people");
		return re;
	}

	@GetMapping("/account")
	public ModelAndView displayAccount(){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		ModelAndView m = new ModelAndView();
		m.setViewName("account");
		m.addObject("user", user);
		m.addObject("tree", tree);
		return m;
	}
	
}
