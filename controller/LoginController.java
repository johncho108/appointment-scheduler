package controller;

import DAO.AppointmentDAO;
import DAO.UserDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Appointment;
import model.User;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The Login Controller manages the front-end logic of the Login scene. The Login scene ccepts a username
 * and password, and if the username and password are found in the database, allows access to the main scene. The
 * Login scene also notifies the user if there exists an upcoming appointment within the next fifteen minutes of
 * the user's local time.
 */
public class LoginController implements Initializable {
    private ResourceBundle rb = ResourceBundle.getBundle("main/Nat", Locale.FRANCE);
    public Label titleLabel;
    public Label locationLabel;
    public TextField userNameField;
    public TextField passwordField;
    public Button loginBtn;

    /**
     * Initializes the login screen. Note that if the user's computer's default system language is French, all
     * text in the login screen will be translated into French using the Resource Bundle "Nat_fr.properties."
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        ZoneId zone = ZoneId.systemDefault();
        locationLabel.setText(zone.getId());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            titleLabel.setText(rb.getString("Login"));
            userNameField.setPromptText(rb.getString("User Name"));
            passwordField.setPromptText(rb.getString("Password"));
            loginBtn.setText(rb.getString("Login"));
        }
    }

    /**
     * Authenticates username and password. Also, notifies user of upcoming appointments and logs every login
     * attempt to the login_activity.txt file in the root directory.
     * @param actionEvent
     * @throws SQLException
     * @throws IOException
     */
    public void onLogin(ActionEvent actionEvent) throws SQLException, IOException {
        String filename = "login_activity.txt";
        FileWriter fwriter = new FileWriter(filename, true);
        PrintWriter outputFile = new PrintWriter(fwriter);
        LocalDateTime loginLDT= LocalDateTime.now();
        LocalDate loginDate = loginLDT.toLocalDate();
        LocalTime loginTime = loginLDT.toLocalTime();
        String userName = userNameField.getText();
        String password = passwordField.getText();

        outputFile.println("Login attempt:");
        outputFile.println("Username: " + userName);
        outputFile.println(loginLDT);
        outputFile.println(loginDate);
        outputFile.println(loginTime);
        ObservableList<User> users = UserDAO.getAllUsers();
        for (User u : users) {
            if (userName.equals(u.getUserName()) && password.equals(u.getPassword())) {
                outputFile.println("Login successful");
                outputFile.println(" ");
                outputFile.close();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                ObservableList<Appointment> appointments = FXCollections.observableArrayList();
                try {
                    appointments = AppointmentDAO.getAllAppointments("all");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                boolean upcomingApptExists = false;
                for (Appointment a : appointments) {
                    if (loginLDT.isBefore(a.getStart()) && (loginLDT.plus(15, ChronoUnit.MINUTES).isAfter(a.getStart()) ||
                            loginLDT.plus(15, ChronoUnit.MINUTES).isEqual(a.getStart()))) {
                        upcomingApptExists = true;
                        LocalDate apptDate = a.getStart().toLocalDate();
                        LocalTime apptTime = a.getStart().toLocalTime();
                        alert.setTitle("Upcoming Appointment");
                        alert.setHeaderText("Upcoming Appointment");
                        alert.setContentText("Appointment ID: " + a.getApptID() +
                                "\nAppointment Date: " + apptDate +
                                "\nAppointment Time: " + apptTime);
                        alert.showAndWait();
                    }
                }
                if (upcomingApptExists == false) {
                    alert.setTitle("Upcoming Appointment");
                    alert.setHeaderText("Upcoming Appointment");
                    alert.setContentText("There are no upcoming appointments.");
                    alert.showAndWait();
                }
                Parent root = FXMLLoader.load(getClass().getResource("/view/Main.fxml"));
                Stage stage = (Stage)((Button)actionEvent.getSource()).getScene().getWindow();
                Scene scene = new Scene(root, 1200, 600, Color.WHITESMOKE);
                stage.setResizable(false);
                stage.setScene(scene);
                stage.show();
                return;
            }
        }
        outputFile.println("Login failed");
        outputFile.println(" ");
        outputFile.close();
        Alert alert2 = new Alert(Alert.AlertType.WARNING);

        if (Locale.getDefault().getLanguage().equals("fr")) {
            alert2.setTitle(rb.getString("Login Failed"));
            alert2.setHeaderText(rb.getString("Login Failed"));
            alert2.setContentText(rb.getString("The user name and/or password are incorrect. Please try again."));
            alert2.showAndWait();
            return;
        } else {
            alert2.setTitle("Login Failed");
            alert2.setHeaderText("Login Failed");
            alert2.setContentText("The user name and/or password are incorrect. Please try again.");
            alert2.showAndWait();
            return;
        }

    }

}

