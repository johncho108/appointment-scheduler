package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;

/**
 * The Appointment Controller manages the front-end logic of the Appointment scene. In the appointment scene, users
 * can add or update appointments.
 */
public class AppointmentController implements Initializable {
    // America/New_York
    private Appointment selectedAppointment = MainController.selectedAppointment;

    public Label titleLabel;
    public TextField apptIDField;
    public TextField titleField;
    public TextField descriptionField;
    public TextField locationField;
    public ComboBox contactField;
    public TextField typeField;
    public TextField custIDField;
    public TextField userIDField;
    public ComboBox startDate1;
    public ComboBox startDate2;
    public ComboBox startDate3;
    public ComboBox startTime1;
    public ComboBox startTime2;
    public ComboBox endDate1;
    public ComboBox endDate2;
    public ComboBox endDate3;
    public ComboBox endTime1;
    public ComboBox endTime2;

    public Button saveBtn;
    public Button cancelBtn;

    /**
     * This method checks whether the customer in question has a preexisting appointment that overlaps with the user-submitted
     * appointment.
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    private static boolean areOverlapped(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        if ((start1.isAfter(start2) || start1.isEqual(start2)) && start1.isBefore(end2)) {
            return true;
        }
        if (end1.isAfter(start2) && (end1.isBefore(end2) || end1.isEqual(end2))) {
            return true;
        }
        if ((start1.isBefore(start2) || start1.isEqual(start2)) && (end1.isAfter(end2) || end1.isEqual(end2))) {
            return true;
        }
        return false;
    }

    /**
     * This method initializes the Appointment scene. The form adjusts according to whether the user is
     * attempting to add or update an appointment. If the user is updating an appointment, the form will
     * be pre-filled with the selected appointment's data.
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        if (MainController.formType == "addAppt") {
            titleLabel.setText("Add Appointment");
        } else if (MainController.formType == "updateAppt") {
            titleLabel.setText("Update Appointment");
        }

        ObservableList<Integer> months = FXCollections.observableArrayList();
        ObservableList<Integer> days = FXCollections.observableArrayList();
        ObservableList<Integer> years = FXCollections.observableArrayList();
        ObservableList<Integer> hours = FXCollections.observableArrayList();
        ObservableList<String> minutes = FXCollections.observableArrayList();

        for (int i = 1; i <= 12; i++) {
            Integer month = i;
            months.add(month);
        }
        for (int i = 1; i <= 31; i++) {
            Integer day = i;
            days.add(day);
        }
        for (int i = 2000; i <= 2030; i++) {
            Integer year = i;
            years.add(year);
        }
        for (int i = 0; i <= 23; i++) {
            Integer hour = i;
            hours.add(hour);
        }
        minutes.addAll("00", "15", "30", "45");

        startDate1.setItems(months);
        startDate2.setItems(days);
        startDate3.setItems(years);
        startTime1.setItems(hours);
        startTime2.setItems(minutes);

        endDate1.setItems(months);
        endDate2.setItems(days);
        endDate3.setItems(years);
        endTime1.setItems(hours);
        endTime2.setItems(minutes);

        try {
            contactField.setItems(ContactDAO.getAllContacts());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (MainController.formType == "updateAppt") {
            apptIDField.setText(Integer.toString(selectedAppointment.getApptID()));
            titleField.setText(selectedAppointment.getTitle());
            descriptionField.setText(selectedAppointment.getDescription());
            locationField.setText(selectedAppointment.getLocation());
            contactField.setValue(selectedAppointment.getContact());;
            typeField.setText(selectedAppointment.getTitle());
            custIDField.setText(Integer.toString(selectedAppointment.getCustID()));
            userIDField.setText(Integer.toString(selectedAppointment.getUserID()));

            LocalDateTime startLDT = selectedAppointment.getStart();
            Integer month = startLDT.getMonthValue();
            Integer day = startLDT.getDayOfMonth();
            Integer year = startLDT.getYear();
            Integer hour = startLDT.getHour();
            Integer min = startLDT.getMinute();
            String minString;
            if (min == 0) {
                minString = "00";
            } else {
                minString = Integer.toString(min);
            }
            startDate1.setValue(month);
            startDate2.setValue(day);
            startDate3.setValue(year);
            startTime1.setValue(hour);
            startTime2.setValue(minString);

            LocalDateTime endLDT = selectedAppointment.getEnd();
            month = endLDT.getMonthValue();
            day = endLDT.getDayOfMonth();
            year = endLDT.getYear();
            hour = endLDT.getHour();
            min = endLDT.getMinute();
            if (min == 0) {
                minString = "00";
            } else {
                minString = Integer.toString(min);
            }
            endDate1.setValue(month);
            endDate2.setValue(day);
            endDate3.setValue(year);
            endTime1.setValue(hour);
            endTime2.setValue(minString);
        }
    }

    /**
     * This method checks whether the user-submitted appointment overlaps with the selected customer's other
     * appointments and whether the appointment falls within business hours. It then saves the appointment
     * to the database.
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveBtn(ActionEvent actionEvent) throws IOException {
        try {
            String title = titleField.getText();
            String description = descriptionField.getText();
            String location = locationField.getText();
            String type = typeField.getText();
            Contact contact = (Contact) contactField.getValue();
            String contactID = Integer.toString(contact.getContactID());
            String custID = custIDField.getText();
            String userID = userIDField.getText();
            String[] appointmentInfo = {title, description, location, type, contactID, custID, userID};

            Integer startMonth = (Integer) startDate1.getValue();
            Integer startDay = (Integer) startDate2.getValue();
            Integer startYear = (Integer) startDate3.getValue();
            Integer startHour = (Integer) startTime1.getValue();
            Integer startMinute = Integer.valueOf((String) startTime2.getValue());
            LocalDateTime startLDT = LocalDateTime.of(startYear, startMonth, startDay, startHour, startMinute);
            ZonedDateTime startZDT = startLDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime easternStartZDT = startZDT.withZoneSameInstant(ZoneId.of("America/New_York"));
            int easternStartYear = easternStartZDT.getYear();
            int easternStartMonth = easternStartZDT.getMonthValue();
            int easternStartDay = easternStartZDT.getDayOfMonth();

            Integer endMonth = (Integer) endDate1.getValue();
            Integer endDay = (Integer) endDate2.getValue();
            Integer endYear = (Integer) endDate3.getValue();
            Integer endHour = (Integer) endTime1.getValue();
            Integer endMinute = Integer.valueOf((String) endTime2.getValue());
            LocalDateTime endLDT = LocalDateTime.of(endYear, endMonth, endDay, endHour, endMinute);
            ZonedDateTime endZDT = endLDT.atZone(ZoneId.of(ZoneId.systemDefault().toString()));
            ZonedDateTime easternEndZDT = endZDT.withZoneSameInstant(ZoneId.of("America/New_York"));

            ZonedDateTime businessDayStart = ZonedDateTime.of(easternStartYear, easternStartMonth, easternStartDay, 8, 0, 0, 0, ZoneId.of("America/New_York"));
            ZonedDateTime businessDayEnd = ZonedDateTime.of(easternStartYear, easternStartMonth, easternStartDay, 22, 0, 0, 0, ZoneId.of("America/New_York"));
            if (easternStartZDT.isBefore(businessDayStart) || easternEndZDT.isAfter(businessDayEnd)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Entry");
                alert.setHeaderText("Invalid Entry");
                alert.setContentText("The appointments must be scheduled between 8 AM and 10 PM Eastern. " +
                        "Please try again.");
                alert.showAndWait();
                return;
            }
            if (startLDT.isAfter(endLDT) || startLDT.isEqual(endLDT)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Entry");
                alert.setHeaderText("Invalid Entry");
                alert.setContentText("The start of the appointment must occur before the end of the appointment. " +
                        "Please try again.");
                alert.showAndWait();
                return;
            }

            ObservableList<Appointment> custAppts = FXCollections.observableArrayList();
            custAppts = AppointmentDAO.getAppointmentsForCustomer(Integer.parseInt(custID));
            if (MainController.formType == "updateAppt") {
                for (Appointment a : custAppts) {
                    if (a.getApptID() != selectedAppointment.getApptID()) {
                        LocalDateTime start2 = a.getStart();
                        LocalDateTime end2 = a.getEnd();
                        if (areOverlapped(startLDT, endLDT, start2, end2)) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            alert.setTitle("Invalid Entry");
                            alert.setHeaderText("Invalid Entry");
                            alert.setContentText("This appointment overlaps with at least one of the customer's " +
                                    "appointments. Please try again.");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            } else if (MainController.formType == "addAppt") {
                for (Appointment a : custAppts) {
                    LocalDateTime start2 = a.getStart();
                    LocalDateTime end2 = a.getEnd();
                    if (areOverlapped(startLDT, endLDT, start2, end2) == true) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Invalid Entry");
                        alert.setHeaderText("Invalid Entry");
                        alert.setContentText("This appointment overlaps with at least one of the customer's " +
                                "appointments. Please try again.");
                        alert.showAndWait();
                        return;
                    }
                }
            }
            if (MainController.formType == "addAppt") {
                AppointmentDAO.addAppointment(appointmentInfo, startLDT, endLDT);
            } else if (MainController.formType == "updateAppt") {
                AppointmentDAO.updateAppointment(selectedAppointment.getApptID(), appointmentInfo, startLDT, endLDT);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        onReturn(actionEvent);
    }
    /**
     * Brings user back to main scene.
     * @param actionEvent
     * @throws IOException
     */
    public void onReturn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1200, 600, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        return;
    }
}


