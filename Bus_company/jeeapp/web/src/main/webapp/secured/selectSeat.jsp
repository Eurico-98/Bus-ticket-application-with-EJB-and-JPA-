<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Selected trip: </strong> ${tripID}
    <br/><br/>
    <form action="BuyTicketServlet" method="POST">
        <input type="hidden" name="tripId" value="${tripID}">
        <br/>  
        <text>Select desired seat: </text>
        <select name="seatnumber">
            <c:forEach items="${seats}" var="category">
                <option>${category}</option>
            </c:forEach>
        </select>
        <input type="submit">
    </form>
    <br/><br/>
    <form action="RedirectServlet" method="GET">
        <button name="name" type="submit" value="selectTripToBuyTicket.jsp">Go back</button>
    </form>
    <br/><br/>
    <form action="LogOutServlet" method="GET">
        <button type="submit">Log out</button>
    </form>
</body>

</html>