package genealogy_tracker.genealogy_tracker;

public class CheckAccess {
    public Boolean checkTreeAccess(Integer treeID, Integer treeID2){
        if(treeID == treeID2){
            return true;
        } else {
            return false;
        }
    }
}
