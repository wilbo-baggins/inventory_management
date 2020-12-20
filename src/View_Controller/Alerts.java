package View_Controller;

import javafx.scene.control.Alert;

// Method to create alert window with informative error message if validation fails
public class Alerts {
    public static void newError(int code) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        switch(code) {
            case 1: {
                alert.setContentText("A part must be selected before it can be modified.");
                break;
            }
            case 2: {
                alert.setContentText("A part must be selected before it can be deleted.");
                break;
            }
            case 3: {
                alert.setContentText("A product must be selected before it can be modified.");
                break;
            }
            case 4: {
                alert.setContentText("A product must be selected before it can be deleted.");
                break;
            }
            case 5: {
                alert.setContentText("Invalid data type detected.");
                break;
            }
            case 6: {
                alert.setContentText("Invalid stock level entered.");
                break;
            }
            case 7: {
                alert.setContentText("Each part or product must have a name.");
                break;
            }
            case 8: {
                alert.setContentText("Max stock value must be greater than Min.");
                break;
            }
            case 9: {
                alert.setContentText("Min stock value must be greater than 0.");
                break;
            }
            case 10: {
                alert.setContentText("A product must have at least one associated part.");
                break;
            }
            case 11: {
                alert.setContentText("Product price must be higher than the sum of associated parts.");
                break;
            }
            case 12: {
                alert.setContentText("You done messed up A-a-ron!");
                break;
            }
            case 13: {
                alert.setContentText("The price must be higher than 0.  This isn't a charity!");
            }
        }
        alert.showAndWait();
    }
}
