<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Select date intervel for listing trips:</strong>
    <form action="SelectDatesForListingServlet" method="GET">

        <br/>

        <strong>First date: </strong><br/>
        <text>Year:</text>
        <select name="fYear">
            <c:forEach items="${dateslistYears}" var="dateYears">
                <option>${dateYears}</option>
            </c:forEach>
        </select>

        <br/>
        
        <text>Month: </text>
        <select name="fMonth">
            <c:forEach items="${dateslistMonths}" var="dateMonths">
                <option>${dateMonths}</option>
            </c:forEach>
        </select>
            
        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Second date: </strong><br/>
        <text>Year:</text>
        <select name="sYear">
            <c:forEach items="${dateslistYears}" var="dateYears">
                <option>${dateYears}</option>
            </c:forEach>
        </select>

        <br/>
        
        <text>Month: </text>
        <select name="sMonth">
            <c:forEach items="${dateslistMonths}" var="dateMonths">
                <option>${dateMonths}</option>
            </c:forEach>
        </select>
            
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