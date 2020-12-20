package View_Controller;

import Model.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddPartController implements Initializable {
    @FXML private RadioButton addInHousePartRadio, addOutsourcedPartRadio;
    @FXML private Label newPartMachineID;
    @FXML private TextField newPartNameInput, newPartInvInput, newPartPriceInput, newPartMaxInput, newPartMinInput, newPartMachineInput;
    @FXML private Button partCancelButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(addOutsourcedPartRadio.isSelected()) {
            newPartMachineID.setText("Company Name");
            newPartMachineInput.setPromptText("Company Name");
        }
        else {
            newPartMachineID.setText("Machine ID");
            newPartMachineInput.setPromptText("Machine ID");
        }
    }

    // Check user inputs to check for errors before attempting to save
    @FXML
    private boolean validateData() throws IOException {
        try {
            int stock = Integer.parseInt(newPartInvInput.getText().trim());
            int min = Integer.parseInt(newPartMinInput.getText().trim());
            int max = Integer.parseInt(newPartMaxInput.getText().trim());
            double price = Double.valueOf(newPartPriceInput.getText().trim());
            if(max <= min) {
                Alerts.newError(8);
                return false;
            }

            else if(min < 1) {
                Alerts.newError(9);
                return false;
            }

            else if (stock > max || stock < min) {
                Alerts.newError(6);
                return false;
            }

            else if (price <= 0) {
                Alerts.newError(13);
                return false;
            }

            else if (newPartNameInput.getText().trim().isEmpty()) {
                Alerts.newError(7);
                return false;
            }

            else {
                return true;
            }
        }
        catch(Exception error) {
            Alerts.newError(5);
            return false;
        }
    }

    // Validate data, attempt save, raise error code 12 if save is not successful
    @FXML private void SavePartEventHandler(ActionEvent event) throws IOException {
        if(validateData()) {
            int partID = Main.newPartIdGenerator();
            String name = newPartNameInput.getText().trim();
            int stock = Integer.parseInt(newPartInvInput.getText().trim());
            int min = Integer.parseInt(newPartMinInput.getText().trim());
            int max = Integer.parseInt(newPartMaxInput.getText().trim());
            double price = Double.parseDouble(newPartPriceInput.getText().trim());
            try {
                if (addInHousePartRadio.isSelected()) {
                    int machineId = Integer.parseInt(newPartMachineInput.getText().trim());
                    Part newPart = new Model.InHouse(partID, name, price, stock, min, max, machineId);
                    Model.Inventory.addPart(newPart);
                } else {
                    String companyName = newPartMachineInput.getText().trim();
                    Part newPart = new Model.Outsourced(partID, name, price, stock, min, max, companyName);
                    Model.Inventory.addPart(newPart);
                }
                returnToMain();
            }   catch(Exception error) {
                Alerts.newError(12);
            }
        }
    }

    // Cancel Add Part action, reset text fields
    @FXML private void CancelPartEventHandler(ActionEvent event) throws IOException {
        newPartNameInput.clear();
        newPartPriceInput.clear();
        newPartInvInput.clear();
        newPartMaxInput.clear();
        newPartMinInput.clear();
        newPartMachineInput.clear();
        returnToMain();
    }

    // Methods to change label and prompt text based on selected Part type
    @FXML private void InHouseRadioEventHandler(ActionEvent event) throws IOException {
            newPartMachineID.setText("Machine ID");
            newPartMachineInput.setPromptText("Machine ID");
    }

    @FXML private void OutsourcedRadioEventHandler() throws IOException {
            newPartMachineID.setText("Company Name");
            newPartMachineInput.setPromptText("Company Name");
    }

    // Reload the main screen
    @FXML private void returnToMain() throws IOException {
        Stage stage;
        Parent root;
        stage=(Stage) partCancelButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
                "MainScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
}
