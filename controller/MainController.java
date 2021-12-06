package controller;

import DAO.AppointmentDAO;
import DAO.CustomerDAO;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The MainController class implements the main scene of the application. It displays tables of the appointments (filtered
 * by all, current month, and current week) and of the customers. Users can move from this scene to various reports
 * and the forms that are used to add or update appointments and customers.
 */
public class MainController implements Initializable {
    public TableView apptTable;
    public TableColumn apptIDCol;
    public TableColumn titleCol;
    public TableColumn descriptionCol;
    public TableColumn locationCol;
    public TableColumn contactCol;
    public TableColumn typeCol;
    public TableColumn startCol;
    public TableColumn endCol;
    public TableColumn apptCustIDCol;
    public TableColumn userIDCol;

    public TableView custTable;
    public TableColumn custCustIDCol;
    public TableColumn nameCol;
    public TableColumn divisionCol;

    public Button addApptBtn;
    public Button updateApptBtn;
    public Button deleteApptBtn;
    public Button addCustBtn;
    public Button updateCustBtn;
    public Button deleteCustBtn;

    public RadioButton allBtn;
    public RadioButton monthBtn;
    public RadioButton weekBtn;

    public static String formType;
    public static Customer selectedCustomer;
    public static Appointment selectedAppointment;

    /**
     * Initializes the main screen and fills the tables with the appointment and customer data from the database.
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {


        custCustIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("custName"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("division"));

        apptIDCol.setCellValueFactory(new PropertyValueFactory<>("apptID"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactCol.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        apptCustIDCol.setCellValueFactory(new PropertyValueFactory<>("custID"));
        userIDCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        try {
            custTable.setItems(CustomerDAO.getAllCustomers());
            apptTable.setItems(AppointmentDAO.getAllAppointments("all"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Directs user to add customer form.
     * @param actionEvent
     * @throws IOException
     */
    public void onAddCustBtn(ActionEvent actionEvent) throws IOException {
        formType = "addCust";
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 450, 500, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Directs user to update customer form.
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateCustBtn(ActionEvent actionEvent) throws IOException {
        formType = "updateCust";
        selectedCustomer = (Customer) custTable.getSelectionModel().getSelectedItem();
        if (selectedCustomer == null) {
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/view/Customer.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 450, 500, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Allows user to delete customer upon user confirmation.
     * @param actionEvent
     * @throws IOException
     */
    public void onDeleteCustBtn(ActionEvent actionEvent) throws IOException {
        Customer selectedCust = (Customer) custTable.getSelectionModel().getSelectedItem();
        if(selectedCust == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete");
        alert.setContentText("Are you sure you want to delete this customer?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                CustomerDAO.deleteCustomer(selectedCust.getCustID());
                custTable.setItems(CustomerDAO.getAllCustomers());
                apptTable.setItems(AppointmentDAO.getAllAppointments("all"));
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setTitle("Deleted");
                alert2.setHeaderText("Deleted");
                alert2.setContentText("This customer has been deleted.");
                alert2.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }

    /**
     * Directs user to add appointment form.
     * @param actionEvent
     * @throws IOException
     */
    public void onAddApptBtn(ActionEvent actionEvent) throws IOException {
        formType = "addAppt";
        Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 450, 500, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Directs user to update appointment form.
     * @param actionEvent
     * @throws IOException
     */
    public void onUpdateApptBtn(ActionEvent actionEvent) throws IOException {
        formType = "updateAppt";
        selectedAppointment = (Appointment) apptTable.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            return;
        }
        Parent root = FXMLLoader.load(getClass().getResource("/view/Appointment.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 450, 500, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Allows user to delete appointment upon user confirmation.
     * @param actionEvent
     */
    public void onDeleteApptBtn(ActionEvent actionEvent) {
        Appointment selectedAppt = (Appointment) apptTable.getSelectionModel().getSelectedItem();
        if(selectedAppt == null){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete");
        alert.setHeaderText("Delete");
        alert.setContentText("Are you sure you want to delete this appointment?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            try {
                AppointmentDAO.deleteAppointment(selectedAppt.getApptID());
                apptTable.setItems(AppointmentDAO.getAllAppointments("all"));
                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setTitle("Deleted");
                alert2.setHeaderText("Deleted");
                alert2.setContentText("This appointment has been deleted.\nAppointment ID: " + selectedAppt.getApptID() +
                        "\nAppointment Type: " +selectedAppt.getType());
                alert2.showAndWait();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Filters appointments table for all appointments.
     * @param actionEvent
     */
    public void onAllBtn(ActionEvent actionEvent) {
        try {
            apptTable.setItems(AppointmentDAO.getAllAppointments("all"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters appointments table for current month.
     * @param actionEvent
     */
    public void onMonthBtn(ActionEvent actionEvent) {
        try {
            apptTable.setItems(AppointmentDAO.getAllAppointments("month"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters appointments table for current week.
     * @param actionEvent
     */
    public void onWeekBtn(ActionEvent actionEvent) {
        try {
            apptTable.setItems(AppointmentDAO.getAllAppointments("week"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Directs user to appointments by type report.
     * @param actionEvent
     * @throws IOException
     */
    public void onAppointmentsByTypeBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/AppointmentsByType.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 400, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Directs user to schedules by contact report.
     * @param actionEvent
     * @throws IOException
     */
    public void onSchedulesByContactBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/SchedulesByContact.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 1000, 600, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Directs user to customers by location report.
     * @param actionEvent
     * @throws IOException
     */
    public void onCustomersByLocationBtn(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/CustomersByLocation.fxml"));
        Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root, 600, 600, Color.WHITESMOKE);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}


