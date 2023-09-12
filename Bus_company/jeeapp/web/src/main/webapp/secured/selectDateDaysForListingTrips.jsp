<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Select date days:</strong>
    <form action="SelectDatesForListingServlet" method="GET"><br/>

        <strong>First date day: </strong><br/>
        <select name="fDay">
            <c:forEach items="${flistOfDays}" var="dateDays">
                <option>${dateDays}</option>
            </c:forEach>
        </select>
            
        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <strong>Second date day: </strong><br/>
        <select name="sDay">
            <c:forEach items="${slistOfDays}" var="dateDays">
                <option>${dateDays}</option>
            </c:forEach>
        </select>

        <br/>
        <br/>
        <strong>-----------------------------------------</strong>
        <br/>
        <br/>

        <input type="hidden" name="fYear" value="${fYear}">
        <input type="hidden" name="fMonth" value="${fMonth}">
        <input type="hidden" name="sYear" value="${sYear}">
        <input type="hidden" name="sMonth" value="${sMonth}">

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