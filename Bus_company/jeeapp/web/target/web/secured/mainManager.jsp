<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Bus Company</title>
</head>

<body>
    <strong>Bus company - terrible quality travels for a small fee!</strong>
    <br/><br/>
    <text>Select an option:</text>
    <br/><br/>
    <form action="RedirectServlet" method="GET">
        <button name="name" type="submit" value="editMyData.jsp">Edit personal data</button>
        <br/><br/>
        <button name="name" type="submit" value="deleteMyData.jsp">Delete my account</button>
        <br/><br/>
        <button name="name" type="submit" value="chargeWallet.jsp">Charge wallet</button>
        <br/><br/>
        <button name="name" type="submit" value="selectTripToBuyTicket.jsp">Buy ticket</button>
        <br/><br/>
        <button name="name" type="submit" value="tripListing.jsp">List available trips</button>
        <br/><br/>
        <button name="name" type="submit" value="userTripsListing.jsp">List my trips</button>
        <br/><br/>
        <button name="name" type="submit" value="selectTripToCancel.jsp">Cancel a trip</button>
        <br/><br/>
        <strong>--------------------------- Menager operations ---------------------------</strong>
        <br/><br/>
        <button name="name" type="submit" value="selectTripData.jsp">Create Trip</button>
        <br/><br/>
        <button name="name" type="submit" value="selectTripToDelete.jsp">Delete Trip</button>
        <br/><br/>
        <button name="name" type="submit" value="listTop5Users.jsp">Top 5 users with the most trips</button>
        <br/><br/>
        <button name="name" type="submit" value="tripListingBetweenDates.jsp">List trips between date interval</button>
        <br/><br/>
        <button name="name" type="submit" value="tripListingOnGivenDate.jsp">List trips occurring on a date</button>
        <br/><br/>
    </form>
    <form action="LogOutServlet" method="GET">
        <button type="submit">Log out</button>
    </form>
</body>

</html>