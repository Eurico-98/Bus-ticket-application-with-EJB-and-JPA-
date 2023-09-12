package book.data;
import java.io.Serializable;
import java.util.List;
import javax.persistence.*;

@Entity
public class MyUser implements Serializable {
    
    // users are identified by a unique userID
    @Id
    private int userID;
    private String mail;
    private String name;
    private String password;
    private double wallet;
    private boolean manager;

    // each user can have several trips booked in the future
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Ticket> myTickets;

    public MyUser() {
    }

    public MyUser(int userID, String name, String mail, String password, double wallet, boolean manager) {
        this.userID = userID;
        this.name = name;
        this.mail = mail;
        this.password = password;
        this.wallet = wallet;
        this.manager = manager;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public boolean isManager() {
        return manager;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    public List<Ticket> getMyTrickets() {
        return myTickets;
    }

    public void setMyTrickets(List<Ticket> myTickets) {
        this.myTickets = myTickets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    
}
