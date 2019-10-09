package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteDAO implements Dao<Cliente>{
    private List<Cliente> clients;
    private static ClienteDAO instance;

    private ClienteDAO() {
        clients = new ArrayList<>();
    }

    public static ClienteDAO getInstance(){
        if(instance == null){
            instance = new ClienteDAO();
        }

        return instance;
    }

    @Override
    public Cliente get(int id) {
        return clients.get(id);
    }

    @Override
    public List<Cliente> getAll() {
        return clients;
    }

    @Override
    public void save(Cliente client) {
        clients.add(client);
    }

    @Override
    public void delete(Cliente client) {
        clients.remove(client);
    }

    public Cliente getByEmail(String email) {
        for (Cliente c : clients) {
            if (c.getEmail().equals(email)) {
                return c;
            }
        }
        return null;
    }
}