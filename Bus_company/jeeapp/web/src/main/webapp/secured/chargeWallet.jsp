<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>

<body>
    <strong>Input amount to charge wallet:</strong>
    <form action="ChargeWalletServlet" method="POST">
        <input name="amount" type="text" placeholder="amount..." />
        <input type="submit">
    </form>
    <br/>
    <form action="RedirectServlet" method="GET">
        <button name="name" type="submit" value=${mainPage}>Go back</button>
    </form>
    <br/><br/>
    <form action="LogOutServlet" method="GET">
        <button name="name" type="submit" value="logout">Log out</button>
    </form>
</body>

</html>