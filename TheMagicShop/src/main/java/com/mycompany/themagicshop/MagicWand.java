package com.mycompany.themagicshop;

import java.time.LocalDate;

public class MagicWand {
    private final int id;
    private final String corpus;
    private final String core;
    private String status;
    private String ownerName;
    private LocalDate purchaseDate;

    public MagicWand(int id, String corpus, String core, String status, 
                    String ownerName, LocalDate purchaseDate) {
        
        this.id = id;
        this.corpus = corpus;
        this.core = core;
        this.status = status;
        this.ownerName = ownerName;
        this.purchaseDate = purchaseDate;
    }

    public int getId() { return id; }
    public String getCorpus() { return corpus; }
    public String getCore() { return core; }
    public String getStatus() { return status; }
    public String getOwnerName() { return ownerName; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
    
    public void setStatus(String status) { this.status = status; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    @Override
    public String toString() {
        return String.format(
            "MagicWand [id=%d, corpus=%s, core=%s, status=%s, owner=%s, purchaseDate=%s]",
            id, corpus, core, status, ownerName, purchaseDate
        );
    }
}
