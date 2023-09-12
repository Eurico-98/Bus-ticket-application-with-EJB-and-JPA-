package beans;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import data.Locations;
import data.MyUser;
import data.RegistUsers;
import data.Ticket;
import data.Trip;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import javax.annotation.Resource;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.math.BigInteger;
import javax.mail.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
public class ManageSystem implements IManageSystem {

    @PersistenceContext(unitName = "tables")
    EntityManager em;

    @Resource(mappedName = "java:jboss/mail/Default")
    private Session session;

    Logger logger = LoggerFactory.getLogger(ManageSystem.class);

    public ManageSystem() {
    }

    // ------------------------------------------------- password encryption methods --------------------------------------------------
    private static String generateStrongPasswordHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException{
    
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64*8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    private static String toHex(byte[] array) throws NoSuchAlgorithmException {
        
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    private static boolean validatePassword(String originalPassword, String storedPassword)throws NoSuchAlgorithmException, InvalidKeySpecException {
        
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }
    // ------------------------------------------------- password encryption methods --------------------------------------------------

    // add new user to database
    public String registration(String name, String mail, String password) throws InvalidKeySpecException, NoSuchAlgorithmException {
        
        MyUser u = new MyUser();
        RegistUsers ru = new RegistUsers();
        String message = "repeated mail"; // to prevent from regitering the same mail more than once
        
        // encrypt password
        String hashedPass = "";
        try {

            hashedPass = generateStrongPasswordHash(password);
            // get next unique ID for new user
            TypedQuery<RegistUsers> tq = em.createQuery("select ru from RegistUsers ru", RegistUsers.class);
            List<RegistUsers> ruList = tq.getResultList();

            // if it's the first entry in the table
            if (ruList.size() == 0) {
               
                u = new MyUser(1, name, mail, hashedPass, 0.0, false);
                ru = new RegistUsers(1, mail);
                em.persist(u);
                em.persist(ru);
                message = "valid registration";
            } 
            else {

                // check if mail is not repeated
                TypedQuery<MyUser> tqmu = em.createQuery("select u from MyUser u where u.mail = ?1", MyUser.class).setParameter(1, mail);
                List<MyUser> mu = tqmu.getResultList();
                
                if (mu.size() == 0) {

                    Query q = em.createQuery("select max(userID) from MyUser");
                    int userID = (int) q.getResultList().get(0) + 1;
                    u = new MyUser(userID, name, mail, hashedPass, 0.0, false);
                    ru = new RegistUsers(userID, mail);
                    em.persist(u);
                    em.persist(ru);
                    message = "valid registration";
                }
            }

        } catch (Exception e) {
            logger.error("Error while trying to register new account", e);
        }

        logger.info(" executed registration");

        return message;
    }

    // check if user is registered and cradentials are valid
    public int authentication(String mail, String pass) throws NoSuchAlgorithmException, InvalidKeySpecException {

        int message = -1;

        // check if user is registered
        Query q = em.createQuery("select userID from RegistUsers where mail = ?1").setParameter(1, mail);
        
        if(q.getResultList().size() == 1){

            logger.info("   '-> found registered users");

            // get password
            MyUser mu = em.find(MyUser.class, q.getResultList().get(0));
            String storedPassword = mu.getPassword();

            // check if it exists in data base
            if(validatePassword(pass, storedPassword) /*pass.equals(storedPassword)*/){
                message = mu.getUserID();
            }
        }
        else{
            logger.info("   did not find registered users");
        }
        
        logger.info(" executed authentication");

        return message;
    }

    // edit personal data
    public String editPersonalData(String newName,String newMail, String newPpass, int authtoken) throws NoSuchAlgorithmException, InvalidKeySpecException{

        // maybe use this to show the updated values
        String message = "Data updated -> ";

        // get users objet from data base using auth token which is the userID
        
        MyUser mu = em.find(MyUser.class, authtoken);
        RegistUsers ru = em.find(RegistUsers.class, authtoken);

        // check which parameters have values
        if(newName.length() != 0){
            mu.setName(newName);
            message = message + "name: " + mu.getName();
        }
        if(newPpass.length() != 0){

            // encrypt new password
            newPpass = generateStrongPasswordHash(newPpass);
            mu.setPassword(newPpass);
            message = message + " - password ";
        }
        if(newMail.length() != 0){
            mu.setMail(newMail);
            ru.setMail(newMail);
            message = message + "- mail: " + mu.getMail();
        }

        logger.info(" executed editPersonalData");

        return message;
    }

    // delete account
    public void deleteAccount(int authtoken) {

        // get objects associated with this user
        MyUser mu = em.find(MyUser.class, authtoken);
        em.remove(mu);
        RegistUsers ru = em.find(RegistUsers.class, authtoken);
        em.remove(ru);

        logger.info(" executed deleteAccount");
    }

    // list all avilable trips
    public List<List<String>> listAvailableTrips(String fDate, String sDate, int typeOfListing){

        List<List<String>> listsOfTrips = new ArrayList<>();

        List<String> tripData = new ArrayList<>();
        List<String> listOfTripIDs = new ArrayList<>();

        // get list of trips from data base
        TypedQuery<Trip> tq = em.createQuery("select t from Trip t", Trip.class);
        List<Trip> tripList = tq.getResultList(); 

        // to format fDate and sDate into timestamps
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss");
        Date dateParser;
        Date fTimestamp = new Date(0);
        Date sTimestamp = new Date(0);

        // do this only for methods of listing trips between 2 dares and on a given date
        if(typeOfListing == 2){

            try {
                dateParser = sdf.parse(fDate);
                fTimestamp = new Date(dateParser.getTime() - 10000); // give a 10 second buffer to include dates ont the same day at time 00:00:00 and 23:59:59;
                dateParser = sdf.parse(sDate);
                sTimestamp = new Date(dateParser.getTime() + 10000);
            } catch (ParseException e) {

                logger.error("Error while trying to parsedate from string to milliseconds", e);
            }
        }
        
        // show only future trips
        Date date = new Date(System.currentTimeMillis() + 43200000); // current time plus 12 hours
        String temp = "";
        for(Trip t: tripList){
        
            // statement 1 - chec if it is in the future and if there are seats available in case of listing available trips
            // statement 2 - check if trips are within dates in case od list trips between 2 dates for listing trips between 2 dates and on a given date
            if(
                (typeOfListing == 1 && t.getDeparture().after(date) && t.getPassengers().size() < t.getSeats()) 
                || 
                (typeOfListing == 2 && t.getDeparture().after(fTimestamp) && t.getDeparture().before(sTimestamp))
            ){
                temp = "Trip number: " + t.getTripID(); 
                tripData.add(temp);
                listOfTripIDs.add( Integer.toString(t.getTripID()));
                temp = "Origin: " + t.getOrigin();
                tripData.add(temp);
                temp = "Destination: " + t.getDestination();
                tripData.add(temp);
                String[] temp2 = t.getDeparture() .toString().split(" ");
                temp = "Departure  at: " + temp2[0];
                tripData.add(temp);
                temp = "Departure Time: " + temp2[1];
                tripData.add(temp);
                String[] arrivalTime = t.getArrival().toString().split(" ");
                temp = "Arrival at: " + arrivalTime[1];
                tripData.add(temp);
                temp = "Price: " + t.getPrice();
                tripData.add(temp);
                temp = "---------------------------------------------";
                tripData.add(temp);
            }
        }
        listsOfTrips.add(tripData);
        listsOfTrips.add(listOfTripIDs);

        logger.info(" executed listAvailableTrips");

        return listsOfTrips;
    }

    // get trip ids
    public List<String> tripIds(){

        List<String> tripIDs = new ArrayList<>();
        TypedQuery<Trip> tq = em.createQuery("select t from Trip t", Trip.class);
        List<Trip> tripList = tq.getResultList(); 
        Date date = new Date(System.currentTimeMillis() + 86400000); // current time plus 24 hours
        String temp = "";
        for(Trip t: tripList){
            if(t.getDeparture().after(date) && t.getPassengers().size() < t.getSeats()){
                temp = Integer.toString(t.getTripID());
                tripIDs.add(temp);    
            }
        }

        logger.info(" executed tripIds");

        return tripIDs;
    }

    // get trip seats
    public List<String> tripSeats(String tripID){

        // get total seat available
        Trip t = em.find(Trip.class, Integer.parseInt(tripID));
        int availableSeats = t.getSeats();

        // get free seats
        List<String> freeSeats = new ArrayList<>(); 

        // fill array
        for(int i = 1; i <= availableSeats; i++){
            freeSeats.add(Integer.toString(i));
        }

        // get list of passengers
        List<Ticket> passangers = t.getPassengers();

        // if trip has passangers remove from free seats list the ones ocupied
        if(passangers.size() > 0){

            for(Ticket p: passangers){
                freeSeats.remove(Integer.toString(p.getSeatNumber()));
            }
        }

        logger.info(" executed tripSeats");

        return freeSeats;
    }

    // charge wallet
    public String chargeWallet(String amount, int authtoken) {

        String message = "Funds added successfully - ";
        try{
            Double d = Double.parseDouble(amount);
            // get user object
            MyUser mu = em.find(MyUser.class, authtoken);
            double currentValue = mu.getWallet();
            mu.setWallet(d+currentValue);
            message += " Total funds in your wallet: " + mu.getWallet();
        } catch (NumberFormatException e) {
            message = "Invalid input!";
        }

        logger.info(" executed chargeWallet");
        
        return message;
    }

    // buy a ticket
    public String buyTicket(int authtoken, String tripID, String seatNumber){

        String message = "Purchase completed successfully!";
        
        int tripIDi = Integer.parseInt(tripID);
        Trip t = em.find(Trip.class, tripIDi);

        // check if user has enough funds
        MyUser mu = em.find(MyUser.class, authtoken);
        if(mu.getWallet() >= t.getPrice()){
        
            Ticket ticket = new Ticket();
            int seatNum = Integer.parseInt(seatNumber);

            // get next unique ID for new user
            Query q = em.createQuery("select count(*) from Ticket");
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String strDate = sdf.format(date);   

            if (q.getResultList().get(0).toString().equals("0")) {
                ticket = new Ticket(1, t, mu, seatNum, strDate); 
                // persist the new ticket in the database
                em.persist(ticket);
            }
            else {
                q = em.createQuery("select max(ticketID) from Ticket");
                int ticketID = (int) q.getResultList().get(0) + 1;
                ticket = new Ticket(ticketID, t, mu, seatNum, strDate);
                em.persist(ticket);
            }

            // subtract the price
            Double funds = mu.getWallet();
            funds -= t.getPrice();
            mu.setWallet(funds);
            
        }
        else{
            message = "Insuficient funds, charge your wallet!";
        }

        logger.info(" executed buyTicket");

        return message;
    }

    // get user trip list
    public List<String> listMyTickets(int authtoken, String resquestedOperation){

        List<String> myTicketsList = new ArrayList<>();

        // get user object from database
        MyUser mu = em.find(MyUser.class, authtoken);
        List<Ticket> myTickets = mu.getMyTrickets();
        Date date = new Date(System.currentTimeMillis() + 43200000); // current time plus 24 hours
        for(Ticket t: myTickets){

            // show only future tickets
            if(t.getTrip().getDeparture().after(date)){

                myTicketsList.add("             Trip: " + t.getTrip().getTripID());
                myTicketsList.add("Origin: " + t.getTrip().getOrigin());
                myTicketsList.add("Destinations: " + t.getTrip().getDestination());
                String[] temp2 = t.getTrip().getDeparture().toString().split(" ");
                myTicketsList.add("Departure  at: " + temp2[0]);
                myTicketsList.add("Departure Time: " + temp2[1]);String[] arrivalTime = t.getTrip().getArrival().toString().split(" ");
                myTicketsList.add("Arrival at: " + arrivalTime[1]);
                myTicketsList.add("Price: " + Double.toString(t.getTrip().getPrice()));
                myTicketsList.add("Selecte seat: " + Integer.toString(t.getSeatNumber()));
                if(resquestedOperation.equals("Cancel Ticket")){
                    myTicketsList.add("Your ticket ID for this trip: ");
                    myTicketsList.add(Integer.toString(t.getTicketID()));
                }
                myTicketsList.add("--------------------------------------------"); 
            }
        }

        logger.info(" executed listMyTickets");

        return myTicketsList;
    }

    // get ticket IDs fo selection
    public List<String> listMyTicketIDs(int authtoken){

        List<String> myTripIDList = new ArrayList<>();

        // get user object from database
        MyUser mu = em.find(MyUser.class, authtoken);
        List<Ticket> myTickets = mu.getMyTrickets();
        Date date = new Date(System.currentTimeMillis() + 43200000); // current time plus 24 hours
        for(Ticket t: myTickets){

            // show only future tickets
            if(t.getTrip().getDeparture().after(date)){
                myTripIDList.add(Integer.toString(t.getTicketID()));
            }
        }

        logger.info(" executed listMyTicketIDs");

        return myTripIDList;
    }

    // cancel a trip
    public String cancelTrip(int authToken, String myTripToCancel){

        String message = "Error while canceling your trip, try again!";

        // get user object from data base
        MyUser mu = em.find(MyUser.class, authToken);
        List<Ticket> myTicket = mu.getMyTrickets();
        int ticketID = 0;

        for(Ticket t: myTicket){

            if(t.getTicketID() == Integer.parseInt(myTripToCancel)){
                ticketID = t.getTicketID();  
                message = "Trip canceled successfully and funds added to your wallet - wallet status: ";
                
                // refund users wallet
                double userFunds = mu.getWallet();
                userFunds += t.getTrip().getPrice();
                mu.setWallet(userFunds);
                message += Double.toString(userFunds);

                // dessociate ticket from user and trip
                mu.getMyTrickets().remove(t);


                // delete entity from data base
                Ticket ticket = em.find(Ticket.class, ticketID);
                em.remove(ticket);

                break;
            }
        }

        logger.info(" executed cancelTrip");

        return message;
    }

    //
    //
    //
    //
    // ----------------------------------------------------------- MANAGER RELATED METHODS -------------------------------------
    public int checkManager(int authtoken){

        int managerID = -1;
        MyUser manager = em.find(MyUser.class, authtoken);
        if(manager.isManager()){
            return authtoken;
        }

        logger.info(" executed checkManager");

        return managerID;
    }

    // get list of places available to travel
    public List<String> listOfloations(String selectedOrigin){

        List<String> places = new ArrayList<>();
        TypedQuery<Locations> tq = em.createQuery("select l from Locations l", Locations.class);
        List<Locations> llist = tq.getResultList();
        for(Locations l: llist){
            if(!l.getLocation().equals(selectedOrigin)){
                places.add(l.getLocation());
            }
        }

        logger.info(" executed listOfloations");

        return places;
    }

    // get list of possible dates
    public List<List<String>> listOfPossibleDates(String selectedYear, String selectedMonth){

        List<List<String>> dates = new ArrayList<>();

        // allows to create trips up to 2 years in the future
        List<String> years = new ArrayList<>();
        int year = Calendar.getInstance().get(Calendar.YEAR);
        for(int i = year; i <= year+2; i++){
            years.add(Integer.toString(i));
        }
        dates.add(years);

        // get months
        List<String> months = new ArrayList<>();
        for(int i = 1; i <= 12; i++){
            months.add(Integer.toString(i));
        }
        dates.add(months);

        // get list of possible days depending on the year and month selected
        List<String> days = new ArrayList<>();
        if(selectedMonth.length() > 0 && selectedMonth.length() > 0){
            
            YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(selectedYear), Integer.parseInt(selectedMonth));
            int daysInMonth = yearMonthObject.lengthOfMonth();
            
            // get current day
            int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            // give a tolerance of 1 day
            for(int i = 1; i <= daysInMonth; i++){

                if(Integer.parseInt(selectedMonth) == currentMonth && i >= day+1){
                    days.add(Integer.toString(i));
                } 
                else {
                    days.add(Integer.toString(i));
                }
            }
            dates.add(days);
        }

        logger.info(" executed listOfPossibleDates");

        return dates;
    }

    // get list of houres and minutes
    public List<List<String>> listHoursMinutes(){

        List<List<String>> houresMinutes = new ArrayList<>();

        // get hours
        List<String> hours = new ArrayList<>();
        for(int i = 0; i <= 23; i++){
            if(i <= 9){
                hours.add("0" + Integer.toString(i));
            }
            else{
                hours.add(Integer.toString(i));
            }
        }
        houresMinutes.add(hours);

        // get minutes
        List<String> minutes = new ArrayList<>();
        for(int i = 0; i <= 59; i++){
            if(i <= 9){
                minutes.add("0" + Integer.toString(i));
            }
            else{
                minutes.add(Integer.toString(i));
            }
        }
        houresMinutes.add(minutes);

        logger.info(" executed listHoursMinutes");

        return houresMinutes;
    }

    // check if ticket price is valid
    public boolean checkTicketPrice(String ticketPrice){

        boolean result = true;
        try{
            Double d = Double.parseDouble(ticketPrice);
            if(d < 0){
                result = false;
            }
        } catch (NullPointerException | NumberFormatException e) {
            result = false;
        }

        logger.info(" executed checkTicketPrice");

        return result;
    }

    // create a new trip
    public String createNewTrip(String Origin, String Destination, String departureYear, String departureDay, String departureMonth, String departureHour, String departureMinute, String arrivalHour, String arrivalMinute, String busCapacity, String ticketPrice){

        String message = "Trip created successfully!";

        // format dates acording to data base format
        String completeDepartureDate = departureYear + "-" + departureMonth + "-" + departureDay + " " + departureHour + ":" + departureMinute + ":00";
        String completeArrivalDate = arrivalHour + ":" + arrivalMinute + ":00";
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-dd-MM hh:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("hh:mm:ss");
        Date date;
        Date depTimestamp = new Date(0);
        Date ArrTimestamp = new Date(0);
        try {
            date = sdf.parse(completeDepartureDate);
            depTimestamp = new Date(date.getTime());
            date = sdf2.parse(completeArrivalDate);
            ArrTimestamp = new Date(date.getTime());
        } catch (ParseException e) {
            message = "Error while parsing dates!";
        }
        
        // create only if departure date is in the future
        Date today = new Date(System.currentTimeMillis() + 86400000); // can only create trips for next day to prevent last minute trip the no one will be able to buy
        if(depTimestamp.after(today)){

            // get tripID from data base
            Query q = em.createQuery("select max(tripID) from Trip");
            if(q.getResultList().get(0) == null){
                Trip newTrip = new Trip(1, depTimestamp, ArrTimestamp, Origin, Destination, Integer.parseInt(busCapacity), Double.parseDouble(ticketPrice));
                em.persist(newTrip);
            }
            else{
                int tripID = (int) q.getResultList().get(0) + 1;
                Trip newTrip = new Trip(tripID, depTimestamp, ArrTimestamp, Origin, Destination, Integer.parseInt(busCapacity), Double.parseDouble(ticketPrice));
                em.persist(newTrip);
            }
        }
        else {
            message = "Invalid departure date!";
        }

        logger.info(" executed createNewTrip");

        return message;
    }

    // delete a trip
    public List<String> deleteFutureTrip(String tripIDToDelete){

        List<String> userMails = new ArrayList<>();

        // get trip entity from data base
        Trip trip = em.find(Trip.class, Integer.parseInt(tripIDToDelete));

        // get list of passengers
        List<Ticket> passangers = trip.getPassengers();
        Double refund = trip.getPrice();
        
        // add departur date to usersMail to notify user
        userMails.add(trip.getDeparture().toString());

        // refund passangers
        for(Ticket t: passangers){

            int userID =  t.getUser().getUserID();
            if(!userMails.contains(t.getUser().getMail())){
                userMails.add(t.getUser().getMail());
            }
            MyUser mu = em.find(MyUser.class, userID);
            Double userWallet = mu.getWallet();
            userWallet += refund;
            mu.setWallet(userWallet);
        }

        logger.info(" executed deleteFutureTrip");

        em.remove(trip);
        return userMails;
    }

    // list top 5 users
    public int partition(ArrayList <ArrayList <Integer>> paths, int begin, int end) {

        int pivot = paths.get(end).get(1);

        int i = begin - 1;

        for (int j = begin; j < end; j++) {
            if (paths.get(j).get(1) > pivot) {
                i++;

                ArrayList <Integer> temp;

                temp = paths.get(i);
                paths.set(i, paths.get(j));
                paths.set(j, temp);
            }
        }


        ArrayList <Integer> temp = paths.get(i + 1);
        paths.set(i + 1, paths.get(end));
        paths.set(end, temp);

        logger.info(" executed partition");

        return i + 1;
    }

    public void quickSort(ArrayList <ArrayList <Integer>> paths, int begin, int end) {
        if (end <= begin)
            return;

        int pivot = partition(paths, begin, end);

        quickSort(paths, begin, pivot - 1);
        quickSort(paths, pivot + 1, end);

        logger.info(" executed quickSort");
    }

    public List<String> listUsers(){

        List<String> userList = new ArrayList<>();

        int ctrl;
        ArrayList <ArrayList <Integer>> userDicInt = new ArrayList<ArrayList <Integer>>();

        // get list of users from data base
        TypedQuery<MyUser> tq = em.createQuery("select u from MyUser u", MyUser.class);
        TypedQuery<Trip> tqt = em.createQuery("select t from Trip t", Trip.class);
        List<MyUser> myUsers = tq.getResultList();
        List<Trip> allTrips = tqt.getResultList();

        // only do this if there'er users in the data base
        if(allTrips.size() > 0){

            //Check all trips
            for(Trip trip: allTrips){  
                
                List<Integer> userControl = new ArrayList<>();  //list to control users that bought more than one ticket for the trip
                
                //Check all tickets of one trip
                for (Ticket bilhete : trip.getPassengers()){

                    int userId = bilhete.getUser().getUserID(); 

                    //If the control list doesn't have the name from the ticket means that is the first time that user is counted
                    if(!userControl.contains(userId)){
                        userControl.add(userId);

                        if(userDicInt.isEmpty()){
                            ArrayList <Integer> auxArrayList = new ArrayList<Integer>();
                            auxArrayList.add(userId);
                            auxArrayList.add(1);
                            userDicInt.add(auxArrayList);
                        }
                        else{
                            ctrl = 1;
                            for(int i = 0; i< userDicInt.size(); i++){
                                if(userDicInt.get(i).get(0) == userId){
                                    int k = userDicInt.get(i).get(1);
                                    userDicInt.get(i).set(1, k+1);
                                    ctrl = 0;
                                    
                                }
                            }
                            if(ctrl==1){
                                ArrayList <Integer> auxArrayList = new ArrayList<Integer>();
                                auxArrayList.add(userId);
                                auxArrayList.add(1);
                                userDicInt.add(auxArrayList);
                            }
                        }
                    }   
                }
                userControl.clear();
            }

            quickSort(userDicInt, 0, userDicInt.size()-1);


            // get top 5
            for(int i = 0; i < 5 && i < userDicInt.size(); i++){
                int userId = userDicInt.get(i).get(0);

                for(MyUser u : myUsers){
                    if(u.getUserID() == userId){
                        String temp = "Name: " + u.getName();
                        userList.add(temp);
                        temp = "Total trips ->  "  + userDicInt.get(i).get(1);
                        userList.add(temp);
                        userList.add("______________________");
                    }
                }
            }
        }
        else{
            String temp = "No results";
            userList.add(temp);
            userList.add("______________________");
        }

        logger.info(" executed listUsers");

        return userList;
    }
    //----------------------------------------

    // list users on a given trip
    public List<String> listOfPassangersOfGivenTrip(String selectedTripID){

        List<String> usersList = new ArrayList<>();

        // get trip entity from data base
        Trip selectedTrip = em.find(Trip.class, Integer.parseInt(selectedTripID));
        List<Ticket> ticketList = selectedTrip.getPassengers();

        // if there are tickets
        if(ticketList.size() > 0 ){

            usersList.add("Users: ");

            // get user names from the ticke list
            for(Ticket t: ticketList){
                if(!usersList.contains("-> " + t.getUser().getName())){
                    usersList.add("-> " + t.getUser().getName());
                }
            }
        }
        else{
            usersList.add("This trip has no passangers.");
        }

        logger.info(" executed listOfPassangersOfGivenTrip");

        return usersList;
    }

}