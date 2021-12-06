package controller;

import DAO.AddressDAO;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Country;
import model.Customer;
import model.Division;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * The Customer Controller manages the front-end logic of the Customer scene. In the customer scene, users
 * can add or update customers.
 */
public class CustomerController implements Initializable {
    private Customer selectedCustomer = MainController.selectedCustomer;
    public Label titleLabel;
    public TextField custIDField;
    public TextField custNameField;
    public TextField addressField;
    public TextField postalCodeField;
    public TextField phoneNumberField;
    public ComboBox countryField;
    public ComboBox divisionField;

    public Button saveBtn;
    public Button cancelBtn;

    /**
     * This method initializes the Customer scene. The form adjusts according to whether the user is
     * attempting to add or update an customer. If the user is updating a customer, the form will
     * be pre-filled with the selected customer's data.
     * @param location
     * @param resources
     */
    public void initialize(URL location, ResourceBundle resources) {
        if (MainController.formType == "addCust") {
            titleLabel.setText("Add Customer");
            try {
                ObservableList<Country> allCountries = AddressDAO.getAllCountries();
                countryField.setItems(allCountries);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (MainController.formType == "updateCust") {
            titleLabel.setText("Update Customer");
            try {
                custIDField.setText(Integer.toString(selectedCustomer.getCustID()));
                custNameField.setText(selectedCustomer.getCustName());
                addressField.setText(selectedCustomer.getAddress());
                postalCodeField.setText(selectedCustomer.getPostalCode());
                phoneNumberField.setText(selectedCustomer.getPhoneNumber());
                countryField.setValue(selectedCustomer.getCountry());
                divisionField.setValue(selectedCustomer.getDivision());
                ObservableList<Country> allCountries = AddressDAO.getAllCountries();
                countryField.setItems(allCountries);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method saves the customer to the database.
     * @param actionEvent
     * @throws IOException
     */
    public void onSaveBtn(ActionEvent actionEvent) throws IOException {
        try {
            String custName = custNameField.getText();
            String address = addressField.getText();
            String postalCode = postalCodeField.getText();
            String phoneNumber = phoneNumberField.getText();
            Division division = (Division) divisionField.getValue();
            String divisionID = Integer.toString(division.getDivisionID());
            String[] customerInfo = {custName, address, postalCode, phoneNumber, divisionID};
            if (MainController.formType == "addCust") {
                CustomerDAO.addCustomer(customerInfo);
            } else if (MainController.formType == "updateCust") {
                CustomerDAO.updateCustomer(selectedCustomer.getCustID(), customerInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        onReturn(actionEvent);
    }

    /**
     * This method brings the user back to the main screen.
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

    /**
     * This method pulls up the list of divisions in the specified country once the user chooses the country.
     * @param actionEvent
     */
    public void onCountryField(ActionEvent actionEvent) {
        if (MainController.formType == "updateCust") {
            divisionField.setValue(null);
        }
        Country selectedCountry = (Country) countryField.getSelectionModel().getSelectedItem();
        ObservableList<Division> divisionsByCountry = null;
        try {
            divisionsByCountry = AddressDAO.getDivisionsByCountry(selectedCountry.getCountryID());
        } catch (Exception e) {
            e.printStackTrace();
        }
        divisionField.setItems(divisionsByCountry);
    }
}


