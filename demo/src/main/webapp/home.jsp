<%--
    Document   : user-list
    Created on : Aug 31, 2022, 2:36:42 PM
    Author     : Admin
--%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<html>
<head>
    <title>BuyBOOK</title>
    <link href="https://use.fontawesome.com/releases/v5.0.1/css/all.css" rel="stylesheet">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <style>
        .badge {
            font-size: 150%;
        }
        .badge:after{
            content:attr(value);
            font-size:14px;
            color: #fff;
            background: red;
            border-radius:50%;
            padding: 0 5px;
            position:relative;
            left:-8px;
            top:-10px;
            opacity:0.9;
        }
        form {
            display: flex;
            /*width: 300px;*/
            margin: 0 auto;
            text-align: center;
        }

        .value-button {
            display: inline-block;
            border: 1px solid #ddd;
            margin: 0px;
            width: 40px;
            height: 40px;
            text-align: center;
            vertical-align: middle;
            padding: 8px 0;
            background: #eee;
            -webkit-touch-callout: none;
            -webkit-user-select: none;
            -khtml-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        .value-button:hover {
            cursor: pointer;
        }
        .quantity {
            text-align: center;
            border: none;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
            margin: 0px;
            width: 40px;
            height: 40px;
        }

        form #input-wrap {
            margin: 0px;
            padding: 0px;
        }

        input#number {
            text-align: center;
            border: none;
            border-top: 1px solid #ddd;
            border-bottom: 1px solid #ddd;
            margin: 0px;
            width: 40px;
            height: 40px;
        }

        input[type=number]::-webkit-inner-spin-button,
        input[type=number]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }
        td {
            vertical-align: middle;
        }
    </style>
</head>
<body>
<header>
    <nav class="navbar navbar-expand-md navbar-dark" style="background-color: tomato; justify-content: space-between">
        <div>
            <a href="<%=request.getContextPath()%>/app/" class="navbar-brand"> Welcome, ${userLogined.name} </a>
        </div>

        <div>
            <a href="logout" class="navbar-brand"> Log out </a>
        </div>
    </nav>
</header>
<br>

<div class="row">

    <div class="container">
        <h3 class="text-center">List of Books</h3>
        <hr>
        <div style="display: flex; justify-content: space-between">

            <a href="<%=request.getContextPath()%>/cart/">
                <i class="fa badge fa-lg" value=${fn:length(cart)}>&#xf290;</i>
            </a>

            <form action="<%=request.getContextPath()%>/app/search" method="post">
                <input type="text" class="form-control" name="search" style="width: 500px" required="required">
                <button type="submit" class="btn btn-success" style="background-color: #2d9ae2">Search</button>
            </form>
        </div>
        <br>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Author</th>
                <th>Price</th>
                <th>Available quantity</th>
                <th style="text-align: center">Action</th>
            </tr>
            </thead>
            <tbody>
            <!--   for (Todo todo: todos) {  -->
            <c:forEach var="book" items="${listBooks}">

                <tr>
                    <td style="vertical-align: middle">
                        <c:out value="${book.id}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.name}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.author}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.price}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.quantity}" />
                    </td>
                    <td>
                        <div style="display: flex">
                            <form action="<%=request.getContextPath()%>/cart/add?id=${book.id}" method="post">
                                <div class="value-button" id="decrease" onclick="decreaseValue(${book.id})" value="Decrease Value">-</div>
                                <input type="number" id="number${book.id}" class="quantity" name="quantity${book.id}" value="1"/>
                                <div class="value-button" id="increase" onclick="increaseValue(${book.id}, ${book.quantity})" value="Increase Value">+</div>
                                <c:set var = "num" scope = "session" value = "${book.quantity}"/>
                                <c:if test = "${num > 0}">
                                    <button type="submit" class="btn btn-success" id="add${book.id}" style="display: inline-block; margin-left: 5px">Add</button>
                                </c:if>
                                <!--<button type="submit" class="btn btn-success" id="add${book.id}" style="display: inline-block; margin-left: 5px">Add</button>-->
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <!-- } -->
            </tbody>

        </table>
    </div>
</div>
<script>
    function increaseValue(val, max_num) {
        var value = parseInt(document.getElementById('number'+val).value, 10);
        value = isNaN(value) ? 0 : value;
        value > max_num-1 ? value = max_num-1 : '';
        value++;
        document.getElementById('number'+val).value = value;
    }

    function decreaseValue(val) {
        var value = parseInt(document.getElementById('number'+val).value, 10);
        value = isNaN(value) ? 1 : value;
        value < 2 ? value = 2 : '';
        value--;
        document.getElementById('number'+val).value = value;
    }
</script>
</body>

</html>

