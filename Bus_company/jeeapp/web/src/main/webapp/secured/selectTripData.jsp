<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Input new Trip data:</strong>
    <form action="CreateTripServlet1" method="GET">

        <br/>

        <strong>Origin: </strong>
        <select name="Origin">
            <c:forEach items="${placesList}" var="locations">
                <option>${locations}</option>
            </c:forEach>
        </select>
        

        <br/>
        <br/>   
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Departure date: </strong><br/>
        <text>Year:</text>
        <select name="year">
            <c:forEach items="${possibleYears}" var="dateYears">
                <option>${dateYears}</option>
            </c:forEach>
        </select>

        <br/>
        
        <text>Month: </text>
        <select name="month">
            <c:forEach items="${possibleMonths}" var="dateMonths">
                <option>${dateMonths}</option>
            </c:forEach>
        </select>
            
        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Departure time: </strong><br/>
        
        <text>Hour:</text>
        <select name="departureHour">
            <c:forEach items="${hours}" var="timeHours">
                <option>${timeHours}</option>
            </c:forEach>
        </select>
        
        <br/>

        <text>Minutes:</text>
        <select name="departureMinute">
            <c:forEach items="${minutes}" var="timeMinutes">
                <option>${timeMinutes}</option>
            </c:forEach>
        </select>

        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Arrival time: </strong><br/>
        
        <text>Hour:</text>
        <select name="arrivalHour">
            <c:forEach items="${hours}" var="timeHours">
                <option>${timeHours}</option>
            </c:forEach>
        </select>
        
        <br/>

        <text>Minutes:</text>
        <select name="arrivalMinute">
            <c:forEach items="${minutes}" var="timeMinutes">
                <option>${timeMinutes}</option>
            </c:forEach>
        </select>

        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Bus capacity: </strong>
        <select name="busCapacity">
            <option>10</option>
            <option>20</option>
            <option>30</option>
            <option>40</option>
            <option>50</option>
        </select>

        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Ticket price: </strong><br/>
            <input name="ticketPrice" type="text" placeholder="Price..." />

        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

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