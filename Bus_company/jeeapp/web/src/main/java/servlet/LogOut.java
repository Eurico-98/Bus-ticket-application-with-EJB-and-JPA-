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

@WebServlet("/LogOutServlet")
public class LogOut extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(LogOut.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        String destination = "/authentication.html";
        request.getSession(true).removeAttribute("auth");
        request.getSession(true).removeAttribute("managerAuth");
        //request.getSession(false);
        destination = "/authentication.html";

        logger.info(" executed LogOutServlet");

        request.getRequestDispatcher(destination).forward(request, response);
    }
}