package Model;

import javafx.beans.property.*;
import javafx.collections.*;

import java.util.List;

public class Product {
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty();
    private DoubleProperty price = new SimpleDoubleProperty();
    private IntegerProperty stock = new SimpleIntegerProperty();
    private IntegerProperty max = new SimpleIntegerProperty();
    private IntegerProperty min = new SimpleIntegerProperty();

    public Product(int id, String name, double price, int stock, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }

    // Methods to allow access to individual attributes
    public ObservableList<Part> getAllAssociatedParts() { return associatedParts; };
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public double getPrice() {return price.get(); }
    public int getStock() { return stock.get(); }
    public int getMin() { return min.get(); }
    public int getMax() { return max.get(); }

    // Methods to modify attributes
    public void setId(int i) { id.set(i); }
    public void setName(String n) { name.set(n); }
    public void setPrice(double p) { price.set(p); }
    public void setStock(int s) { stock.set(s); }
    public void setMin(int mi) { min.set(mi); }
    public void setMax(int ma) { max.set(ma); }
    public void addAssociatedPart(Part part) { associatedParts.add(part); }

    // Function to delete object from associatedParts
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        try {associatedParts.remove(selectedAssociatedPart);
            return true;
        } catch (Exception error) {
            return false;
        }
    }
}