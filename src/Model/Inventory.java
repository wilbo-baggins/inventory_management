package Model;

import View_Controller.ModifyPartController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    // Methods to add items to inventory and update them as needed
    public static void addPart(Part part) {
        allParts.add(part);
    }

    public static void addProduct(Product product) { allProducts.add(product);}

    public static void updatePart(int index, Part selectedPart) {
        allParts.get(index).setId(selectedPart.getId());
        allParts.get(index).setName(selectedPart.getName());
        allParts.get(index).setPrice(selectedPart.getPrice());
        allParts.get(index).setStock(selectedPart.getStock());
        allParts.get(index).setMax(selectedPart.getMax());
        allParts.get(index).setMin(selectedPart.getMin());
        if(selectedPart instanceof InHouse) {
            ((InHouse) allParts.get(index)).setMachineID(((InHouse) selectedPart).getMachineId());
        }
        else {
            ((Outsourced) allParts.get(index)).setCompanyName(((Outsourced) selectedPart).getCompanyName());
        }
    }
    // Method to modify existing products.  I opted to use the Product setters directly within the controller
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.get(index).setId(selectedProduct.getId());
        allProducts.get(index).setName(selectedProduct.getName());
        allProducts.get(index).setStock(selectedProduct.getStock());
        allProducts.get(index).setMax(selectedProduct.getMax());
        allProducts.get(index).setMin(selectedProduct.getMin());
    }

    // Methods to lookup items based on name or ID.  Due to the limitation of returning only one part or product per search, I replaced these methods with the BetterSearchMethods class in the View_Controller package
    public static Part lookupPart(int partId) {
        Part nullPart = null;
        Part currentPart;
        try {
            for (int i = 0; i < getAllParts().size(); i++) {
                currentPart = getAllParts().get(i);
                if (currentPart.getId() == partId) {
                    return currentPart;
                }
            }
        }
        catch (Exception error) {
        }
        return nullPart;
    }

    public static Product lookupProduct(int productId) {
        Product nullProduct = null;
        Product currentProduct;
        try {
            for (int i = 0; i < getAllProducts().size(); i++) {
                currentProduct = getAllProducts().get(i);
                if (currentProduct.getId() == productId) {
                    return currentProduct;
                }
            }
        }
        catch (Exception error) {
        }
        return nullProduct;
    }

    public static Part lookupPart(String partName) {
        Part nullPart = null;
        for (Part currentPart: getAllParts()) {
            if(currentPart.getName().contains(partName.trim())){
                return currentPart;
            }
        }
        return nullPart;
    }

    public static Product lookupProduct(String productName) {
        Product nullProduct = null;
        for (Product currentProduct: getAllProducts()) {
            if(currentProduct.getName().contains(productName.trim())){
                return currentProduct;
            }
        }
        return nullProduct;
    }

    // Methods to delete items from inventory
    public static boolean deletePart(Part selectedPart) {
        try {
            allParts.remove(allParts.indexOf(selectedPart));
        } catch (IndexOutOfBoundsException error) {
            return false;
        }
        if (allParts.indexOf(selectedPart) == -1) {
            return true;
        }
        else {
            return false;
        }
    }

    public static boolean deleteProduct(Product selectedProduct) {
        try {
            allProducts.remove(selectedProduct);
        } catch (IndexOutOfBoundsException error) {

        }
        if (allProducts.indexOf(selectedProduct) == -1) {
            return true;
        }
        else {
            return false;
        }
    }

    // Methods to make the part and product lists available to other classes
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    public static ObservableList<Product> getAllProducts() { return allProducts; }
}
