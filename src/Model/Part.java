package Model;

import javafx.beans.property.*;

// Part superclass, abstract, parts must be created as In-House or Outsourced
public abstract class Part {
    private IntegerProperty id = new SimpleIntegerProperty();
    private StringProperty name = new SimpleStringProperty("");
    private DoubleProperty price = new SimpleDoubleProperty();
    private IntegerProperty stock = new SimpleIntegerProperty();
    private IntegerProperty min = new SimpleIntegerProperty();
    private IntegerProperty max = new SimpleIntegerProperty();

    // Constructor
    public Part(int id, String name, double price, int stock, int min, int max) {
        setId(id);
        setName(name);
        setPrice(price);
        setStock(stock);
        setMin(min);
        setMax(max);
    }

    // Methods that allow access to individual attributes
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public double getPrice() { return price.get(); }
    public int getStock() { return stock.get(); }
    public int getMin() { return min.get(); }
    public int getMax() { return max.get(); }

    // Setter methods to allow the modification of individual attributes
    public void setId(int i) {id.set(i);}
    public void setName(String n) {name.set(n);}
    public void setPrice(double p) {price.set(p);}
    public void setStock(int s) {stock.set(s);}
    public void setMin(int mi) {min.set(mi);}
    public void setMax(int ma) {max.set(ma);}
}