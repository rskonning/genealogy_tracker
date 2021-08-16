package genealogy_tracker.genealogy_tracker.person;

public class PersonNotFoundException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = -8910907151603711777L;

    public PersonNotFoundException(Integer id){
        super("Could not find the person with id of " + id);
    }
}
