package View_Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

// Search methods that are capable of returning multiple results that match user's search param.  Used in place of lookup methods in Inventory
public class BetterSearchMethods {
    public static ObservableList<Part> searchParts(String searchParam) {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        for(Part currentPart: Inventory.getAllParts()) {
            if (String.valueOf(currentPart.getId()) == searchParam || currentPart.getName().contains(searchParam)) {
                returnedParts.add(currentPart);
            }
        }
        return returnedParts;
    }

    public static ObservableList<Product> searchProducts(String searchParam) {
        ObservableList<Product> returnedProducts = FXCollections.observableArrayList();
        for(Product currentProduct: Inventory.getAllProducts()) {
            if (String.valueOf(currentProduct.getId()) == searchParam || currentProduct.getName().contains(searchParam)) {
                returnedProducts.add(currentProduct);
            }
        }
        return returnedProducts;
    }

    public static ObservableList<Part> addAssociatedPartSearch(ObservableList<Part> availableParts, String searchParam) {
        ObservableList<Part> returnedParts = FXCollections.observableArrayList();
        for(Part currentPart: availableParts) {
            if (String.valueOf(currentPart.getId()) == searchParam || currentPart.getName().contains(searchParam)) {
                returnedParts.add(currentPart);
            }
        }
        return returnedParts;
    }
}
