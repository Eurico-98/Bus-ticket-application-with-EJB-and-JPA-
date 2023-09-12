package beans;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

import javax.ejb.Local;

@Local
public interface IManageSystem {
    public String registration(String name, String mail, String password) throws InvalidKeySpecException, NoSuchAlgorithmException;
    public int authentication(String mail, String pass) throws InvalidKeySpecException, NoSuchAlgorithmException;
    public String editPersonalData(String name,String mail,String pass, int authtoken) throws NoSuchAlgorithmException, InvalidKeySpecException;
    public void deleteAccount(int authtoken);
    public List<List<String>> listAvailableTrips(String fDate, String sDate, int typeOfListing);
    public List<String> tripIds();
    public List<String> tripSeats(String tripID);
    public String chargeWallet(String amount, int authtoken);
    public String buyTicket(int authtoken, String tripID, String seatNumber);
    public List<String> listMyTickets(int authtoken, String resquestedOperation);
    public List<String> listMyTicketIDs(int authtoken);
    public String cancelTrip(int authToken, String myTripToCancel);

    // --------------------------------------------- MANAGER RELATED METHODS -------------------------------------
    public int checkManager(int authtoken);
    public List<String> listOfloations(String selectedOrigin);
    public List<List<String>> listOfPossibleDates(String selectedYear, String selectedMonth);
    public List<List<String>> listHoursMinutes();
    public boolean checkTicketPrice(String ticketPrice);
    public String createNewTrip(String Origin, String Destination, String departureYear, String departureDay, String departureMonth, String departureHour, String departureMinute, String arrivalHour, String arrivalMinute, String busCapacity, String ticketPrice);
    public List<String> deleteFutureTrip(String tripIDToDelete);
    public List<String> listUsers();
    public List<String> listOfPassangersOfGivenTrip(String selectedTripID);
    
}
