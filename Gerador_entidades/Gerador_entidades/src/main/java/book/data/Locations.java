package book.data;
import java.io.Serializable;
import javax.persistence.*;

@Entity
public class Locations implements Serializable{
    
    // this class represents locations
    @Id
    private int locationID;
    private String location;

    public Locations(){
    }

    public Locations(int locationID, String location){
        this.locationID = locationID;
        this.location = location;
    }
    
    public void setLocationID(int locationID){
        this.locationID = locationID;
    }

    public int getLocationID(){
        return locationID;
    }

    public void setLocation(String location){
        this.location = location;
    }

    public String getLocation(){
        return location;
    }
}
