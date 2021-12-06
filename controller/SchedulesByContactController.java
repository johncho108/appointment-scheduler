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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The SchedulesByContact Controller manages the front-end logic of the SchedulesByContact scene.
 */
public class SchedulesByContactController implements Initializable {
    public ComboBox contactField;
    public Button submitBtn;
    public Button returnBtn;

    public TableView apptTable;
    public TableColumn apptIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn apptCustIDCol;

    /**
     * Initializes the SchedulesByContact scene.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Contact> contacts = FXCollections.observableArrayList();
        try {
            contacts = ContactDAO.getAllContacts();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        contactField.setItems(contacts);

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
    }

    /**
     * Updates tableview to display schedules for the user-specified contact.
     * @param actionEvent
     * @throws SQLException
     */
    public void onSubmitBtn(ActionEvent actionEvent) throws SQLException {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        Contact contact = (Contact) contactField.getValue();
        int contactID = contact.getContactID();
        appointments = AppointmentDAO.getAppointmentsByContact(contactID);
        apptTable.setItems(appointments);
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
