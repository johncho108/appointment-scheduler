package model;
/**
 * The Division class implements the Division object. The AddressDAO class generates Division and Country objects
 * as interfaces between the application and the database. The Division objects allow the application to retrieve and filter
 * information about divisions.
 */
public class Division {
    private int divisionID;
    private String divisionName;
    private int countryID;

    public Division(int divisionID, String divisionName, int countryID) {
        this.divisionID = divisionID;
        this.divisionName = divisionName;
        this.countryID = countryID;
    }

    public int getDivisionID() {
        return divisionID;
    }

    public void setDivisionID(int divisionID) {
        this.divisionID = divisionID;
    }

    public String getDivisionName() {
        return divisionName;
    }

    public void setDivisionName(String division) {
        this.divisionName = division;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    @Override
    public String toString() {
        return divisionName;
    }
}
