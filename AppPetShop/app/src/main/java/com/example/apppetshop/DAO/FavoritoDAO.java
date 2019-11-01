package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Favorito;
import java.util.ArrayList;
import java.util.List;


public class FavoritoDAO implements Dao<Favorito> {

    private List<Favorito> favorites;

    private static FavoritoDAO instance;

    private FavoritoDAO() {
        favorites = new ArrayList<>();
    }


    public static FavoritoDAO getInstance() {
        if (instance == null) {
            instance = new FavoritoDAO();
        }
        return instance;
    }


    @Override
    public Favorito get(int id) {
        return favorites.get(id);
    }

    @Override
    public List<Favorito> getAll() {
        return favorites;
    }

    public List<Favorito> getByClient(int id) {
        List<Favorito> fav = new ArrayList<>();
        for (Favorito f : favorites) {
            if (f.getIdCliente() == id)
                fav.add(f);
        }
        return fav;
    }

    @Override
    public void save(Favorito favorito) {
        favorites.add(favorito);
    }

    @Override
    public void delete(Favorito favorito) {
        int n = -1;
        for (Favorito f: favorites){
            n++;
            if(f.getIdProduto() == favorito.getIdProduto() && f.getIdCliente() == favorito.getIdCliente()){
                favorites.remove(n);
            }
        }
    }
}