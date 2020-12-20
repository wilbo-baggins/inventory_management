package View_Controller;

import java.net.URL;
import Model.*;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML private TableColumn partsTableColumn1, partsTableColumn2, partsTableColumn3, partsTableColumn4, productsTableColumn1, productsTableColumn2, productsTableColumn3, productsTableColumn4;
    @FXML private TableView partsTable, productsTable;
    @FXML private GridPane partsPane, productsPane;
    @FXML private Button quitButton, modifyPartButton, addPartButton;
    @FXML private TextField partsSearchField, productSearchField;
    @FXML private AnchorPane aroot;

    // Initializes the controller
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // set width of part and product panes relative to the anchor pane
        partsPane.prefWidthProperty().bind(aroot.widthProperty().multiply(0.45));
        productsPane.prefWidthProperty().bind(aroot.widthProperty().multiply(0.45));

        // format and populate tables
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
        partsTableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsTableColumn1.prefWidthProperty().bind(partsTable.widthProperty().multiply(0.25));
        partsTableColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsTableColumn2.prefWidthProperty().bind(partsTable.widthProperty().multiply(0.25));
        partsTableColumn3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsTableColumn3.prefWidthProperty().bind(partsTable.widthProperty().multiply(0.25));
        partsTableColumn4.setCellValueFactory(new PropertyValueFactory<>("price"));
        partsTableColumn4.prefWidthProperty().bind(partsTable.widthProperty().multiply(0.25));
        // The currency solution was obtained from an online article: https://en.it1352.com/article/80f8a5a6d2d24e4787474a54d89d93ba.html.  I just made the small updates to match the values in my table.
        partsTableColumn4.setCellFactory(tc -> new TableCell<Product, Double>() {
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
        partsTable.setItems(Inventory.getAllParts());
        partsTableColumn1.setSortType(TableColumn.SortType.ASCENDING);
        partsTable.getSortOrder().add(partsTableColumn1);

        productsTableColumn1.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsTableColumn1.prefWidthProperty().bind(productsTable.widthProperty().multiply(0.25));
        productsTableColumn2.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsTableColumn2.prefWidthProperty().bind(productsTable.widthProperty().multiply(0.25));
        productsTableColumn3.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsTableColumn3.prefWidthProperty().bind(productsTable.widthProperty().multiply(0.25));
        productsTableColumn4.setCellValueFactory(new PropertyValueFactory<>("price"));
        productsTableColumn4.prefWidthProperty().bind(productsTable.widthProperty().multiply(0.25));
        productsTableColumn4.setCellFactory(tc -> new TableCell<Product, Double>() {
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
        productsTable.setItems(Inventory.getAllProducts());
        productsTableColumn1.setSortType(TableColumn.SortType.ASCENDING);
        productsTable.getSortOrder().add(productsTableColumn1);


        // add event listeners to reset tables if search text is cleared
        partsSearchField.textProperty().addListener((obs, oldText, newText) -> {
            if (partsSearchField.getText().trim().equals("")) {
                partsTable.setItems(Inventory.getAllParts());
            }
        });

        productSearchField.textProperty().addListener((obs, oldText, newText) -> {
            if (productSearchField.getText().trim().equals("")) {
                productsTable.setItems(Inventory.getAllProducts());
            }
        });
    }

//**********************************************************************************************************
    // Part logic
//**********************************************************************************************************

    // Launches the Add Part screen
    @FXML private void AddPartScreenHandler(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage=(Stage) addPartButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
                "AddPartScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    // Launches the Modify Part screen
    @FXML private void ModifyPartScreenHandler(ActionEvent event) throws IOException {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
        if(selectedPart == null) {
            Alerts.newError(1);
        }
        else {
            Stage stage;
            Parent root;
            stage=(Stage) modifyPartButton.getScene().getWindow();
            //load up OTHER FXML document
            FXMLLoader loader=new FXMLLoader(getClass().getResource(
                    "ModifyPartScreen.fxml"));
            root =loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
            ModifyPartController controller = loader.getController();
            controller.setPart(selectedPart);
        }
    }

    // Handles the Delete Part button event
    @FXML private void DeletePartEventHandler() throws IOException {
        Part selectedPart = (Part) partsTable.getSelectionModel().getSelectedItem();
            if(selectedPart == null) {
                Alerts.newError(2);
            }
            else {
                Boolean isDeleted = Inventory.deletePart(selectedPart);
            }
    }

    // Handes the Search Part button event, and swaps the Parts table data to show only search results
    @FXML private void SearchPartEventHandler() throws IOException {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList(BetterSearchMethods.searchParts(partsSearchField.getText().trim()));
        partsTable.setItems(returnedParts);
    }

 //**********************************************************************************************************
    //  Product Logic
 //**********************************************************************************************************

    // Launches the Add Product screen
    @FXML private void AddProductScreenHandler(ActionEvent event) throws IOException {
        Stage stage;
        Parent root;
        stage=(Stage) addPartButton.getScene().getWindow();
        //load up OTHER FXML document
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
                "AddProductScreen.fxml"));
        root =loader.load();
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
        stage.setScene(scene);
        stage.show();

    }

    // Launches the Modify Product screen
    @FXML private void ModifyProductScreenHandler(ActionEvent event) throws IOException {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null) {
            Alerts.newError(3);
        }
        else {
            Stage stage;
            Parent root;
            stage = (Stage) addPartButton.getScene().getWindow();
            //load up OTHER FXML document
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "ModifyProductScreen.fxml"));
            root = loader.load();
            ModifyProductController controller = loader.getController();
            controller.setProduct(selectedProduct);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        }
    }

    // Handles the Search Product button event, and swaps Products table data to show only search results
    @FXML private void SearchProductEventHandler(ActionEvent event) throws IOException {
        ObservableList<Product> returnedProducts = FXCollections.observableArrayList(BetterSearchMethods.searchProducts(productSearchField.getText().trim()));
        productsTable.setItems(returnedProducts);
    }

    // Deletes the selected product
    @FXML private void DeleteProductEventHandler(ActionEvent event) throws IOException {
        Product selectedProduct = (Product) productsTable.getSelectionModel().getSelectedItem();
        if(selectedProduct == null) {
            Alerts.newError(4);
        }
        else {
            Boolean isDeleted = Inventory.deleteProduct(selectedProduct);
            System.out.println(isDeleted);
        }

    }

    @FXML private void CloseApp(ActionEvent event) {
        Stage stage = (Stage) quitButton.getScene().getWindow();
        stage.close();
    }
}