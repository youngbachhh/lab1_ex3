/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import model.Book;
import model.Order;
import model.User;


public class OrderDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/htttptit";
    private String jdbcUsername = "root";
    private String jdbcPassword = "youngbach";

    private static final String CREATE_ORDER = "INSERT INTO orders" + "  (user_id, total, status) VALUES " + " (?, ?, ?);";
    private static final String GET_ORDER = "select quantity from order_books where order_id=? and book_id=?";
    private static final String ADD_ORDER = "INSERT INTO order_books" + "  (quantity, order_id, book_id) VALUES " + " (?, ?, ?);";
    private static final String CONFIRM_ORDER = "update orders set total=?,status=? where id=?;";
    private static final String UPDATE_ORDER = "update order_books set quantity=? where order_id=? and book_id=?";
    private static final String FIND_UNCONFIRM_ORDER_BY_USERID = "select id,total from orders where user_id = ? and status = ?";
    private static final String SELECT_ALL_BOOKSID_BY_ORDER_ID = "select book_id from order_books where order_id=?";
    private static final String DELETE_ORDER_BOOK_SQL = "delete from order_books where order_id = ? and book_id=?;";

    public OrderDao() {
    }

    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }

    public Order findUnconfirmOrderByUserID(User user){
        Order order = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_UNCONFIRM_ORDER_BY_USERID);) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.setString(2, "unconfirmed");
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                double total = rs.getDouble("total");
                order = new Order(id, user, total, "unconfirmed");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return order;
    }

    private int orderExisted(int order_id, int book_id){
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_ORDER);) {
            preparedStatement.setInt(1, order_id);
            preparedStatement.setInt(2, book_id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return rs.getInt("quantity");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return -1;
    }


    public void addChangeToOrder(Order order, Book book, int quantity) {
        System.out.println(ADD_ORDER);
        int sl = orderExisted(order.getId(), book.getId());
        String query = (sl==-1 ? ADD_ORDER : UPDATE_ORDER);
        int new_sl = (sl==-1 ? quantity : (quantity+sl<=book.getQuantity()? quantity+sl : book.getQuantity()));
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, new_sl);
            preparedStatement.setInt(2, order.getId());
            preparedStatement.setInt(3, book.getId());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void changeOrder(int orderID, int bookID, int quantity) {
        System.out.println(UPDATE_ORDER);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_ORDER)) {
            preparedStatement.setInt(1, quantity);
            preparedStatement.setInt(2, orderID);
            preparedStatement.setInt(3, bookID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void confirmOrder(Map<Book, Integer> cart, int orderID) {
        System.out.println(CONFIRM_ORDER);
        double total = 0;
        Iterator it = cart.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            total +=((Book) pair.getKey()).getPrice() * (Integer)pair.getValue();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CONFIRM_ORDER)) {
            preparedStatement.setDouble(1, total);
            preparedStatement.setString(2, "confirmed");
            preparedStatement.setInt(3, orderID);
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public void createOrder(Order order) {
        System.out.println(CREATE_ORDER);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_ORDER)) {
            preparedStatement.setInt(1, order.getUser().getId());
            preparedStatement.setDouble(2, order.getTotal());
            preparedStatement.setString(3, order.getStatus());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public boolean deleteOrderBook(int orderID, int bookID) throws SQLException {
        boolean rowDeleted;
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_ORDER_BOOK_SQL);) {
            statement.setInt(1, orderID);
            statement.setInt(2, bookID);
            System.out.println(statement);
            rowDeleted = statement.executeUpdate() > 0;
        }
        return rowDeleted;
    }

    private void printSQLException(SQLException ex) {
        for (Throwable e: ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }
}
