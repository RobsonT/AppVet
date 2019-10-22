package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Compra;

import java.util.ArrayList;
import java.util.List;

public class CompraDAO implements Dao<Compra> {

    private List<Compra> compras;

    private static CompraDAO instance;

    private CompraDAO() {
        compras = new ArrayList<>();
    }

    public static CompraDAO getInstance(){
        if(instance == null){
            instance = new CompraDAO();
        }

        return instance;
    }

    @Override
    public Compra get(int id) {
        return compras.get(id);
    }

    @Override
    public List<Compra> getAll() {
        return compras;
    }

    public Compra getUnconfirmed(int id) {
        for (Compra c: compras) {
            if(c.getIdCliente() == id && !c.isConfirmado())
                return c;
        }
        return null;
    }

    @Override
    public void save(Compra purchase) {
        compras.add(purchase);
    }

    @Override
    public void delete(Compra purchase) {
        compras.remove(purchase);
    }
}
