package servlet;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import beans.IManageSystem;

@WebServlet("/EditDataServlet")
public class EditData extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(EditData.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String name = request.getParameter("name");
        String mail = request.getParameter("mail");
        String pass = request.getParameter("pass");
        String destination = "secured/resultPage.jsp";
        String message = "";
        
        // get auth token which
        int authToken = (int) request.getSession().getAttribute("auth");

        try {
            message = ms.editPersonalData(name, mail, pass, authToken);
            request.getSession(true).setAttribute("status", message);
        } 
        catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            logger.error("Error while trying edit user data!", e);
        }

        logger.info(" executed EditDataServlet");

        // forward back to resultPage page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}