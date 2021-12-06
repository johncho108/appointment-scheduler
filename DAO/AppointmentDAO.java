package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

import java.sql.*;
import java.time.LocalDateTime;

/**
 * The AppointmentDAO acts an interface between the application and the database. It allows users to execute CRUD
 * operations on the database, such as adding appointments, updating appointments, or filtering appointments. These
 * operations are utilized in the Controller classes to display the relevant appointment information to users.
 */
public class AppointmentDAO {
    /**
     * This method adds a new appointment to the database.
     * @param appointmentInfo
     * @param startLDT
     * @param endLDT
     * @throws SQLException
     */
    public static void addAppointment(String[] appointmentInfo, LocalDateTime startLDT, LocalDateTime endLDT)
            throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;
        sql = "INSERT INTO appointments(Title, Description, Location, Type, " +
                "Contact_ID, Customer_ID, User_ID, Start, End ) " +
                "VALUES(?,?,?,?,?,?,?,?,?)";

        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        for (int i = 0; i < 7; i++) {
            ps.setString(i+1, appointmentInfo[i]);
        }
        ps.setTimestamp(8, Timestamp.valueOf(startLDT));
        ps.setTimestamp(9, Timestamp.valueOf(endLDT));
        ps.execute();
        JDBC.closeConnection();
        return;
    }

    /**
     * This method updates a preexisting appointment in the database.
     * @param apptID
     * @param appointmentInfo
     * @param startLDT
     * @param endLDT
     * @throws SQLException
     */
    public static void updateAppointment(int apptID, String[] appointmentInfo, LocalDateTime startLDT, LocalDateTime endLDT)
            throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "UPDATE appointments " +
                "SET Title = ?, Description = ?, Location = ?, Type = ?, " +
                "Contact_ID = ?, Customer_ID = ?, User_ID = ?, Start = ?, End = ? " +
                "WHERE Appointment_ID = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        for (int i = 0; i < 7; i++) {
            ps.setString(i+1, appointmentInfo[i]);
        }
        ps.setTimestamp(8, Timestamp.valueOf(startLDT));
        ps.setTimestamp(9, Timestamp.valueOf(endLDT));
        ps.setString(10, Integer.toString(apptID));
        ps.execute();
        JDBC.closeConnection();
        return;
    }

    /**
     * This method deletes preexisting appointments from the database.
     * @param apptID
     * @throws SQLException
     */
    public static void deleteAppointment(int apptID) throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "DELETE appointments FROM appointments " +
                "WHERE appointment_ID = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.setInt(1, apptID);
        ps.execute();
        JDBC.closeConnection();
        return;
    }

    /**
     * This method allows the Controller classes to access either all appointments from all time,
     * from the past month, or from the past week, depending on the filter parameter.
     * @param filter
     * @return ObservableList<Appointment>
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAllAppointments(String filter) throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT * FROM appointments " +
                "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "JOIN users ON appointments.User_ID = users.User_ID " +
                "JOIN customers ON appointments.Customer_ID = customers.Customer_ID ";
        if (filter == "month") {
            sql += "WHERE MONTH(start) = MONTH(CURDATE()) " +
                    "AND YEAR(start) = YEAR(CURDATE())";
        } else if (filter == "week") {
            sql += "WHERE WEEK(start) = WEEK(CURDATE()) " +
                    "AND YEAR(start) = YEAR(CURDATE())";
        }
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        while (rs.next()) {
            int apptID = rs.getInt("appointments.Appointment_ID");
            String title = rs.getString("appointments.Title");
            String description = rs.getString("appointments.Description");
            String location = rs.getString("appointments.Location");
            int contactID = rs.getInt("contacts.Contact_ID");
            String contactName = rs.getString("contacts.Contact_Name");
            String type = rs.getString("appointments.Type");
            Timestamp start = rs.getTimestamp("appointments.Start");
            Timestamp end = rs.getTimestamp("appointments.End");
            LocalDateTime startLDT = start.toLocalDateTime();
            LocalDateTime endLDT = end.toLocalDateTime();
            int custID = rs.getInt("customers.Customer_ID");
            int userID = rs.getInt("users.User_ID");

            Contact contact = new Contact(contactID, contactName);
            Appointment appointment = new Appointment(apptID, title, description, location, contact, type, startLDT, endLDT, custID, userID);
            allAppointments.add(appointment);
        }
        JDBC.closeConnection();
        return allAppointments;
    }

    /**
     * This method allows the Controller classes to filter appointments by customer.
     * @param custID
     * @return ObservableList<Appointment>
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentsForCustomer(int custID) throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT * FROM appointments " +
                "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "JOIN users ON appointments.User_ID = users.User_ID " +
                "JOIN customers ON appointments.Customer_ID = customers.Customer_ID " +
                "WHERE customers.Customer_ID = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.setInt(1, custID);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        while (rs.next()) {
            int apptID = rs.getInt("appointments.Appointment_ID");
            String title = rs.getString("appointments.Title");
            String description = rs.getString("appointments.Description");
            String location = rs.getString("appointments.Location");
            int contactID = rs.getInt("contacts.Contact_ID");
            String contactName = rs.getString("contacts.Contact_Name");
            String type = rs.getString("appointments.Type");
            Timestamp start = rs.getTimestamp("appointments.Start");
            Timestamp end = rs.getTimestamp("appointments.End");
            LocalDateTime startLDT = start.toLocalDateTime();
            LocalDateTime endLDT = end.toLocalDateTime();
            int userID = rs.getInt("users.User_ID");

            Contact contact = new Contact(contactID, contactName);
            Appointment appointment = new Appointment(apptID, title, description, location, contact, type, startLDT, endLDT, custID, userID);
            allAppointments.add(appointment);
        }
        JDBC.closeConnection();
        return allAppointments;
    }

    /**
     * This method allows the Controller classes to get the count of a type of appointment.
     * @param month
     * @param type
     * @return int
     * @throws SQLException
     */
    public static int getCountByType(int month, String type) throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT COUNT(Appointment_ID) FROM appointments " +
                "WHERE MONTH(Start) = ? AND " +
                "Type = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.setInt(1, month);
        ps.setString(2, type);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        int count = 0;
        while (rs.next()) {
            count = rs.getInt("count(appointment_id)");
        }
        JDBC.closeConnection();
        return count;
    }

    /**
     * This method allows the Controller classes to access appointments by contact.
     * @param ID
     * @return ObservableList<Appointment>
     * @throws SQLException
     */
    public static ObservableList<Appointment> getAppointmentsByContact(int ID) throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT * FROM appointments " +
                "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "JOIN users ON appointments.User_ID = users.User_ID " +
                "JOIN customers ON appointments.Customer_ID = customers.Customer_ID " +
                "WHERE contacts.Contact_ID = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.setInt(1, ID);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
        while (rs.next()) {
            int custID = rs.getInt("customers.Customer_ID");
            int apptID = rs.getInt("appointments.Appointment_ID");
            String title = rs.getString("appointments.Title");
            String description = rs.getString("appointments.Description");
            String location = rs.getString("appointments.Location");
            int contactID = rs.getInt("contacts.Contact_ID");
            String contactName = rs.getString("contacts.Contact_Name");
            String type = rs.getString("appointments.Type");
            Timestamp start = rs.getTimestamp("appointments.Start");
            Timestamp end = rs.getTimestamp("appointments.End");
            LocalDateTime startLDT = start.toLocalDateTime();
            LocalDateTime endLDT = end.toLocalDateTime();
            int userID = rs.getInt("users.User_ID");

            Contact contact = new Contact(contactID, contactName);
            Appointment appointment = new Appointment(apptID, title, description, location, contact, type, startLDT, endLDT, custID, userID);
            allAppointments.add(appointment);
        }
        JDBC.closeConnection();
        return allAppointments;
    }
}
