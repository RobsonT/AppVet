package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Descricao;

import java.util.ArrayList;
import java.util.List;

public class DescritionDAO implements Dao<Descricao> {

    private List<Descricao> desscritions = new ArrayList<>();

    @Override
    public Descricao get(int id) {
        return desscritions.get(id);
    }

    @Override
    public List<Descricao> getAll() {
        return desscritions;
    }

    @Override
    public void save(Descricao descrition) {
        desscritions.add(descrition);
    }

    @Override
    public void delete(Descricao descricao) {
        desscritions.remove(descricao);
    }
}
