package com.example.apppetshop.DAO;

import com.example.apppetshop.model.ServicoCliente;

import java.util.ArrayList;
import java.util.List;

public class ServiceClientDAO implements Dao<ServicoCliente>{

    private List<ServicoCliente> services = new ArrayList<>();

    @Override
    public ServicoCliente get(int id) {
        return services.get(id);
    }

    @Override
    public List<ServicoCliente> getAll() {
        return services;
    }

    @Override
    public void save(ServicoCliente serviceClient) {
        services.add(serviceClient);
    }

    @Override
    public void delete(ServicoCliente serviceClient) {
        services.remove(serviceClient);
    }
}
