package View_Controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static int partIdCounter = 0;
    static int productIdCounter = 0;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("inventoryStyles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static int newPartIdGenerator() {
        partIdCounter++;
        return partIdCounter;
    }

    public static int newProductIdGenerator() {
        productIdCounter++;
        return productIdCounter;
    }

    // ready, steady, go!
    public static void main(String[] args) {
        launch(args);
    }
}

