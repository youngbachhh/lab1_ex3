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
            <form action="<%=request.getContextPath()%>/app/add" method="post">
                <caption>
                    <h2>
                        Register
                    </h2>
                </caption>

                <c:if test="${user != null}">
                    <input type="hidden" name="id" value="<c:out value='${user.id}' />" />
                </c:if>
                <fieldset class="form-group">
                    <label>Name</label> <input type="text" class="form-control" name="name" required="required">
                </fieldset>

                <fieldset class="form-group">
                    <label>Username</label> <input type="text" class="form-control" name="username" required="required" value="${not null ? account.username : ''}">
                </fieldset>

                <c:if test="${not empty  account.username}">
                    <p style="color: red; font-style: italic">This username is already in use. Please try another one</p>
                </c:if>

                <fieldset class="form-group">
                    <label>Password</label> <input type="password" class="form-control" name="password" required="required" value="${not null ? account.password : ''}">
                </fieldset>

                <fieldset class="form-group">
                    <label>Phone Number</label> <input type="text" class="form-control" name="phoneNum" required="required">
                </fieldset>

                <fieldset class="form-group">
                    <label>User address</label> <input type="text" class="form-control" name="address" required="required">
                </fieldset>


                <button type="submit" class="btn btn-success">Register</button>
            </form>
        </div>
    </div>
</div>
<script>
</script>
</body>

</html>

