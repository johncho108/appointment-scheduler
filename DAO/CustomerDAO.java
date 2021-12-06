package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Customer;
import model.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The CustomerDAO acts an interface between the application and the database. It allows users to execute CRUD
 * operations on the database, such as adding customers, updating customers, or filtering customers. These
 * operations are utilized in the Controller classes to display the relevant customer information to users.
 */
public class CustomerDAO {
    /**
     * This method adds a new customer to the database.
     * @param customerInfo
     * @throws SQLException
     * @throws Exception
     */
    public static void addCustomer(String[] customerInfo) throws SQLException, Exception {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "INSERT INTO customers(Customer_Name, Address, Postal_Code, Phone, Division_ID) " +
                "VALUES(?,?,?,?,?)";

        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        for (int i = 0; i < 5; i++) {
            ps.setString(i+1, customerInfo[i]);
        };
        ps.execute();
        JDBC.closeConnection();
        return;
    }

    /**
     * This method updates a preexisting customer in the database.
     * @param custID
     * @param customerInfo
     * @throws SQLException
     * @throws Exception
     */
    public static void updateCustomer(int custID, String[] customerInfo) throws SQLException, Exception {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "UPDATE customers " +
                "SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Division_ID = ? " +
                "WHERE Customer_ID = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        for (int i = 0; i < 5; i++) {
            ps.setString(i+1, customerInfo[i]);
        }
        ps.setString(6, Integer.toString(custID));
        ps.execute();
        JDBC.closeConnection();
        return;
    }

    /**
     * This method deletes a preexisting customer from the database.
     * @param custID
     * @throws SQLException
     * @throws Exception
     */
    public static void deleteCustomer(int custID) throws SQLException, Exception {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "DELETE appointments FROM appointments " +
                "WHERE Customer_ID = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.setInt(1, custID);
        ps.execute();

        sql = "DELETE FROM customers " +
                "WHERE Customer_ID = ?";
        DBQuery.setStatement(conn, sql);
        ps = DBQuery.getStatement();
        ps.setInt(1, custID);
        ps.execute();
        JDBC.closeConnection();
        return;
    }

    /**
     * This method allows the Controller classes to access all customers from the database.
     * @return ObservableList<Customer>
     * @throws SQLException
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT * FROM customers " +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID ";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        while (rs.next()) {
            int custID = rs.getInt("customers.Customer_ID");
            String custName = rs.getString("customers.Customer_Name");
            String address = rs.getString("customers.Address");
            String postalCode = rs.getString("customers.Postal_Code");
            String phoneNumber = rs.getString("customers.Phone");

            String divisionName = rs.getString("first_level_divisions.Division");
            int divisionID = rs.getInt("first_level_divisions.Division_ID");
            String countryName = rs.getString("countries.Country");
            int countryID = rs.getInt("countries.Country_ID");

            Division division = new Division(divisionID, divisionName, countryID);
            Country country = new Country(countryID, countryName);
            Customer customer = new Customer(custID, custName, address, division, country, postalCode, phoneNumber);
            allCustomers.add(customer);
        }
        JDBC.closeConnection();
        return allCustomers;
    }

    /**
     * This method allows the Controller classes to access all customers by location.
     * @param location
     * @return ObservableList<Customer>
     * @throws SQLException
     */
    public static ObservableList<Customer> getCustomersByLocation(String location) throws SQLException {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT * FROM customers " +
                "JOIN appointments on customers.Customer_ID = appointments.Customer_ID " +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "JOIN countries ON first_level_divisions.Country_ID = countries.Country_ID " +
                "WHERE appointments.location = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.setString(1, location);

        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
        while (rs.next()) {
            int custID = rs.getInt("customers.Customer_ID");
            String custName = rs.getString("customers.Customer_Name");
            String address = rs.getString("customers.Address");
            String postalCode = rs.getString("customers.Postal_Code");
            String phoneNumber = rs.getString("customers.Phone");

            String divisionName = rs.getString("first_level_divisions.Division");
            int divisionID = rs.getInt("first_level_divisions.Division_ID");
            String countryName = rs.getString("countries.Country");
            int countryID = rs.getInt("countries.Country_ID");

            Division division = new Division(divisionID, divisionName, countryID);
            Country country = new Country(countryID, countryName);
            Customer customer = new Customer(custID, custName, address, division, country, postalCode, phoneNumber);
            allCustomers.add(customer);
        }
        JDBC.closeConnection();
        return allCustomers;
    }
}
