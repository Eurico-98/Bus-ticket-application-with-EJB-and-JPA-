package beans;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import data.MyUser;
import data.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class SheduleMailSender{

    Logger logger = LoggerFactory.getLogger(SheduleMailSender.class);

    @PersistenceContext(unitName = "tables")
    EntityManager em;

    @Resource(mappedName = "java:jboss/mail/Default")
    private Session session;

    public SheduleMailSender() {}

    // send mails to managers every 24 hours hour = "*/24",
    @Schedule(second = "*", minute = "*", hour = "*/24")
    public void notifyManagers(){

        // get list of managers
        TypedQuery<MyUser> tqmu = em.createQuery("select u from MyUser u where manager = true", MyUser.class);
        List<MyUser> managers = tqmu.getResultList();

        // get list of tickets
        TypedQuery<Ticket> tqt = em.createQuery("select t from Ticket t", Ticket.class);
        List<Ticket> ticketsSold = tqt.getResultList();
        Double revenueToday = 0.0;

        // get star of current day
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat dayf = new SimpleDateFormat("yyyy-MM-dd");
        String todayDate = dayf.format(today); 

        for(Ticket t: ticketsSold){

            String purchase = t.getTimeOfPurchase();
            String[] arr = purchase.split(" ");

            if(todayDate.equals(arr[0])){
                revenueToday += t.getTrip().getPrice();
            }
        }

        // send mails
        for(MyUser m: managers){
            try{
                Message message = new MimeMessage(session);
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(m.getMail()));
                message.setSubject("Daily Revenue");
                message.setText("Daily Revenue: " + revenueToday);
                Transport.send(message);
            }
            catch (javax.mail.MessagingException e){
                logger.error(" Error while trying to send mail to managers", e);
            }
        }

        logger.info(" executed notifyManagers");
    }
}