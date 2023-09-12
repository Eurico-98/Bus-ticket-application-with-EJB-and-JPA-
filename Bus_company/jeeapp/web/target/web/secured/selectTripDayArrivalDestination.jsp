<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Select destination and day of arrival:</strong>
    <form action="CreateTripServlet2" method="GET">

        <br/>

        <strong>Destination: </strong>
        <select name="destination">
            <c:forEach items="${destinationsList}" var="locations">
                <option>${locations}</option>
            </c:forEach>
        </select>
        

        <br/>
        <br/>   
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Departure day: </strong><br/>
        <select name="departureDay">
            <c:forEach items="${listOfDays}" var="dateDays">
                <option>${dateDays}</option>
            </c:forEach>
        </select>
            
        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <input type="hidden" name="Origin" value="${Origin}">
        <input type="hidden" name="year" value="${year}">
        <input type="hidden" name="month" value="${month}">
        <input type="hidden" name="departureHour" value="${departureHour}">
        <input type="hidden" name="departureMinute" value="${departureMinute}">
        <input type="hidden" name="arrivalHour" value="${arrivalHour}">
        <input type="hidden" name="arrivalMinute" value="${arrivalMinute}">
        <input type="hidden" name="busCapacity" value="${busCapacity}">
        <input type="hidden" name="ticketPrice" value="${ticketPrice}">

        <input type="submit">
    </form>
    <br/>
    <form action="RedirectServlet" method="GET">
        <button name="name" type="submit" value=${mainPage}>Go back</button>
    </form>
    <br/><br/>
    <form action="LogOutServlet" method="GET">
        <button type="submit">Log out</button>
    </form>
</body>

</html>