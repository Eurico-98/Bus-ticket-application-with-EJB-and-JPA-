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

@WebServlet("/ChargeWalletServlet")
public class WalletCharge extends HttpServlet {

    @EJB
    private IManageSystem ms;

    Logger logger = LoggerFactory.getLogger(WalletCharge.class);

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
        String amount = (String) request.getParameter("amount");
        String destination = "secured/resultPage.jsp";
        
        // get auth token which
        int authToken = (int) request.getSession().getAttribute("auth");
        String message = ms.chargeWallet(amount, authToken);
        request.getSession(true).setAttribute("status", message);

        logger.info(" executed ChargeWalletServlet");
        
        // forward back to resultPage page
        request.getRequestDispatcher(destination).forward(request, response);
    }
}