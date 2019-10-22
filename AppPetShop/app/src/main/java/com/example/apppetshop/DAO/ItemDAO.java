package com.example.apppetshop.DAO;

import com.example.apppetshop.R;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements Dao<Item>{

    private List<Item> itens;

    private static ItemDAO instance;

    private ItemDAO() {
        itens = new ArrayList<>();
    }

    public static ItemDAO getInstance(){
        if(instance == null){
            instance = new ItemDAO();
        }

        return instance;
    }


    @Override
    public Item get(int id) {
        return itens.get(id);
    }

    @Override
    public List<Item> getAll() {
        return itens;
    }

    @Override
    public void save(Item item) {
        itens.add(item);
    }

    @Override
    public void delete(Item item) {
        itens.remove(item);
    }
}
