package book.data;
import java.io.Serializable;
import javax.persistence.*;

@Entity
public class RegistUsers implements Serializable{
    
    // this class represents all registered users, it only needs the mail because its unique
    @Id
    private int userID;
    private String mail;

    public RegistUsers(){
    }

    public RegistUsers(int userID, String mail){
        this.userID = userID;
        this.mail = mail;
    }
    
    public void setUserID(int userID){
        this.userID = userID;
    }

    public int getUserID(){
        return userID;
    }

    public void setMail(String mail){
        this.mail = mail;
    }

    public String getMail(){
        return mail;
    }
}
