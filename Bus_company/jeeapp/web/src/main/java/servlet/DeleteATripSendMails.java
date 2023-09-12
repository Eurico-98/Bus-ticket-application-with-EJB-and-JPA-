package servlet;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import beans.IManageSystem;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/DeleteTripServlet")
public class DeleteATripSendMails extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(DeleteATripSendMails.class);

    @Resource(mappedName = "java:jboss/mail/Default")
    private Session session;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String destination = "/secured/resultPage.jsp";
        String tripIDToDelete = request.getParameter("tripIDToDelete");

        // send mail to affeted user
        List<String> userMails = ms.deleteFutureTrip(tripIDToDelete);
        for(int i = 1; i < userMails.size(); i++){
            
            try{
                Message message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userMails.get(i)));
                message.setSubject("Trip canceled");
                message.setText("We are sorry to say your trip with departure date at " + userMails.get(0) + " was canceled, the ticket price was added to your wallet!");
                Transport.send(message);
            }
            catch (javax.mail.MessagingException e){
                logger.error("Error while trying to send mail do user!", e);
            }
        }

        logger.info(" executed DeleteTripServlet");

        request.getSession(true).setAttribute("status", "Trip deleted successfully and all passangers notifed and refunded!");
        // forward back to resultPage page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}