package servlet;
import java.io.IOException;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import beans.IManageSystem;

@WebServlet("/CreateTripServlet2")
public class CreateNewTrip2 extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(CreateNewTrip2.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        // get all the parameters
        String Origin = request.getParameter("Origin");
        String Destination = request.getParameter("destination");
        //-----------------------------------------------------------------
        String departureYear = request.getParameter("year");
        String departureMonth = request.getParameter("month");
        String departureDay = request.getParameter("departureDay");
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
        String destinationPage = "secured/resultPage.jsp";

        String message = ms.createNewTrip(Origin, Destination, departureYear, departureMonth, departureDay, departureHour, departureMinute, arrivalHour, arrivalMinute, busCapacity, ticketPrice);
        request.getSession(true).setAttribute("status", message);

        logger.info(" executed CreateTripServlet2");
        
        //response.getWriter().println(message);
        // forward to result page
        request.getRequestDispatcher(destinationPage).forward(request, response);
    }
}