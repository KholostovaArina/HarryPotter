package com.mycompany.themagicshop;

public class Warehouse {
    private final int id;
    private final String type;
    private final String name;
    private final int quantity;
    private final int idSupply;

    public Warehouse(int id, String type, String name, int quantity, int idSupply) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.idSupply = idSupply;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public int getIdSupply() { return idSupply; }
}