package com.example.apppetshop.model;

import java.util.Date;

public class ServicoCliente {
    private int id;
    private int idClient;
    private int idService;
    private Date time;

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

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
