package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Servico;

import java.util.ArrayList;
import java.util.List;

public class ServicoDAO implements Dao<Servico>{

    private List<Servico> services = new ArrayList<>();


    @Override
    public Servico get(int id) {
        return services.get(id);
    }

    @Override
    public List<Servico> getAll() {
        return services;
    }

    @Override
    public void save(Servico service) {
        services.add(service);
    }

    @Override
    public void delete(Servico service) {
        services.remove(service);
    }
}
