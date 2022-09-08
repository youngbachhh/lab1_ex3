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
import java.util.List;
import model.User;

public class UserDao {
    private String jdbcURL = "jdbc:mysql://localhost:3306/htttptit";
    private String jdbcUsername = "root";
    private String jdbcPassword = "youngbach";

    private static final String INSERT_USERS_SQL = "INSERT INTO users" + "  (name, phoneNum, address) VALUES " + " (?, ?, ?);";
    private static final String SELECT_USER_BY_ID = "select name,phoneNum,address from users where id =?";
    private static final String SELECT_ACCOUNT_BY_USERNAME = "select user_id,username,password from accounts where username =?";
    private static final String SELECT_NEW_USER_ID = "SELECT id FROM users ORDER BY id DESC LIMIT 1";

    public UserDao() {
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

    public int getNewID(){
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_NEW_USER_ID);) {
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return -1;
    }

    public void register(User user) {
        System.out.println(INSERT_USERS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USERS_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getPhoneNum());
            preparedStatement.setString(3, user.getAddress());
            System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public User getUserByUserID(int userID){
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_ID);) {
            preparedStatement.setInt(1, userID);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String phoneNum = rs.getString("phoneNum");
                String address = rs.getString("address");
                return new User(userID, name, phoneNum, address);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
    }

    public User login(String username, String userPassword) {
        User user = null;
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ACCOUNT_BY_USERNAME);) {
            preparedStatement.setString(1, username);
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int user_id = rs.getInt("user_id");
                String password = rs.getString("password");
                if(password.equals(userPassword)){
                    return getUserByUserID(user_id);
                }else{
                    return null;
                }
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
        return user;
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
