package model;
/**
 * The Contact class implements the Contact object. The ContactDAO class generates the Contact objects
 * as an interface between the application and the database. The Contact objects allow the application to retrieve and filter
 * information about contacts.
 */
public class Contact {
    private int contactID;
    private String contactName;

    public Contact(int contactID, String contactName) {
        this.contactID = contactID;
        this.contactName = contactName;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return contactName;
    }
}


