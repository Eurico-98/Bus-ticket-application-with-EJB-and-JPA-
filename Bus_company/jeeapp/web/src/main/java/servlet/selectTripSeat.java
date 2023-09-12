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

@WebServlet("/SelectSeatServlet")
public class selectTripSeat extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(selectTripSeat.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String destination = "/secured/selectSeat.jsp";
        String tripID = request.getParameter("tripID");
        List<String> seats = ms.tripSeats(tripID);
        request.getSession(true).setAttribute("seats", seats);
        request.getSession(true).setAttribute("tripID", tripID);

        logger.info(" executed SelectSeatServlet");
        
        // forward back to triplisting page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}