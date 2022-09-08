<%--
    Document   : user-form
    Created on : Aug 31, 2022, 2:39:51 PM
    Author     : Admin
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>

<head>
    <title>BuyBOOK</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>

<body>

<header>
    <nav class="navbar navbar-expand-md navbar-dark" style="background-color: tomato">
        <div>
            <a href="<%=request.getContextPath()%>/app/" class="navbar-brand"> BuyBOOK </a>
        </div>

        <!--                    <ul class="navbar-nav">
                        <li><a href="<%=request.getContextPath()%>/list" class="nav-link">Users</a></li>
                    </ul>-->
    </nav>
</header>
<br>
<div class="container col-md-5">
    <div class="card">
        <div class="card-body">
            <form action="<%=request.getContextPath()%>/app/check" method="post">
                <caption>
                    <h2>
                        Login
                    </h2>
                </caption>

                <fieldset class="form-group">
                    <label>User Name</label> <input type="text" class="form-control" name="name" required="required">
                </fieldset>

                <fieldset class="form-group">
                    <label>Password</label> <input type="password" class="form-control" name="password" required="required">
                </fieldset>

                <div style = "display: flex">
                    <span style="padding: 8px 0px">Don't have an account ?</span>
                    <a href="<%=request.getContextPath()%>/app/register" class="nav-link">Register</a>
                </div>


                <button type="submit" class="btn btn-success">Login</button>
            </form>
        </div>
    </div>
</div>
</body>

</html>

