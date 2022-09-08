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
    <title>Cart</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <style>
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

        <!--            <ul class="navbar-nav">
                <li><a href="<%=request.getContextPath()%>/list" class="nav-link">Books</a></li>
            </ul>-->

        <div>
            <a href="logout" class="navbar-brand"> Log out </a>
        </div>
    </nav>
</header>
<br>

<div class="row">

    <div class="container">
        <h3 class="text-center">Cart</h3>
        <hr>
        <div class="container text-left">

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
                <th style="text-align: center">Actions</th>
            </tr>
            </thead>
            <tbody>
            <!--   for (Todo todo: todos) {  -->
            <c:forEach var="book" items="${cart}">

                <tr>
                    <td style="vertical-align: middle">
                        <c:out value="${book.key.id}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.key.name}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.key.author}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.key.price}" />
                    </td>
                    <td style="vertical-align: middle">
                        <c:out value="${book.key.quantity}" />
                    </td>
                    <td>
                        <div style="display: flex">
                            <form id="formAction${book.key.id}" action="" method="post">
                                <div class="value-button" id="decrease" onclick="decreaseValue(${book.key.id})" value="Decrease Value">-</div>
                                <input type="number" id="number${book.key.id}" name="quantity${book.key.id}" class="quantity" value="${book.value}" onkeypress="onChangeBtn(${book.key.id}, ${book.key.quantity})"/>
                                <div class="value-button" id="increase" onclick="increaseValue(${book.key.id}, ${book.key.quantity})" value="Increase Value">+</div>
                                <button type="submit" class="btn btn-danger" style="display: inline-block; margin-left: 5px" onclick="onClickRemove(${book.key.id})">Remove</button>
                                <button type="submit" id="btnShow${book.key.id}" class="btn btn-success" style="display: none; margin-left: 5px" onclick="onClickUpdate(${book.key.id}, ${book.key.quantity})">Save</button>
                            </form>
                        </div>
                    </td>
                </tr>
            </c:forEach>
            <!-- } -->
            </tbody>

        </table>

        <a href="<%=request.getContextPath()%>/cart/viewAll" class="btn btn-primary" style="margin-right: 60px">View all orders</a>
        <c:if test = "${fn:length(cart) > 0}">
            <form action="<%=request.getContextPath()%>/cart/confirm" method="post" style="float:right">
                <input type="hidden" name = "orderID" value="${orderID}"/>
                <button type="submit" class="btn btn-success" style="margin-right: 60px">Confirm Order</button>
            </form>
        </c:if>
    </div>
</div>
<script>
    function increaseValue(val, max_num) {
        document.getElementById('btnShow'+val).style.display="inline-block";
        var value = parseInt(document.getElementById('number'+val).value, 10);
        value = isNaN(value) ? 0 : value;
        value > max_num-1 ? value = max_num-1 : '';
        value++;
        document.getElementById('number'+val).value = value;
    }

    function onChangeBtn(val, max_num) {
        var value = parseInt(document.getElementById('number'+val).value, 10);
        value = isNaN(value) ? 1 : value;
        value < 1 ? value = 1 : '';
        value > max_num ? value = max_num : '';
        document.getElementById('number'+val).value = value;
        document.getElementById('btnShow'+val).style.display="inline-block";
    }

    function decreaseValue(val) {
        document.getElementById('btnShow'+val).style.display="inline-block";
        var value = parseInt(document.getElementById('number'+val).value, 10);
        value = isNaN(value) ? 1 : value;
        value < 2 ? value = 2 : '';
        value--;
        document.getElementById('number'+val).value = value;
    }
    function onClickUpdate(val, max_num){
//            var quantity = document.getElementById("number"+val).value;
        var value = parseInt(document.getElementById('number'+val).value, 10);
        value = isNaN(value) ? 1 : value;
        value < 1 ? value = 1 : '';
        value > max_num ? value = max_num : '';
        document.getElementById('number'+val).value = value;
        document.getElementById("formAction"+val).action = "<%=request.getContextPath()%>/cart/save?order_id=${orderID};book_id="+val+";quantity="+value;
    }

    function onClickRemove(val){
        document.getElementById("formAction"+val).action = "<%=request.getContextPath()%>/cart/remove?order_id=${orderID};book_id="+val;
    }
</script>
</body>

</html>

