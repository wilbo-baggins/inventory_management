package Model;

import javafx.beans.property.*;

// Subclass of Part for parts not produced In-House
public class Outsourced extends Part{
    private final StringProperty companyName = new SimpleStringProperty();

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        setCompanyName(companyName);
    }

    public void setCompanyName(String cName) { companyName.set(cName); }
    public String getCompanyName() { return companyName.get(); }
}
