<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Input your new data <br/> Input only in desired fields:</strong>
    <form action="EditDataServlet" method="POST">
        <input name="name" type="text" placeholder="username..." />
        <input name="mail" type="text" placeholder="mail..." />
        <input name="pass" type="text" placeholder="password..." />
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