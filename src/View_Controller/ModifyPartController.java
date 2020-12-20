package View_Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ModifyPartController implements Initializable {
    @FXML private RadioButton modifyPartInhouseRadio, modifyPartOutsourcedRadio;
    @FXML private Label modifyPartMachID;
    @FXML private TextField modifyPartIDInput, modifyPartNameInput, modifyPartInvInput, modifyPartPriceInput, modifyPartMaxInput, modifyPartMachIDInput, modifyPartMinInput;
    @FXML private Button modifyPartCancel;
    private Part currentPart;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Sets the on-screen text fields with the correct values based on the selected part
    public void setPart(Part selectedPart) {
       currentPart = selectedPart;
       modifyPartIDInput.setText(String.valueOf(selectedPart.getId()));
       modifyPartNameInput.setText(selectedPart.getName());
       modifyPartInvInput.setText(String.valueOf(selectedPart.getStock()));
       modifyPartPriceInput.setText(String.valueOf(selectedPart.getPrice()));
       modifyPartMinInput.setText(String.valueOf(selectedPart.getMin()));
       modifyPartMaxInput.setText(String.valueOf(selectedPart.getMax()));
       if(selectedPart instanceof Model.InHouse) {
           modifyPartMachIDInput.setText(String.valueOf(((InHouse) selectedPart).getMachineId()));
       }
       else if (selectedPart instanceof Model.Outsourced){
           modifyPartOutsourcedRadio.setSelected(true);
           modifyPartMachID.setText("Company Name");
           modifyPartMachIDInput.setText(String.valueOf(((Outsourced) selectedPart).getCompanyName()));
       }
    }

    // Validate data entered by the user, attempt to save, raise error code 12 if save fails
    @FXML private void ModifyPartSaveEventHandler(ActionEvent event) throws IOException {
        ObservableList<Part> tempList = Inventory.getAllParts();
        int currentPartIndex = tempList.indexOf(currentPart);
        if(validateData()) {
            if (currentPart instanceof InHouse && modifyPartOutsourcedRadio.isSelected()) {
                Inventory.deletePart(currentPart);
                Outsourced newPart = new Outsourced(Integer.valueOf(modifyPartIDInput.getText().trim()), modifyPartNameInput.getText().trim(), Double.valueOf(modifyPartPriceInput.getText().trim()), Integer.valueOf(modifyPartInvInput.getText().trim()), Integer.valueOf(modifyPartMaxInput.getText().trim()), Integer.valueOf(modifyPartMinInput.getText().trim()), modifyPartMachIDInput.getText().trim());
                Inventory.addPart(newPart);
            } else if (currentPart instanceof Outsourced && modifyPartInhouseRadio.isSelected()) {
                Inventory.deletePart(currentPart);
                InHouse newPart = new InHouse(Integer.valueOf(modifyPartIDInput.getText().trim()), modifyPartNameInput.getText().trim(), Double.valueOf(modifyPartPriceInput.getText().trim()), Integer.valueOf(modifyPartInvInput.getText().trim()), Integer.valueOf(modifyPartMinInput.getText().trim()), Integer.valueOf(modifyPartMaxInput.getText().trim()), Integer.valueOf(modifyPartMachIDInput.getText().trim()));
                Inventory.addPart(newPart);
            } else if (currentPart instanceof InHouse && modifyPartInhouseRadio.isSelected()) {
                InHouse newPart = new InHouse(Integer.valueOf(modifyPartIDInput.getText().trim()), modifyPartNameInput.getText().trim(), Double.valueOf(modifyPartPriceInput.getText().trim()), Integer.valueOf(modifyPartInvInput.getText().trim()), Integer.valueOf(modifyPartMinInput.getText().trim()), Integer.valueOf(modifyPartMaxInput.getText().trim()), Integer.valueOf(modifyPartMachIDInput.getText().trim()));
                Inventory.updatePart(currentPartIndex, newPart);

            } else if (currentPart instanceof Outsourced && modifyPartOutsourcedRadio.isSelected()) {
                Outsourced newPart = new Outsourced(Integer.valueOf(modifyPartIDInput.getText().trim()), modifyPartNameInput.getText().trim(), Double.valueOf(modifyPartPriceInput.getText().trim()), Integer.valueOf(modifyPartInvInput.getText().trim()), Integer.valueOf(modifyPartMinInput.getText().trim()), Integer.valueOf(modifyPartMaxInput.getText().trim()), modifyPartMachIDInput.getText().trim());
                Inventory.updatePart(currentPartIndex, newPart);
            } else {
                Alerts.newError(12);
            }
            returnToMain();
        }
    }

    // Handles click on "cancel" button, reloads main screen for user
    @FXML private void CancelModifyPartEventHandler(ActionEvent event) throws IOException {
        returnToMain();
    }

    // Handles click events on radio buttons
    @FXML private void ModifyInHouseRadioEventHandler(ActionEvent event) throws IOException {
        modifyPartMachID.setText("Machine ID");
    }

    @FXML private void ModifyOutsourcedRadioEventHandler() throws IOException {
        modifyPartMachID.setText("Company Name");
    }

    // Return user to main screen
    @FXML private void returnToMain() throws IOException {
        Stage stage;
        Parent root;
        stage=(Stage) modifyPartCancel.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
                "MainScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/View_Controller/inventoryStyles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Validate data entered by user, raise informative error messages
    private boolean validateData() {
        try {
            int stock = Integer.valueOf(modifyPartInvInput.getText().trim());
            int max = Integer.valueOf(modifyPartMaxInput.getText().trim());
            int min = Integer.valueOf(modifyPartMinInput.getText().trim());
            double price = Double.valueOf(modifyPartPriceInput.getText().trim());
            if (max <= min) {
                Alerts.newError(8);
                return false;
            } 
            else if (min < 1) {
                Alerts.newError(9);
                return false;
            } 
            else if (stock > max || stock < min) {
                Alerts.newError(6);
                return false;
            } 
            else if (modifyPartNameInput.getText().trim().isEmpty()) {
                Alerts.newError(7);
                return false;
            } 
            else if (price <= 0) {
                Alerts.newError(13);
                return false;
            } 
            else {
                return true;
            }
        } catch (Exception error) {
            Alerts.newError(5);
            return false;
        }
    }
}
