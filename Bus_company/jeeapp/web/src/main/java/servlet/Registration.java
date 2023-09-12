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
import beans.IManageSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/RegistServlet")
public class Registration extends HttpServlet{

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(Registration.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{

        String name = request.getParameter("name");
        String mail = request.getParameter("mail");
        String pass = request.getParameter("pass");
        String destination = "/registerror.html";
        String message = "ivalid registration";

        // if input parameters are not null, register user in data base and foward to login page
        if (name != "" && mail != "" && pass != "") {
            
            try {
                message = ms.registration(name, mail, pass);
                if(message.equals("valid registration")){
                    destination = "/authentication.html";
                }
            } 
            catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                logger.error("Error while trying to regiter a user", e);
            }
        }

        logger.info(" executed RegistServlet: ", message);

        //response.getWriter().println(message);
        // forward to authentication page or error page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}
