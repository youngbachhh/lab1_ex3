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
import model.Account;


public class AccountDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/htttptit";
    private String jdbcUsername = "root";
    private String jdbcPassword = "youngbach";

    private static final String INSERT_ACCOUNT_SQL = "INSERT INTO accounts" + "  (user_id, username, password) VALUES " + " (?, ?, ?);";
    private static final String CHECK_ACCOUNT_AVAILABLE = "select password from accounts where username=?";

    public AccountDao() {
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

    public void insertAccount(int userID, Account account){
        System.out.println(INSERT_ACCOUNT_SQL);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_ACCOUNT_SQL)) {
            preparedStatement.setInt(1, userID);
            preparedStatement.setString(2, account.getUsername());
            preparedStatement.setString(3, account.getPassword());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public Account isExisted(String username){
        Account acc=null;
        System.out.println(CHECK_ACCOUNT_AVAILABLE);
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(CHECK_ACCOUNT_AVAILABLE)) {
            preparedStatement.setString(1, username);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String password = rs.getString("password");
                acc =  new Account(username, password);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return acc;
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
