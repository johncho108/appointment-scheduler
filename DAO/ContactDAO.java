package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The ContactDAO acts an interface between the application and the database. It allows the Controller classes to access
 * the list of all contacts.
 */
public class ContactDAO {
    public static ObservableList<Contact> getAllContacts() throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT * FROM contacts";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        while (rs.next()) {
            int contactID = rs.getInt("Contact_ID");
            String contactName = rs.getString("Contact_Name");
            Contact contact = new Contact(contactID, contactName);
            allContacts.add(contact);
        }
        JDBC.closeConnection();
        return allContacts;
    }
}
