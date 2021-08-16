package genealogy_tracker.genealogy_tracker.user;

public class UserNotFoundException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = -6761626052928549881L;

    public UserNotFoundException(Integer id){
        super("Could not find user with id of " + id);
    }

    public UserNotFoundException(String username){
        super("Could not find user with username of " + username);
    }
}
