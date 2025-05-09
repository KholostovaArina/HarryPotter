package com.mycompany.themagicshop;

import java.time.LocalDate;

public class Supply {
    private final int id;
    private final LocalDate date;
    private boolean inWarehouse;

    public Supply(int id, LocalDate date, boolean inWarehouse) {
        this.id = id;
        this.date = date;
        this.inWarehouse = inWarehouse;
    }

    public int getId() { return id; }
    public LocalDate getDate() { return date; }
    public boolean getInWarehouse() { return inWarehouse; }
    
    public void setInWarehouse(boolean inWarehouse) { this.inWarehouse = inWarehouse;}

}
