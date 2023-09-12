package servlet;
import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import beans.IManageSystem;

@WebServlet("/RedirectServlet")
public class RedirectToPage extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(RedirectToPage.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        String targetPage = "/secured/" + request.getParameter("name");

        // get trip data to buy a ticket
        if(targetPage.equals("/secured/selectTripToBuyTicket.jsp")){
            List<List<String>> tickteData = ms.listAvailableTrips("","", 1);
            List<String> tripIDs = ms.tripIds();

            if(tickteData.get(0).size() > 0){
                request.getSession(true).setAttribute("ticketData", tickteData.get(0));
                request.getSession(true).setAttribute("tripIDs", tripIDs);
            } 
            else {
                targetPage = "/secured/resultPage.jsp";
                request.getSession(true).setAttribute("status", "There are no trips available.");
            }
        }

        // list available trips
        else if(targetPage.equals("/secured/tripListing.jsp")){
        
            List<List<String>> availableTrips = ms.listAvailableTrips("","", 1);
            if(availableTrips.get(0).size() > 0){
                request.getSession(true).setAttribute("tripList", availableTrips.get(0));
            }
            else {
                targetPage = "/secured/resultPage.jsp";
                request.getSession(true).setAttribute("status", "There are no trips available.");
            }
        }

        // cancel a trip
        else if(targetPage.equals("/secured/selectTripToCancel.jsp")){
            int authToken = (int) request.getSession().getAttribute("auth");
            List<String> myTripsList = ms.listMyTickets(authToken, "Cancel Ticket");
            List<String> myTripIDs = ms.listMyTicketIDs(authToken);
            request.getSession(true).setAttribute("myTripList", myTripsList);
            request.getSession(true).setAttribute("myTripIDs", myTripIDs);
        }

        // list user trips 
        else if(targetPage.equals("/secured/userTripsListing.jsp")){
            int authToken = (int) request.getSession().getAttribute("auth");
            List<String> myTripsList = ms.listMyTickets(authToken, "List Trips");

            if(myTripsList.size() > 0){
                request.getSession(true).setAttribute("tripList", myTripsList);
            }
            else {
                targetPage = "/secured/resultPage.jsp";
                request.getSession(true).setAttribute("status", "You have no booked trips");
            }
        }

        // get trip information to create a new trip
        else if(targetPage.equals("/secured/selectTripData.jsp")){
            List<String> placesList = ms.listOfloations("");
            List<List<String>> possibleDates = ms.listOfPossibleDates("", "");
            List<List<String>> hoursMinutes = ms.listHoursMinutes();
            request.getSession(true).setAttribute("placesList", placesList);
            request.getSession(true).setAttribute("possibleYears", possibleDates.get(0));
            request.getSession(true).setAttribute("possibleMonths", possibleDates.get(1));
            request.getSession(true).setAttribute("hours", hoursMinutes.get(0));
            request.getSession(true).setAttribute("minutes", hoursMinutes.get(1));
        }

        // get trip list and ID list to delete trip
        else if(targetPage.equals("/secured/selectTripToDelete.jsp")){
            List<List<String>> availableTrips = ms.listAvailableTrips("","", 1);
            request.getSession(true).setAttribute("tripList", availableTrips.get(0));
            request.getSession(true).setAttribute("tripListIDs", availableTrips.get(1));
        }

        // list top 5 users
        else if(targetPage.equals("/secured/listTop5Users.jsp")){

            List<String> usersList = ms.listUsers();
            if(usersList.size() > 0){
                request.getSession(true).setAttribute("usersList", usersList);
            }
            else {
                request.getSession(true).setAttribute("usersList", "There no records of top 5 users with most trips.");
            }
        }

        // list trips between 2 dates
        else if(targetPage.equals("/secured/tripListingBetweenDates.jsp")){
            List<List<String>> dateslist = ms.listOfPossibleDates("", "");
            request.getSession(true).setAttribute("dateslistYears", dateslist.get(0));
            request.getSession(true).setAttribute("dateslistMonths", dateslist.get(1));
        }

        // list trips on a given date
        else if(targetPage.equals("/secured/tripListingOnGivenDate.jsp")){
            List<List<String>> dateslist = ms.listOfPossibleDates("", "");
            request.getSession(true).setAttribute("dateslistYears", dateslist.get(0));
            request.getSession(true).setAttribute("dateslistMonths", dateslist.get(1));
        }

        // list all passengers of a trip
        else if(targetPage.equals("/secured/printPassangerList")){
            targetPage = "/secured/tripListing.jsp";
            String selectedTripID = request.getParameter("selectedTripID");
            List<String> passangers = ms.listOfPassangersOfGivenTrip(selectedTripID);
            request.getSession(true).setAttribute("tripList", passangers);
        }

        logger.info(" executed RedirectServlet");

        // forward to value of targetPage
        request.getRequestDispatcher(targetPage).forward(request, response);
    }
}