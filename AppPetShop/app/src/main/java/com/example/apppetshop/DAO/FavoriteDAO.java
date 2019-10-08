package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Favorito;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO implements Dao<Favorito> {

    private List<Favorito> favorites = new ArrayList<>();

    @Override
    public Favorito get(int id) {
        return favorites.get(id);
    }

    @Override
    public List<Favorito> getAll() {
        return favorites;
    }

    @Override
    public void save(Favorito favorito) {
        favorites.add(favorito);
    }

    @Override
    public void delete(Favorito favorito) {
        favorites.remove(favorito);
    }
}
