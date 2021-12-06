package model;

import java.time.LocalDateTime;

/**
 * The Appointment class implements the Appointment object. The AppointmentDAO class generates the Appointment objects
 * as an interface between the application and the database. The application then uses the Appointment objects to retrieve,
 * filter, and display appointments to the user.
 */
public class Appointment {
    private int apptID;
    private String title;
    private String description;
    private String location;
    private Contact contact;
    private String type;
    private LocalDateTime start;
    private LocalDateTime end;
    private int custID;
    private int userID;

    public Appointment(int apptID, String title, String description, String location, Contact contact, String type, LocalDateTime start, LocalDateTime end, int custID, int userID) {
        this.apptID = apptID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.contact = contact;
        this.type = type;
        this.start = start;
        this.end = end;
        this.custID = custID;
        this.userID = userID;
    }

    public int getApptID() {
        return apptID;
    }

    public void setApptID(int apptID) {
        this.apptID = apptID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public int getCustID() {
        return custID;
    }

    public void setCustID(int custID) {
        this.custID = custID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }
}
