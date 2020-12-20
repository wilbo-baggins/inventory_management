package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class ModifyProductController implements Initializable {
    @FXML public GridPane modifyProductGridPane1, modifyProductGridPane2;
    @FXML public Label modifyProductIdLabel, modifyProductNameLabel, modifyProductInvLabel, modifyProductPriceLabel, modifyProductMaxLabel, modifyProductMinLabel;
    @FXML public TextField modifyProductIdInput, modifyProductNameInput, modifyProductInvInput, modifyProductPriceInput, modifyProductMaxInput, modifyProductMinInput;
    @FXML public TextField modifyProductSearchInput;
    @FXML public Button modifyProductAddButton, modifyProductDeleteButton, modifyProductCancelButton, modifyProductSaveButton, modifyProductSearchButton;
    @FXML public TableView modifyProductAddTable, modifyProductDeleteTable;
    @FXML public TableColumn modifyProductAddTableColumn1, modifyProductAddTableColumn2, modifyProductAddTableColumn3, modifyProductAddTableColumn4, modifyProductDeleteTableColumn1, modifyProductDeleteTableColumn2, modifyProductDeleteTableColumn3, modifyProductDeleteTableColumn4;
    private Product selectedProduct;
    private ObservableList<Part> selectedParts = FXCollections.observableArrayList();
    private ObservableList<Part> availableParts = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        modifyProductAddTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        modifyProductAddTableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductAddTableColumn1.prefWidthProperty().bind(modifyProductAddTable.widthProperty().multiply(0.25));
        modifyProductAddTableColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductAddTableColumn2.prefWidthProperty().bind(modifyProductAddTable.widthProperty().multiply(0.25));
        modifyProductAddTableColumn3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductAddTableColumn3.prefWidthProperty().bind(modifyProductAddTable.widthProperty().multiply(0.25));
        modifyProductAddTableColumn4.setCellValueFactory(new PropertyValueFactory<>("price"));
        modifyProductAddTableColumn4.prefWidthProperty().bind(modifyProductAddTable.widthProperty().multiply(0.25));
        // The currency solution was obtained from an online article: https://en.it1352.com/article/80f8a5a6d2d24e4787474a54d89d93ba.html.  I just made the small updates to match the values in my table.
        modifyProductAddTableColumn4.setCellFactory(tc -> new TableCell<Product, Double>() {
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
        modifyProductAddTable.setItems(availableParts);
        modifyProductAddTableColumn1.setSortType(TableColumn.SortType.ASCENDING);
        modifyProductAddTable.getSortOrder().add(modifyProductAddTableColumn1);

        modifyProductDeleteTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        modifyProductDeleteTableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyProductDeleteTableColumn1.prefWidthProperty().bind(modifyProductDeleteTable.widthProperty().multiply(0.25));
        modifyProductDeleteTableColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyProductDeleteTableColumn2.prefWidthProperty().bind(modifyProductDeleteTable.widthProperty().multiply(0.25));
        modifyProductDeleteTableColumn3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyProductDeleteTableColumn3.prefWidthProperty().bind(modifyProductDeleteTable.widthProperty().multiply(0.25));
        modifyProductDeleteTableColumn4.setCellValueFactory(new PropertyValueFactory<>("price"));
        modifyProductDeleteTableColumn4.prefWidthProperty().bind(modifyProductDeleteTable.widthProperty().multiply(0.25));
        modifyProductDeleteTableColumn4.setCellFactory(tc -> new TableCell<Product, Double>() {
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
        modifyProductDeleteTable.setItems(selectedParts);
        modifyProductDeleteTableColumn1.setSortType(TableColumn.SortType.ASCENDING);
        modifyProductDeleteTable.getSortOrder().add(modifyProductDeleteTableColumn1);

        modifyProductSearchInput.textProperty().addListener((obs, oldText, newText) -> {
            if (modifyProductSearchInput.getText().trim().equals("")) {
                modifyProductAddTable.setItems(availableParts);
            }
        });
    }

    // Adds selected parts to associated parts, removes the same parts from available parts list for a cleaner user experience
    @FXML private void AddAssociatedPartsEventHandler(ActionEvent event) throws IOException {
        ObservableList<Part> newSelections = FXCollections.observableArrayList(modifyProductAddTable.getSelectionModel().getSelectedItems());
        for (Part add: newSelections) {
            selectedParts.add(add);
        }
        modifyProductDeleteTable.setItems(selectedParts);
        for (Part remove: newSelections) {
            availableParts.remove(remove);
        }
        modifyProductAddTable.setItems(availableParts);
    }

    // remove associated parts handler, re-add removed parts to the available parts list
    @FXML private void DeleteAssociatedPartsHandler() {
        ObservableList<Part> removeParts = FXCollections.observableArrayList(modifyProductDeleteTable.getSelectionModel().getSelectedItems());
        for(Part part: removeParts) {
            selectedProduct.deleteAssociatedPart(part);
        }
        selectedParts = FXCollections.observableArrayList(selectedProduct.getAllAssociatedParts());
        modifyProductDeleteTable.setItems(selectedParts);
        for(Part part: removeParts) {
            availableParts.add(part);
        }
        modifyProductAddTable.setItems(availableParts);
    }

    // Handles search requests for available parts
    @FXML private void SearchAvailableProductsEventHandler(ActionEvent event) throws IOException {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList(BetterSearchMethods.addAssociatedPartSearch(availableParts, modifyProductSearchInput.getText().trim()));
        modifyProductAddTable.setItems(returnedParts);
    }


    // save product event handler
    @FXML private void SaveProductEventHandler(ActionEvent event) throws IOException {
        try {
            int id = Integer.valueOf(modifyProductIdInput.getText().trim());
            int stock = Integer.parseInt(modifyProductInvInput.getText().trim());
            int min = Integer.parseInt(modifyProductMinInput.getText().trim());
            int max = Integer.parseInt(modifyProductMaxInput.getText().trim());
            if (validateData()) {
                String name = modifyProductNameInput.getText().trim();
                double price = Double.valueOf(modifyProductPriceInput.getText().trim());
                for (Part part : selectedParts) {
                    try {
                        selectedProduct.getAllAssociatedParts().indexOf(part);
                    } catch (IndexOutOfBoundsException error) {
                        selectedProduct.addAssociatedPart(part);
                    }
                }
                selectedProduct.setId(id);
                selectedProduct.setName(name);
                selectedProduct.setPrice(price);
                selectedProduct.setStock(stock);
                selectedProduct.setMin(min);
                selectedProduct.setMax(max);
                returnToMain();
            }
        }
        catch(Exception error) {
            Alerts.newError(12);
        }
    }

    // Cancel modify task and return user to main screen
    @FXML private void CancelProductEventHandler(ActionEvent event) throws IOException {
        returnToMain();
    }

    @FXML private void returnToMain() throws IOException {
        Stage stage;
        Parent root;
        stage=(Stage) modifyProductCancelButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
                "MainScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Populates UI with correct data based on selected product
    public void setProduct(Product product) {
        selectedProduct = product;
        modifyProductIdInput.setText(String.valueOf(selectedProduct.getId()));
        modifyProductNameInput.setText(selectedProduct.getName());
        modifyProductPriceInput.setText(String.valueOf(selectedProduct.getPrice()));
        modifyProductInvInput.setText(String.valueOf(selectedProduct.getStock()));
        modifyProductMaxInput.setText(String.valueOf(selectedProduct.getMax()));
        modifyProductMinInput.setText(String.valueOf(selectedProduct.getMin()));
        selectedParts = FXCollections.observableArrayList(selectedProduct.getAllAssociatedParts());
        availableParts = FXCollections.observableArrayList(Inventory.getAllParts());
        modifyProductDeleteTable.setItems(selectedParts);
        for (Part currentPart : selectedParts) {
            availableParts.remove(currentPart);
        }
        modifyProductAddTable.setItems(availableParts);
    }

    // Get total cost of associated parts for data validation
    private double getPartsPriceTotal() {
        double totalPrice = 0;
        if(selectedParts.size() > 0) {
            for (Part part : selectedParts) {
                totalPrice = totalPrice + part.getPrice();
            }
        }
        return totalPrice;
    }

    // Validate user inputs, raise informative error messages
    private boolean validateData() throws IOException {
        try {
            int stock = Integer.valueOf(modifyProductInvInput.getText().trim());
            int min = Integer.valueOf(modifyProductMinInput.getText().trim());
            int max = Integer.valueOf(modifyProductMaxInput.getText().trim());
            double totalPrice = getPartsPriceTotal();
            if (modifyProductNameInput.getText().trim().isEmpty()) {
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
            else if (Double.valueOf(modifyProductPriceInput.getText().trim()) < totalPrice) {
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
}
