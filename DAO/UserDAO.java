package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;
import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The UserDAO acts an interface between the application and the database. It allows the Controller classes to access
 * the list of all users.
 */
public class UserDAO {
    public static ObservableList<User> getAllUsers() throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT * FROM users";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<User> allUsers = FXCollections.observableArrayList();
        while (rs.next()) {
            int userID = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            User user = new User(userID, userName, password);
            allUsers.add(user);
        }
        JDBC.closeConnection();
        return allUsers;
    }
}
