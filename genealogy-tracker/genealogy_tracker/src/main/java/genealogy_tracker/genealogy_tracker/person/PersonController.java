package genealogy_tracker.genealogy_tracker.person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import genealogy_tracker.genealogy_tracker.CheckAccess;
import genealogy_tracker.genealogy_tracker.family_tree.FamilyTree;
import genealogy_tracker.genealogy_tracker.family_tree.FamilyTreeNotFoundException;
import genealogy_tracker.genealogy_tracker.family_tree.FamilyTreeRepository;
import genealogy_tracker.genealogy_tracker.user.CustomUserDetails;
import genealogy_tracker.genealogy_tracker.user.User;

@RestController
public class PersonController {
    @Autowired(required = true)
	private PersonRepository personRepository;
    @Autowired(required = true)
	private FamilyTreeRepository familyTreeRepository;

    // create root person and redirect to tree
	@PostMapping("/root")
	public RedirectView createRoot(Person person){
		Person p = personRepository.save(person);
		FamilyTree t = familyTreeRepository.findById(person.getTreeID()).orElseThrow(()-> 
			new FamilyTreeNotFoundException(person.getTreeID()));
		t.setRootPersonID(p.getPersonID());
		familyTreeRepository.save(t);
		RedirectView re = new RedirectView();
		re.setUrl("/tree/" + p.getTreeID());
		return re;
	}

    @GetMapping("/person")
	public ModelAndView createPerson(){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		Iterable<Person> people = personRepository.findPeopleFromTree(tree.getTreeID());
		Person person = new Person();
		person.setTreeID(tree.getTreeID());
		ModelAndView m = new ModelAndView();
		m.setViewName("createPerson");
		m.addObject("person", person);
		m.addObject("people", people);
		return m;
	}

	// create person
	@PostMapping("/person")
	public RedirectView createPerson(Person person){
		personRepository.save(person);
		RedirectView re = new RedirectView();
		re.setUrl("/tree/" + person.getTreeID());
		return re;
	}
	

	// update person
	@PostMapping("/person/{id}")
	public RedirectView replacePerson(Person person, @PathVariable Integer id){
		personRepository.save(person);
		RedirectView re = new RedirectView();
		re.setUrl("/person/" + id);
		return re;
	}

	// delete prompt - ask the user if they are sure they want to delete
	@PostMapping("/person/{id}/delete")
	public ModelAndView doDeletePerson(@PathVariable Integer id){
		Person person = personRepository.findById(id).orElseThrow(()-> new PersonNotFoundException(id));
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		Boolean access = new CheckAccess().checkTreeAccess(person.getTreeID(), tree.getTreeID());
		ModelAndView m = new ModelAndView();
		if (access == true){
			m.setViewName("deletePerson");
			m.addObject("person", person);
			m.addObject("id", id);
			return m;
		} else {
			m.setViewName("accessDenied");
			return m;
		}
		
	}

	// delete person
	@PostMapping("/person/{id}/deleteTrue")
	public RedirectView deletePerson(@PathVariable Integer id){
		Person person = personRepository.findById(id).orElseThrow(()-> new PersonNotFoundException(id));
		personRepository.delete(person);
		RedirectView re = new RedirectView();
		re.setUrl("/home");
		return re;
	}


	// look at all persons of a specific tree (user can only look at people in their tree)
	@GetMapping("/tree/{id}/people")
	public ModelAndView getPeoplefromTree(@PathVariable Integer id){
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		Boolean access = new CheckAccess().checkTreeAccess(id, tree.getTreeID());
		Iterable<Person> people = personRepository.findPeopleFromTree(id);
		ModelAndView modelAndView = new ModelAndView();
		if (access == true){
			modelAndView.setViewName("people");
    		modelAndView.addObject("people", people);
			return modelAndView;
		} else {
			modelAndView.setViewName("accessDenied");
			return modelAndView;
		}
	}

	// look at specific person
	@GetMapping("/person/{id}")
	public ModelAndView getPerson(@PathVariable Integer id){
		Person person = personRepository.findById(id).orElseThrow(()-> new PersonNotFoundException(id));
		CustomUserDetails u = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User user = u.getUser();
		FamilyTree tree = familyTreeRepository.getTree(user.getUserID());
		Iterable<Person> people = personRepository.findPeopleFromTree(tree.getTreeID());
		// check for null values so no errors happen in template parsing
		Boolean[] values = new Boolean[3];
		values[0] = (person.getFatherID() == null);
		values[1] = (person.getMotherID() == null);
		values[2] = (person.getSpouseID() == null);
		ModelAndView modelAndView = new ModelAndView();
		Boolean access = new CheckAccess().checkTreeAccess(person.getTreeID(), tree.getTreeID());
		if (access == true){
			modelAndView.setViewName("person");
			modelAndView.addObject("person", person);
			modelAndView.addObject("people", people);
			modelAndView.addObject("values", values);
			return modelAndView;
		} else {
			modelAndView.setViewName("accessDenied");
			return modelAndView;
		}
		
	}
}
