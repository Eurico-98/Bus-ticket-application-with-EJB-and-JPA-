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

@WebServlet("/CancelTripServlet")
public class CancelATrip extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(CancelATrip.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String destination = "/secured/resultPage.jsp";
        String myTripToCancel = request.getParameter("myTripToCancel");
        int authToken = (int) request.getSession().getAttribute("auth");
        String status = ms.cancelTrip(authToken, myTripToCancel);
        request.getSession(true).setAttribute("status", status);

        logger.info(" executed CancelTripServlet");
        
        //response.getWriter().println(status);
        // forward back to resultPage page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}