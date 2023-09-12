<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Operation status:</strong>
    <br/><br/>
    <text> 
        ${status}
    </text>
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