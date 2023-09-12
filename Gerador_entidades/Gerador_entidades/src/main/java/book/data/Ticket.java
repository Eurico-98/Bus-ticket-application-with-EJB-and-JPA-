package book.data;
import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Ticket implements Serializable{

    @Id
    private int ticketID;
    @ManyToOne
    private Trip trip;
    @ManyToOne
    private MyUser user;
    private int seatNumber;
    private String timeOfPurchase;

    public Ticket(){
    }

    public Ticket(int ticketID, Trip trip, MyUser user, int seatNumber, String timeOfPurchase) {
        this.ticketID = ticketID;
        this.trip = trip;
        this.user = user;
        this.seatNumber = seatNumber;
        this.timeOfPurchase = timeOfPurchase;
    }

    public String getTimeOfPurchase(){
        return timeOfPurchase;
    }

    public void setTimeOfPurchase(String timeOfPurchase){
        this.timeOfPurchase = timeOfPurchase;
    }

    public int getTicketID(){
        return ticketID;
    }

    public void setTicketID(int ticketID){
        this.ticketID = ticketID;
    }

    public Trip getTrip(){
        return trip;
    }

    public void setTrip(Trip trip){
        this.trip = trip;
    }

    public MyUser getUser(){
        return user;
    }

    public void setUser(MyUser user){
        this.user = user;
    }

    public int getSeatNumber(){
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber){
        this.seatNumber = seatNumber;
    }

}