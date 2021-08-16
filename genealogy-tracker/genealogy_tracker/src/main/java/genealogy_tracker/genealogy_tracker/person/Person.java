package genealogy_tracker.genealogy_tracker.person;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "person")
public class Person {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false, length = 3)
    private Integer PersonID;
    @Column(nullable = true, length = 25)
    private String LastName;
    @Column(nullable = true, length = 25)
    private String FirstName;
    @Column(nullable = false, length = 1)
    private char Gender = 'U';
    @Column(nullable = true, length = 10)
    private String DOBirth;
    @Column(nullable = true, length = 50)
    private String POBirth;
    @Column(nullable = true, length = 10)
    private String DODeath;
    @Column(nullable = true, length = 50)
    private String PODeath;
    @Column(nullable = false, length = 2)
    private Integer TreeID;
    @Column(nullable = true, length = 3)
    private Integer FatherID;
    @Column(nullable = true, length = 3)
    private Integer MotherID;
    @Column(nullable = true, length = 3)
    private Integer SpouseID;
    @Column(name="genOrder", nullable = true, length = 11)
    private String genOrder;
    @Column(columnDefinition = "text")
    private String Notes;

    public Person(){

    }

    public Person(Integer id, String ln, String fn, char g, String dob, String pob, 
    String dod, String pod, Integer treeid, Integer fid, Integer mid, Integer sid, String gen, String notes){
        this.PersonID = id;
        this.LastName = ln;
        this.FirstName = fn;
        this.Gender = g;
        this.DOBirth = dob;
        this.POBirth = pob;
        this.DODeath = dod;
        this.PODeath = pod;
        this.TreeID = treeid;
        this.FatherID = fid;
        this.MotherID = mid;
        this.SpouseID = sid;
        this.genOrder = gen;
        this.Notes = notes;
    }

    public void setPersonID(Integer id){
        this.PersonID = id;
    }

    public Integer getPersonID(){
        return this.PersonID;
    }

    public void setLastName(String name){
        this.LastName = name;
    }

    public String getLastName(){
        return this.LastName;
    }

    public void setFirstName(String name){
        this.FirstName = name;
    }

    public String getFirstName(){
        return this.FirstName;
    }

    public void setGender(char g){
        this.Gender = g;
    }

    public char getGender(){
        return this.Gender;
    }

    public void setDOBirth(String dob){
        this.DOBirth = dob;
    }

    public String getDOBirth(){
        return this.DOBirth;
    }

    public void setPOBirth(String pob){
        this.POBirth = pob;
    }

    public String getPOBirth(){
        return this.POBirth;
    }

    public void setDODeath(String dod){
        this.DODeath = dod;
    }

    public String getDODeath(){
        return this.DODeath;
    }

    public void setPODeath(String pod){
        this.PODeath = pod;
    }

    public String getPODeath(){
        return this.PODeath;
    }

    public void setTreeID(Integer id){
        this.TreeID = id;
    }

    public Integer getTreeID(){
        return this.TreeID;
    }

    public void setFatherID(Integer id){
        this.FatherID = id;
    }

    public Integer getFatherID(){
        return this.FatherID;
    }

    public void setMotherID(Integer id){
        this.MotherID = id;
    }

    public Integer getMotherID(){
        return this.MotherID;
    }

    public void setSpouseID(Integer id){
        this.SpouseID = id;
    }

    public Integer getSpouseID(){
        return this.SpouseID;
    }

    public void setGenOrder(String order){
        this.genOrder = order;
    }

    public String getGenOrder(){
        return this.genOrder;
    }

    public void setNotes(String notes){
        this.Notes = notes;
    }

    public String getNotes(){
        return this.Notes;
    }
}
