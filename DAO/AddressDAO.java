package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.Division;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 * The AddressDAO acts an interface between the application and the database. It allows the Controller classes to access
 * lists of all countries and divisions.
 */
public class AddressDAO {
    public static ObservableList<Country> getAllCountries() throws SQLException, Exception {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT Country_ID, Country FROM countries";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Country> allCountries = FXCollections.observableArrayList();
        while (rs.next()) {
            int countryID = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            Country country = new Country(countryID, countryName);
            allCountries.add(country);
        }
        JDBC.closeConnection();
        return allCountries;
    }

    public static ObservableList<Division> getDivisionsByCountry(int countryID) throws SQLException, Exception {
        JDBC.openConnection();
        Connection conn = JDBC.connection;
        String sql;

        sql = "SELECT Division_ID, Division, Country_ID " +
                "FROM first_level_divisions " +
                "WHERE Country_ID = ?";
        DBQuery.setStatement(conn, sql);
        PreparedStatement ps = DBQuery.getStatement();
        ps.setInt(1, countryID);
        ps.execute();

        ResultSet rs = ps.getResultSet();
        ObservableList<Division> divisionsByCountry = FXCollections.observableArrayList();
        while (rs.next()) {
            int divisionID = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            Division division = new Division(divisionID, divisionName, countryID);
            divisionsByCountry.add(division);
        }
        JDBC.closeConnection();
        return divisionsByCountry;
    }
}
