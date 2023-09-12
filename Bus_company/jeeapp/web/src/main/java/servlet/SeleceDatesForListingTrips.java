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

@WebServlet("/SelectDatesForListingServlet")
public class SeleceDatesForListingTrips extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(SeleceDatesForListingTrips.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String fYear = request.getParameter("fYear");
        String fMonth = request.getParameter("fMonth");
        String sYear = request.getParameter("sYear");
        String sMonth = request.getParameter("sMonth");
        String fDay = request.getParameter("fDay");
        String sDay = request.getParameter("sDay");
        String givenYear = request.getParameter("givenYear");
        String givenMonth = request.getParameter("givenMonth");
        String givenDay = request.getParameter("givenDay");
        String destination = "secured/resultPage.jsp";
        
        // to print list of trips between 2 dates
        if(fDay == null && sDay == null && givenYear == null && givenMonth == null && givenDay == null){

            if(Integer.parseInt(sYear) >= Integer.parseInt(fYear)){

                if(Integer.parseInt(sYear) == Integer.parseInt(fYear) && Integer.parseInt(sMonth) < Integer.parseInt(fMonth)){
                    request.getSession(true).setAttribute("status", "Invalid second month! Try again.");
                }
                else {
                    // get list of days in specific month selected
                    List<List<String>> flistOfDays = ms.listOfPossibleDates(fYear, fMonth);
                    List<List<String>> slistOfDays = ms.listOfPossibleDates(sYear, sMonth);
                    request.getSession(true).setAttribute("flistOfDays", flistOfDays.get(2));
                    request.getSession(true).setAttribute("slistOfDays", slistOfDays.get(2));
                    request.getSession(true).setAttribute("fYear", fYear);
                    request.getSession(true).setAttribute("fMonth", fMonth);
                    request.getSession(true).setAttribute("sYear", sYear);
                    request.getSession(true).setAttribute("sMonth", sMonth);
                    destination = "secured/selectDateDaysForListingTrips.jsp";
                }
            }
            else{
                request.getSession(true).setAttribute("status", "Invalid second year! Try again.");
            }
        }
        else if(fDay != null && sDay != null && givenYear == null && givenMonth == null && givenDay == null) {

            // check if days are valid
            if(Integer.parseInt(sYear) == Integer.parseInt(fYear) && Integer.parseInt(sMonth) == Integer.parseInt(fMonth) && Integer.parseInt(sDay) < Integer.parseInt(fDay)){
                request.getSession(true).setAttribute("status", "Invalid second day! Try again.");
            }
            else{

                String fDate = fYear + "-" + fDay + "-" + fMonth + " 00:00:00";
                String sDate = sYear + "-" + sDay + "-" + sMonth + " 00:00:00";
                List<List<String>> availableTrips = ms.listAvailableTrips(fDate, sDate, 2);
                if(availableTrips.get(0).size() > 0){
                    request.getSession(true).setAttribute("tripListing", availableTrips.get(0));
                    request.getSession(true).setAttribute("tripIDslist", availableTrips.get(1));
                    destination = "secured/listTripsUsingDates.jsp";
                }
                else {
                    request.getSession(true).setAttribute("status", "There are no trips available in between these dates.");
                }
            }
        }

        // to print list of trips on a given date
        if(givenYear != null && givenMonth != null && givenDay == null){

            // get list of days in specific month selected
            List<List<String>> listOfGivenDays = ms.listOfPossibleDates(givenYear, givenMonth);
            request.getSession(true).setAttribute("listOfGivenDays", listOfGivenDays.get(2));
            request.getSession(true).setAttribute("givenYear", givenYear);
            request.getSession(true).setAttribute("givenMonth", givenMonth);
            destination = "secured/selectGivenDayForListingTrips.jsp";
        }
        else if(givenDay != null) {

            String givenYearStart = givenYear + "-" + givenDay + "-" + givenMonth + " 00:00:00";
            String givenYearEnd = givenYear + "-" + givenDay + "-" + givenMonth + " 23:59:59";
            List<List<String>> availableTrips = ms.listAvailableTrips(givenYearStart, givenYearEnd, 2);
            if(availableTrips.get(0).size() > 0){
                request.getSession(true).setAttribute("tripListing", availableTrips.get(0));
                request.getSession(true).setAttribute("tripIDslist", availableTrips.get(1));
                destination = "secured/listTripsUsingDates.jsp";
            }
            else {
                request.getSession(true).setAttribute("status", "There are no trips available at this date.");
            }
        }

        logger.info(" executed SelectDatesForListingServlet");
        
        // forward back to jsp page that lets select the destination the arrival time and the day of departure
        request.getRequestDispatcher(destination).forward(request, response);
    }
}