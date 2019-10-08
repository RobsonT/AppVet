package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Cliente;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ClienteDAO implements Dao<Cliente>{
    private List<Cliente> clients = new ArrayList<>();

    public ClienteDAO() {
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
    public void update(Cliente client, String[] params) {
        client.setName(params[0]);
        client.setCpf(params[1]);
        client.setEmail(params[2]);
        client.setPassword(params[3]);

        clients.add(client);
    }

    @Override
    public void delete(Cliente client) {
        clients.remove(client);
    }
}