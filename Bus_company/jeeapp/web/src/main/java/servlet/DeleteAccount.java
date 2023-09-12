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

@WebServlet("/DeleteDataServlet")
public class DeleteAccount extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(DeleteAccount.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String destination = request.getParameter("name");
        //String message = "error";

        // get auth token
        int authToken = (int) request.getSession().getAttribute("auth");
        ms.deleteAccount(authToken);
        
        // remove auth tokens
        request.getSession(true).removeAttribute("auth");
        try{
            request.getSession(true).removeAttribute("managerAuth");
        } 
        catch (IllegalStateException e){
            logger.error("Error while trying to authToken from manager account.", e);
        }

        logger.info(" executed DeleteDataServlet");
        

        //response.getWriter().println(message);
        // forward back to main page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}