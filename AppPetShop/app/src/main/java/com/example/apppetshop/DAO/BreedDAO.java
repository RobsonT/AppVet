package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Raca;

import java.util.ArrayList;
import java.util.List;

public class BreedDAO implements Dao<Raca>{

    private List<Raca> breeds = new ArrayList<>();

    @Override
    public Raca get(int id) {
        return breeds.get(id);
    }

    @Override
    public List<Raca> getAll() {
        return breeds;
    }

    @Override
    public void save(Raca breed) {
        breeds.add(breed);
    }

    @Override
    public void delete(Raca breed) {
        breeds.remove(breed);
    }
}
