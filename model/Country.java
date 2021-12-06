package model;
/**
 * The Country class implements the Country object. The AddressDAO class generates Country and Division objects
 * as interfaces between the application and the database. The Country objects allow the application to retrieve and filter
 * information about countries.
 */
public class Country {
    private int countryID;
    private String countryName;

    public Country(int countryID, String countryName) {
        this.countryID = countryID;
        this.countryName = countryName;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;

    }

    public String getCountry() {
        return countryName;
    }

    public void setCountry(String countryName) {
        this.countryName = countryName;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
