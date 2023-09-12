<!DOCTYPE html>
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>

<body>
    <strong>Unauthorized access, this account is not a manger account!</strong>
    <br/>
    <form action="RedirectServlet" method="GET">
        <button name="name" type="submit" value="main.jsp">Go back</button>
    </form>
    <br/><br/>
    <form action="LogOutServlet" method="GET">
        <button type="submit">Log out</button>
    </form>
</body>

</html>