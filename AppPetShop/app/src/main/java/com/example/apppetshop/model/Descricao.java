package com.example.apppetshop.model;

public class Descricao {
    private int id;
    private int idProduto;
    private String attribute;
    private String value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String price) {
        this.value = price;
    }
}
