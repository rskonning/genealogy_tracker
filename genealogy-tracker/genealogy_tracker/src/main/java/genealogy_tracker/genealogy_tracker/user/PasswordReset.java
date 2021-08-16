package genealogy_tracker.genealogy_tracker.user;

public class PasswordReset {
    private User user;
    private String code;
    private String user_code;


    PasswordReset(){

    }

    PasswordReset(User u, String code, String u_code){
        this.user = u;
        this.code = code;
        this.user_code = u_code;
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getCode(){
        return this.code;
    }

    public void setUser_code(String ucode){
        this.user_code = ucode;
    }

    public String getUser_code(){
        return this.user_code;
    }


}
