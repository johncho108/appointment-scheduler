package controller;

import DAO.AppointmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * The AppointmentsByType Controller manages the front-end logic of the AppointmentsByType scene.
 */
public class AppointmentsByTypeController implements Initializable {
    public ComboBox monthField;
    public ComboBox appointmentTypeField;
    public Button submitBtn;
    public Label resultLabel;
    public Button returnBtn;

    /**
     * Initializes the AppointmentsByType scene. Note: This method contains one of the required lambda expressions
     * (see lines 55-59). Lambda expressions are most often used when short methods with return values are in turn
     * used as parameters for another function. They are also often used to anonymize methods within for-loops in order to
     * simplify the syntax and make the code more readable, as demonstrated below.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Integer> months = FXCollections.observableArrayList();
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        ObservableList<String> types = FXCollections.observableArrayList();
        for (int i = 1; i <= 12; i++) {
            Integer month = i;
            months.add(month);
        }
        monthField.setItems(months);

        try {
            appointments = AppointmentDAO.getAllAppointments("all");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        appointments.forEach( (a) -> {
            if (!types.contains(a.getType())) {
                types.add(a.getType());
            }
        } );
        appointmentTypeField.setItems(types);
    }

    /**
     * Updates scene to display the number of appointments by user-submitted month and type.
     * @param actionEvent
     * @throws SQLException
     */
    public void onSubmitBtn(ActionEvent actionEvent) throws SQLException {
        int res = AppointmentDAO.getCountByType((Integer)monthField.getValue(),(String)appointmentTypeField.getValue());
        resultLabel.setText("Number of appointments by type: " + Integer.toString(res));
    }

    /**
     * Brings user back to the main scene.
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
