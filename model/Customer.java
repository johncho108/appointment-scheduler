package model;
/**
 * The Customer class implements the Customer object. The CustomerDAO class generates Customer objects
 * as interfaces between the application and the database. The application then uses the Customer objects to retrieve,
 * filter, and display customer information to the user.
 */
public class Customer {
    private int custID;
    public String custName;
    private String address;
    private Division division;
    private Country country;
    private String postalCode;
    private String phoneNumber;

    public Customer(int custID, String custName, String address, Division division, Country country, String postalCode, String phoneNumber) {
        this.custID = custID;
        this.custName = custName;
        this.address = address;
        this.division = division;
        this.country = country;
        this.postalCode = postalCode;
        this.phoneNumber = phoneNumber;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Division getDivision() {
        return division;
    }

    public void setDivision(Division division) {
        this.division = division;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
