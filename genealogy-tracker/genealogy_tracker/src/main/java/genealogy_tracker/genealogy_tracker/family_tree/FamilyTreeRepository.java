package genealogy_tracker.genealogy_tracker.family_tree;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface FamilyTreeRepository extends CrudRepository<FamilyTree, Integer> {
    // gets tree for a specific user id
    @Query(value = "SELECT * FROM family_tree where family_tree.userid = ?1", nativeQuery = true)
    FamilyTree getTree(Integer id);
}