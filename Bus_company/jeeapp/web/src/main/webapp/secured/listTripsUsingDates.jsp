<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>List of trips:</strong>
    <br/><br/>
    <c:forEach items="${tripListing}" var="item">
        ${item}<br>
    </c:forEach>
    <br/><br/>
    <form action="RedirectServlet" method="GET">
        <text>See list of passengers of trip: </text>
        <select name="selectedTripID">
            <c:forEach items="${tripIDslist}" var="category">
                <option>${category}</option>
            </c:forEach>
        </select>

        <br/><br/>

        <input type="hidden" name="name" value="printPassangerList">

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