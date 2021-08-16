package genealogy_tracker.genealogy_tracker.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "family_user")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(nullable = false, length = 3)
    private Integer UserID;
    @Column(nullable = false, length = 12)
    private String Username;
    @Column(nullable = false, length = 64)
    private String Password = "password";

    public User(){

    }

    public User(Integer id, String username, String password){
        this.UserID = id;
        this.Username = username;
        this.Password = password;
    }

    public void setUserID(Integer id){
        this.UserID = id;
    }

    public Integer getUserID(){
        return this.UserID;
    }

    public void setUsername(String username){
        this.Username = username;
    }

    public String getUsername(){
        return this.Username;
    }

    public void setPassword(String password){
        this.Password = password;
    }

    public String getPassword(){
        return this.Password;
    }
 
}
