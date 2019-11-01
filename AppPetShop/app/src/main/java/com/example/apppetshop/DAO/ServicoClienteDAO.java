package com.example.apppetshop.DAO;

import com.example.apppetshop.model.ServicoCliente;
import java.util.ArrayList;
import java.util.List;


public class ServicoClienteDAO implements Dao<ServicoCliente>{

    private List<ServicoCliente> services = new ArrayList<>();
    private static ServicoClienteDAO instance;

    private ServicoClienteDAO() {}

    public static ServicoClienteDAO getInstance(){
        if(instance == null){
            instance = new ServicoClienteDAO();
        }
        return instance;
    }

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

    public List<ServicoCliente> getByClient(int id){
        List<ServicoCliente> newServices = new ArrayList<>();
        for (ServicoCliente sc: services) {
            if(sc.getIdCliente() == id){
                newServices.add(sc);
            }
        }
        return newServices;
    }
}