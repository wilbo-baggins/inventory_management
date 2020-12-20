package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class AddProductController implements Initializable {
    @FXML private TextField addProductNameInput, addProductInvInput, addProductPriceInput, addProductMaxInput, addProductMinInput, productSearchInput;
    @FXML private Button productAddSearchButton, productAddCancelButton;
    @FXML private TableView addProductAddTable, addProductDeleteTable;
    @FXML private TableColumn addProductAddTableColumn1, addProductAddTableColumn2, addProductAddTableColumn3, addProductAddTableColumn4, addProductDeleteTableColumn1, addProductDeleteTableColumn2, addProductDeleteTableColumn3, addProductDeleteTableColumn4;
    private ObservableList<Part> selectedParts = FXCollections.observableArrayList();
    private ObservableList<Part> localPartsList = FXCollections.observableArrayList();

    // Initializes the controller
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for(int i = 0; i < Inventory.getAllParts().size(); i++) {
            localPartsList.add(Inventory.getAllParts().get(i));
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        addProductAddTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addProductAddTableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductAddTableColumn1.prefWidthProperty().bind(addProductAddTable.widthProperty().multiply(0.25));
        addProductAddTableColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductAddTableColumn2.prefWidthProperty().bind(addProductAddTable.widthProperty().multiply(0.25));
        addProductAddTableColumn3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductAddTableColumn3.prefWidthProperty().bind(addProductAddTable.widthProperty().multiply(0.25));
        addProductAddTableColumn4.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductAddTableColumn4.prefWidthProperty().bind(addProductAddTable.widthProperty().multiply(0.25));
        // The currency solution was obtained from an online article: https://en.it1352.com/article/80f8a5a6d2d24e4787474a54d89d93ba.html.  I just made the small updates to match the values in my table.
        addProductAddTableColumn4.setCellFactory(tc -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        addProductAddTable.setItems(localPartsList);
        addProductAddTableColumn1.setSortType(TableColumn.SortType.ASCENDING);
        addProductAddTable.getSortOrder().add(addProductAddTableColumn1);

        addProductDeleteTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        addProductDeleteTableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        addProductDeleteTableColumn1.prefWidthProperty().bind(addProductDeleteTable.widthProperty().multiply(0.25));
        addProductDeleteTableColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductDeleteTableColumn2.prefWidthProperty().bind(addProductDeleteTable.widthProperty().multiply(0.25));
        addProductDeleteTableColumn3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addProductDeleteTableColumn3.prefWidthProperty().bind(addProductDeleteTable.widthProperty().multiply(0.25));
        addProductDeleteTableColumn4.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProductDeleteTableColumn4.prefWidthProperty().bind(addProductDeleteTable.widthProperty().multiply(0.25));
        addProductDeleteTableColumn4.setCellFactory(tc -> new TableCell<Product, Double>() {
            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(currencyFormat.format(price));
                }
            }
        });
        addProductDeleteTable.setItems(selectedParts);
        addProductDeleteTableColumn1.setSortType(TableColumn.SortType.ASCENDING);
        addProductDeleteTable.getSortOrder().add(addProductDeleteTableColumn1);

        productSearchInput.textProperty().addListener((obs, oldText, newText) -> {
            if (productSearchInput.getText().trim().equals("")) {
                addProductAddTable.setItems(localPartsList);
            }
        });
    }

    @FXML private boolean validateData() throws IOException {
        try {
            int stock = Integer.valueOf(addProductInvInput.getText().trim());
            int min = Integer.valueOf(addProductMinInput.getText().trim());
            int max = Integer.valueOf(addProductMaxInput.getText().trim());
            double totalPrice = getPartsPriceTotal();
            if (addProductNameInput.getText().trim().isEmpty()) {
                Alerts.newError(7);
                return false;
            }
            else if (max < min) {
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
            else if (Double.valueOf(addProductPriceInput.getText().trim()) < totalPrice) {
                Alerts.newError(11);
                return false;
            }
            else if (selectedParts.size() == 0) {
                Alerts.newError(10);
                return false;
            }
            else {
                return true;
            }
        }
        catch (Exception error) {
            Alerts.newError(5);
            return false;
        }
    }

    // Adds the selected part to the Associated Parts table for the new product and removes it from Available Parts
    @FXML private void AddAssociatedPartsEventHandler(ActionEvent event) throws IOException {
        ObservableList<Part> newSelections = FXCollections.observableArrayList(addProductAddTable.getSelectionModel().getSelectedItems());
        for (Part add: newSelections) {
            selectedParts.add(add);
        }
        addProductDeleteTable.setItems(selectedParts);
        for (Part remove: newSelections) {
            localPartsList.remove(remove);
        }
        if(productSearchInput.getText().trim().equals("")){
            addProductAddTable.setItems(localPartsList);
        }
        else {
            productAddSearchButton.fire();
        }

    }

    // Removes the selected part from the Associated Parts table for the new product and returns it to Available Parts
    @FXML private void DeleteAssociatedPartsEventHandler(ActionEvent event) throws IOException {
        ObservableList<Part> newDeletions = FXCollections.observableArrayList(addProductDeleteTable.getSelectionModel().getSelectedItems());
        for (Part remove: newDeletions) {
            selectedParts.remove(remove);
        }
        addProductDeleteTable.setItems(selectedParts);
        for (Part add: newDeletions) {
            localPartsList.add(add);
        }
        if(productSearchInput.getText().trim().equals("")){
            addProductAddTable.setItems(localPartsList);
        }
        else {
            productAddSearchButton.fire();
        }
    }

    // Handles search requests for available parts
    @FXML private void SearchAvailableProductsEventHandler(ActionEvent event) throws IOException {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList(BetterSearchMethods.addAssociatedPartSearch(localPartsList, productSearchInput.getText().trim()));
        addProductAddTable.setItems(returnedParts);
    }

    // Save button event handler.  Validate data, attempt to save, raise error code 12 if unknown exception is caught
    @FXML private void SaveProductEventHandler(ActionEvent event) throws IOException {
        try {
            if (validateData()) {
                int stock = Integer.valueOf(addProductInvInput.getText().trim());
                int min = Integer.valueOf(addProductMinInput.getText().trim());
                int max = Integer.valueOf(addProductMaxInput.getText().trim());
                double price = Double.valueOf(addProductPriceInput.getText().trim());
                String name = addProductNameInput.getText().trim();
                int id = Main.newProductIdGenerator();
                Product newProduct = new Model.Product(id, name, price, stock, min, max);
                for (Part part : selectedParts) {
                    newProduct.addAssociatedPart(part);
                }
                Model.Inventory.addProduct(newProduct);
                returnToMain();
            }
        } catch (Exception error) {
            Alerts.newError(12);
        }
    }

    // Cancel button event handler
    @FXML private void CancelProductEventHandler(ActionEvent event) throws IOException {
        addProductNameInput.clear();
        addProductPriceInput.clear();
        addProductInvInput.clear();
        addProductMaxInput.clear();
        addProductMinInput.clear();
        returnToMain();
    }

    // Returns the user to the main screen
    @FXML private void returnToMain() throws IOException {
        Stage stage;
        Parent root;
        stage=(Stage) productAddCancelButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
                "MainScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    private double getPartsPriceTotal() {
        double totalPrice = 0;
        if(selectedParts.size() > 0) {
            for (Part part : selectedParts) {
                totalPrice = totalPrice + part.getPrice();
            }
        }
        return totalPrice;
    }

    public Part lookupPart(int partId) {
        Part nullPart = null;
        try {
            for (Part currentPart: localPartsList) {
                if (currentPart.getId() == partId) {
                    return currentPart;
                }
            }
        }
        catch (Exception error) {
        }
        return nullPart;
    }

    public Part lookupPart(String partName) {
        Part nullPart = null;
        for (Part currentPart: localPartsList) {
            if(currentPart.getName().contains(partName.trim())){
                return currentPart;
            }
        }
        return nullPart;
    }
}
