package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements Dao<Produto>{

    private List<Produto> products = new ArrayList<>();

    @Override
    public Produto get(int id) {
        return products.get(id);
    }

    @Override
    public List<Produto> getAll() {
        return products;
    }

    @Override
    public void save(Produto produto) {
        products.add(produto);
    }

    @Override
    public void delete(Produto produto) {
        products.remove(produto);
    }
}
