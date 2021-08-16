package genealogy_tracker.genealogy_tracker.family_tree;

import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Column;

@Entity
@Table(name = "family_tree")
public class FamilyTree {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false, length = 2)
    private Integer TreeID;
    @Column(nullable = false, length = 30)
    private String TreeName;
    @Column(nullable = false, length = 3)
    private Integer UserID;
    @Column(columnDefinition = "text")
    private String Description;
    @Column(nullable = true, length = 3)
    private Integer RootPersonID;

    public FamilyTree(){

    }

    public FamilyTree(Integer id, String name, Integer user, String desc, Integer root){
        this.TreeID = id;
        this.TreeName = name;
        this.UserID = user;
        this.Description = desc;
        this.RootPersonID = root;
    }

    public void setTreeID(Integer id){
        this.TreeID = id;
    }

    public Integer getTreeID(){
        return this.TreeID;
    }

    public void setTreeName(String name){
        this.TreeName = name;
    }

    public String getTreeName(){
        return this.TreeName;
    }

    public void setUserID(Integer user){
        this.UserID = user;
    }

    public Integer getUserID(){
        return this.UserID;
    }

    public void setDescription(String desc){
        this.Description = desc;
    }

    public String getDescription(){
        return this.Description;
    }

    public void setRootPersonID(Integer id){
        this.RootPersonID = id;
    }

    public Integer getRootPersonID(){
        return this.RootPersonID;
    }
}