package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Compra;

import java.util.ArrayList;
import java.util.List;

public class CompraDAO implements Dao<Compra> {

    private List<Compra> purchases = new ArrayList<>();

    @Override
    public Compra get(int id) {
        return purchases.get(id);
    }

    @Override
    public List<Compra> getAll() {
        return purchases;
    }

    @Override
    public void save(Compra purchase) {
        purchases.add(purchase);
    }

    @Override
    public void update(Compra purchase, String[] params) {
        purchase.setIdClient(Integer.parseInt(params[0]));
        purchase.setIdItem(Integer.parseInt(params[1]));

        purchases.add(purchase);
    }

    @Override
    public void delete(Compra purchase) {
        purchases.remove(purchase);
    }
}
