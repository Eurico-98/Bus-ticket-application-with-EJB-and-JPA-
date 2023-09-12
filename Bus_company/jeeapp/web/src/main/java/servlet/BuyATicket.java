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

@WebServlet("/BuyTicketServlet")
public class BuyATicket extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(BuyATicket.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String tripID = request.getParameter("tripId");
        String seatnumber = request.getParameter("seatnumber");
        String destination = "secured/resultPage.jsp";
        
        // get auth token which
        int authToken = (int) request.getSession().getAttribute("auth");
        String message = ms.buyTicket(authToken, tripID, seatnumber);
        request.getSession(true).setAttribute("status", message);
        
        logger.info(" executed BuyTicketServlet");

        //response.getWriter().println(message);
        // forward back to resultPage page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}