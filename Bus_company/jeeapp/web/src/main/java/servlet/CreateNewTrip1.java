package servlet;
import java.io.IOException;
import java.util.Calendar;
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

@WebServlet("/CreateTripServlet1")
public class CreateNewTrip1 extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(CreateNewTrip1.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String Origin = request.getParameter("Origin");
        //-----------------------------------------------------------------
        String year = request.getParameter("year");
        String month = request.getParameter("month");
        //-----------------------------------------------------------------
        String departureHour = request.getParameter("departureHour");
        String departureMinute = request.getParameter("departureMinute");
        //-----------------------------------------------------------------
        String arrivalHour = request.getParameter("arrivalHour");
        String arrivalMinute = request.getParameter("arrivalMinute");
        //-----------------------------------------------------------------
        String busCapacity = request.getParameter("busCapacity");
        //-----------------------------------------------------------------
        String ticketPrice = request.getParameter("ticketPrice");
        //-----------------------------------------------------------------
        String destination = "secured/resultPage.jsp";
        request.getSession(true).setAttribute("status", "Invalid month or invalid ticket price, try again.");
        
        int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        boolean validMonth = true;
        
        if(Integer.parseInt(year) == currentYear && Integer.parseInt(month) < currentMonth){
            validMonth = false;
        }

        // check if ticket price is a valid double
        if(ms.checkTicketPrice(ticketPrice) && validMonth){
            
            // get list of possible destinations
            List<String> destinationsList = ms.listOfloations(Origin);
            // get list of days in specific month selected
            List<List<String>> listOfDays = ms.listOfPossibleDates(year, month);

            // pass values to next jsp page
            request.getSession(true).setAttribute("Origin", Origin);
            request.getSession(true).setAttribute("destinationsList", destinationsList);
            //------------------------------------------------------------------------------
            request.getSession(true).setAttribute("listOfDays", listOfDays.get(2));
            //------------------------------------------------------------------------------
            request.getSession(true).setAttribute("year", year);
            request.getSession(true).setAttribute("month", month);
            //------------------------------------------------------------------------------
            request.getSession(true).setAttribute("departureHour", departureHour);
            request.getSession(true).setAttribute("departureMinute", departureMinute);
            //------------------------------------------------------------------------------
            request.getSession(true).setAttribute("arrivalHour", arrivalHour);
            request.getSession(true).setAttribute("arrivalMinute", arrivalMinute);
            //------------------------------------------------------------------------------
            request.getSession(true).setAttribute("busCapacity", busCapacity);
            //------------------------------------------------------------------------------
            request.getSession(true).setAttribute("ticketPrice", ticketPrice);

            destination = "secured/selectTripDayArrivalDestination.jsp";
        }

        logger.info(" executed CreateTripServlet1");

        //response.getWriter().println(message);
        // forward back to jsp page that lets select the destination the arrival time and the day of departure
        request.getRequestDispatcher(destination).forward(request, response);
    }
}