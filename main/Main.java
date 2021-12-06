package main;

import java.io.IOException;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
/**
 * Note: JavaDoc files for this application are located in the folder titled "JavaDocFiles" in the C195 folder.
 *
 * This application is entitled "C195PA" and was created according to the requirements described in WGU's C195
 * (Software II) course. It allows business users to add customers to their business database and schedule
 * appointments between these customers and the contacts from within the business. The application adjusts for the
 * user's local time and validates entries to prevent overlapping appointments and appointments outside of business
 * hours. This application utilizes JavaFX for the GUI, Java for the application programming, and MySQL for the
 * database. Thank you for perusing the source code!
 *
 * @author John
 *
 */
public class Main extends Application {
    /**
     * The start method loads the main stage and the login screen.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/view/Login.fxml"));
        Scene scene = new Scene(root, 300, 300, Color.WHITESMOKE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * This method contains one of the required lambda expressions. A common use case for lambda expressions
     * is to initialize threads in multiprogramming. The lambda expression is used here to turn
     * the main "launch" method of this application into a runnable thread. This thread allows the current application
     * to scale so that if it is part of a larger suite of applications, other applications could be threaded to
     * run in parallel with the current application.
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        Runnable r = () -> {launch(args); };
        r.run();
    }
}
