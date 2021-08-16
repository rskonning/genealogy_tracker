package genealogy_tracker.genealogy_tracker.family_tree;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import genealogy_tracker.genealogy_tracker.CheckAccess;
import genealogy_tracker.genealogy_tracker.person.Person;
import genealogy_tracker.genealogy_tracker.person.PersonRepository;
import genealogy_tracker.genealogy_tracker.user.CustomUserDetails;
import genealogy_tracker.genealogy_tracker.user.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
public class FamilyTreeController {
    @Autowired(required = true)
	private FamilyTreeRepository familyTreeRepository;
	@Autowired(required = true)
	private PersonRepository personRepository;

    @GetMapping("/tree")
	public ModelAndView makeTree(){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("makeTree");
		FamilyTree tree = new FamilyTree();
		tree.setUserID(user.getUserID());
    	modelAndView.addObject("tree", tree);
		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@GetMapping("/tree/{id}/pedigree")
	public ModelAndView pedigree(@PathVariable Integer id){
		FamilyTree tree= familyTreeRepository.findById(id).orElseThrow(()-> new FamilyTreeNotFoundException(id));
		Iterable<Person> people = personRepository.findPeopleFromTree(tree.getTreeID());
		// check if user is allowed to access tree
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree2 = familyTreeRepository.getTree(user.getUserID());
		Boolean access = new CheckAccess().checkTreeAccess(id, tree2.getTreeID());
		ModelAndView modelAndView = new ModelAndView();
		if(access == true){
			modelAndView.setViewName("pedigree");
			modelAndView.addObject("tree", tree);
			modelAndView.addObject("people", people);
			return modelAndView;
		} else {
			modelAndView.setViewName("accessDenied");
			return modelAndView;
		}
	}

	// find specific tree
	@GetMapping("/tree/{id}")
	//public FamilyTree getTree(@PathVariable Integer id){
	//	return familyTreeRepository.findById(id).orElseThrow(()-> new FamilyTreeNotFoundException(id));
	public ModelAndView getTree(@PathVariable Integer id){
		FamilyTree tree= familyTreeRepository.findById(id).orElseThrow(()-> new FamilyTreeNotFoundException(id));
		// check if user is allowed to access tree
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree2 = familyTreeRepository.getTree(user.getUserID());
		Boolean access = new CheckAccess().checkTreeAccess(id, tree2.getTreeID());
		ModelAndView modelAndView = new ModelAndView();
		if(access == true){
			modelAndView.setViewName("tree_table");
			modelAndView.addObject("tree", tree);
			return modelAndView;
		} else {
			modelAndView.setViewName("accessDenied");
			return modelAndView;
		}
	}

	// create tree
	@PostMapping("/tree")
	public ModelAndView newFamilyTree(FamilyTree newTree){
		FamilyTree t = familyTreeRepository.save(newTree);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("createRootPerson");
		Person person = new Person();
		person.setTreeID(t.getTreeID());
		modelAndView.addObject("person", person);
		modelAndView.addObject("tree", t);
		return modelAndView;
	}

	// tree update form
	@GetMapping("/tree/{id}/update")
	public ModelAndView updateTreeForm(){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		Iterable<Person> people = personRepository.findPeopleFromTree(tree.getTreeID());
		ModelAndView model = new ModelAndView();
		model.setViewName("updateTree");
		model.addObject("tree", tree);
		model.addObject("people", people);
		return model;
	}


	// update tree
	@PostMapping("/tree/{id}")
	public RedirectView replaceTree(FamilyTree newTree, @PathVariable Integer id){
		familyTreeRepository.save(newTree);
		RedirectView re = new RedirectView();
		re.setUrl("/tree/" + id);
		return re;
	}

	// delete tree form
	@GetMapping("/tree/{id}/delete")
	public ModelAndView deleteTreeForm(){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		ModelAndView model = new ModelAndView();
		model.setViewName("deleteTree");
		model.addObject("tree", tree);
		return model;
	}

	// delete one specific tree (this should be difficult to do / prompt user multiple times if they want to delete)
	// delete all people on tree
	@PostMapping("/tree/{id}/deleteTrue")
	public RedirectView deleteTree(@PathVariable Integer id){
		personRepository.deleteAllPeopleFromTree(id);
		//familyTreeRepository.deleteById(id);
		RedirectView re = new RedirectView();
		re.setUrl("/logout");
		return re;
	}
}
