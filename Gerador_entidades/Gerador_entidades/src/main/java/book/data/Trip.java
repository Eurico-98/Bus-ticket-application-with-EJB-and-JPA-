package book.data;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
public class Trip implements Serializable{

    @Id
    private int tripID;
    @Temporal(TemporalType.TIMESTAMP)
    private Date departure;
    @Temporal(TemporalType.TIMESTAMP)
    private Date arrival;
    private String origin;
    private String destination;
    private int seats;
    private double price;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL)
    private List<Ticket> passengers;

    public Trip() {
    }

    public Trip(int tripID, Date departure, Date arrival, String origin, String destination, int seats, double price) {
        this.tripID = tripID;
        this.departure = departure;
        this.arrival = arrival;
        this.origin = origin;
        this.destination = destination;
        this.seats = seats;
        this.price = price;
    }

    public int getTripID() {
        return tripID;
    }

    public Date getDeparture() {
        return departure;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Ticket> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Ticket> passengers) {
        this.passengers = passengers;
    }
}
