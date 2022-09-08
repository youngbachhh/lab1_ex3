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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Book;


public class BookDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/htttptit";
    private String jdbcUsername = "root";
    private String jdbcPassword = "youngbach";

    //    private static final String INSERT_BOOK_SQL = "INSERT INTO books" + "  (name, author, price) VALUES " + " (?, ?, ?);";
    private static final String SELECT_BOOK_BY_ID = "select name,author,price,quantity from books where id =?";
    private static final String SELECT_ALL_BOOKS = "select * from books";
    private static final String SELECT_ALL_BOOKS_FROM_ORDER = "select book_id, quantity from order_books where order_id = ?";
    private static final String UPDATE_QUANTITY = "update books set quantity = ? where id = ?";
//    private static final String DELETE_BOOK_SQL = "delete from books where id = ?;";
//    private static final String UPDATE_BOOK_SQL = "update books set name = ?, author =? where id = ?;";

    public BookDao() {
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

    public Book selectBook(int id) {
        Book book = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_BOOK_BY_ID);) {
            preparedStatement.setInt(1, id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String name = rs.getString("name");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                book = new Book(id, name, author, price, quantity);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return book;
    }

    public List <Book> selectAllBooks() {
        List <Book> books = new ArrayList < > ();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS);) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                books.add(new Book(id, name, author, price, quantity));
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return books;
    }

    public List <Book> selectBooksByName(String searchingName) {
        System.out.println("searchingName: "+ searchingName);
        List <Book> books = new ArrayList < > ();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS);) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String author = rs.getString("author");
                double price = rs.getDouble("price");
                int quantity = rs.getInt("quantity");
                String name = rs.getString("name");
                if(name.toLowerCase().contains(searchingName.toLowerCase())){
                    books.add(new Book(id, name, author, price, quantity));
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return books;
    }


    public Map<Book, Integer> findBooksFromOrder(int order_id) {
        System.out.println(SELECT_ALL_BOOKS_FROM_ORDER);
        Map<Book, Integer> books = new HashMap<Book, Integer>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_BOOKS_FROM_ORDER);) {
            preparedStatement.setInt(1, order_id);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("book_id");
                int quantity = rs.getInt("quantity");
                books.put(selectBook(id), quantity);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return books;
    }


    public boolean updateBook(int bookID, int quantity) throws SQLException {
        boolean rowUpdated;
        System.out.println(UPDATE_QUANTITY);
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUANTITY);) {
            statement.setInt(1,quantity);
            statement.setInt(2,bookID);

            System.out.println(statement);
            rowUpdated = statement.executeUpdate() > 0;
        }
        return rowUpdated;
    }

    public void updateListBook(Map<Book, Integer> cart) throws SQLException{
        System.out.println("=============>>>>>>>> Check");
        Iterator it = cart.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            Book book = (Book) pair.getKey();
            System.out.println("TEST: "+book);
            boolean row = updateBook(book.getId(), book.getQuantity()-(Integer) pair.getValue());
            System.out.println(row);
            it.remove(); // avoids a ConcurrentModificationException
        }
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
