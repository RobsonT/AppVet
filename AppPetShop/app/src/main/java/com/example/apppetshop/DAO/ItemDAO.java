package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO implements Dao<Item>{

    private List<Item> items = new ArrayList<>();


    @Override
    public Item get(int id) {
        return items.get(id);
    }

    @Override
    public List<Item> getAll() {
        return items;
    }

    @Override
    public void save(Item item) {
        items.add(item);
    }

    @Override
    public void delete(Item item) {
        items.remove(item);
    }
}
