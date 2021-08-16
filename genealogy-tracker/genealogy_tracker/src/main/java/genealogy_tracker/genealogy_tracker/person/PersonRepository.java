package genealogy_tracker.genealogy_tracker.person;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface PersonRepository extends CrudRepository<Person, Integer>{
    @Query(value = "SELECT * FROM person WHERE person.treeid = ?1", nativeQuery = true)
    Iterable<Person> findPeopleFromTree(Integer treeID);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM person WHERE person.treeid = ?1", nativeQuery = true)
    void deleteAllPeopleFromTree(Integer treeID);
}
