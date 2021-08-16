 package genealogy_tracker.genealogy_tracker.family_tree;

public class FamilyTreeNotFoundException extends RuntimeException{
    /**
     *
     */
    private static final long serialVersionUID = 5161510920486588104L;

    public FamilyTreeNotFoundException(Integer id){
        super("Could not find tree with id of " + id);
    }
}
