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

@WebServlet("/AuthServlet")
public class Authentication extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(Authentication.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        
        String mail = request.getParameter("mail");
        String pass = request.getParameter("pass");
        String destination = "/autherror.html";
        int message = -1;

        // if the credetials are correct give auth token and forward to main.jsp page
        if (mail != null && pass != null) {

            try {
                // check if the inserted mail is registered and if the credentials are correct
                message = ms.authentication(mail, pass);

                if(message != -1){
                    request.getSession(true).setAttribute("auth", message);
                    // if user is not a manager
                    destination = "/secured/main.jsp"; 
                    request.getSession(true).setAttribute("mainPage", "main.jsp");

                    // check if it is a manager
                    int authToken = (int) request.getSession().getAttribute("auth");
                    int managerToken = ms.checkManager(authToken);
                    if(managerToken != -1){
                        
                        request.getSession(true).setAttribute("managerAuth", managerToken);

                        // if it is a manager
                        destination = "/secured/mainManager.jsp";
                        request.getSession(true).setAttribute("mainPage", "mainManager.jsp");
                    }
                }
                else{
                    request.getSession(true).removeAttribute("auth");
                }
            } 
            catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.error(" Error while trying to authenticate", e);
            }
        }

        logger.info(" executed AuthServlet");

        //response.getWriter().println((int) request.getSession().getAttribute("managerAuth"));
        // forward to main.jsp or error page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}