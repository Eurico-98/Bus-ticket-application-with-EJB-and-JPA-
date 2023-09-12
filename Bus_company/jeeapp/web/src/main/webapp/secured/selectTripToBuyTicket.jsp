<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Available trips to book:</strong>
    <br/><br/>
    <c:forEach items="${ticketData}" var="item">
        ${item}<br>
    </c:forEach>
    <br/><br/>

    <strong>___________________________________________________</strong>
    <form action="SelectSeatServlet" method="GET">
        <text>Select desired Trip: </text>
        <select name="tripID">
            <c:forEach items="${tripIDs}" var="category">
                <option>${category}</option>
            </c:forEach>
        </select>
        <input type="submit">
    </form>
    <br/><br/>
    <form action="RedirectServlet" method="GET">
        <button name="name" type="submit" value=${mainPage}>Go back</button>
    </form>
    <br/><br/>
    <form action="LogOutServlet" method="GET">
        <button type="submit">Log out</button>
    </form>
</body>

</html>