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
        for (int i = 0; i< itens.size(); i++) {
            if(itens.get(i).getIdProduto() == item.getIdProduto() && itens.get(i).getIdCompra() == item.getIdCompra()) {
                itens.remove(item);
            }
        }
    }

    public List<Item> getByCompra(int id){
        List<Item> newItens = new ArrayList<>();
        for (Item i: itens) {
            if (i.getIdCompra() == id){
                newItens.add(i);
            }
        }
        return newItens;
    }

    public void updateQuantity(Item item){
        for (int i = 0; i < itens.size(); i++){
            if(itens.get(i).getIdCompra() == item.getIdCompra() && itens.get(i).getIdProduto() == item.getIdProduto()){
                itens.set(i, item);
            }
        }
    }
}
