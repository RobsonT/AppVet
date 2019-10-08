package com.example.apppetshop.model;

public class Compra {
    private int id;
    private int idClient;
    private int idItem;
    private boolean confirmed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }
}
