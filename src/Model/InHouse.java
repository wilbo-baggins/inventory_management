package Model;

import javafx.beans.property.*;

// Subclass of Part for parts created In-House
public class InHouse extends Part{
    private final IntegerProperty machineId = new SimpleIntegerProperty();

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        setMachineID(machineId);
    }

    public void setMachineID(int machId) {machineId.set(machId);}
    public int getMachineId() {return machineId.get();}
}