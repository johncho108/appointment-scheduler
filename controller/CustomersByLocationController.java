package controller;

import DAO.AppointmentDAO;
import DAO.ContactDAO;
import DAO.CustomerDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.Contact;
import model.Customer;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The SchedulesByContact Controller manages the front-end logic of the CustomersByLocation scene.
 */
public class CustomersByLocationController implements Initializable {
    public ComboBox locationField;
    public Button submitBtn;
    public Button returnBtn;

    public TableView custTable;
    public TableColumn custCustIDCol;
    public TableColumn nameCol;
    public TableColumn divisionCol;

    /**
     * Initializes the SchedulesByContact scene. Note: This method contains one of the required lambda expressions
     * (see lines 57-61). Lambda expressions are most often used when short methods with return values are in turn
     * used as parameters for another function. They are also often used to anonymize methods within for-loops in order to
     * simplify the syntax and make the code more readable, as demonstrated below.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ObservableList<String> locations = FXCollections.observableArrayList();
        try {
            appointments = AppointmentDAO.getAllAppointments("all");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        appointments.forEach( (a) -> {
            if (!locations.contains(a.getLocation())) {
                locations.add(a.getLocation());
            }
        } );
        locationField.setItems(locations);

        custCustIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));
    }

    /**
     * Updates tableview to display customers filtered by user-submitted location.
     * @param actionEvent
     * @throws SQLException
     */
    public void onSubmitBtn(ActionEvent actionEvent) throws SQLException {
        ObservableList<Customer> customers = FXCollections.observableArrayList();
        String location = (String) locationField.getValue();
        customers = CustomerDAO.getCustomersByLocation(location);
        custTable.setItems(customers);
    }

    /**
     * Brings the user back to the main scene.
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
