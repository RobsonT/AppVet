package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Descricao;

import java.util.ArrayList;
import java.util.List;

public class DescricaoDAO implements Dao<Descricao> {

    private List<Descricao> descritions;

    private static DescricaoDAO instance;

    private DescricaoDAO() {
        descritions = new ArrayList<>();
    }

    public static DescricaoDAO getInstance(){
        if(instance == null){
            instance = new DescricaoDAO();
        }

        return instance;
    }

    @Override
    public Descricao get(int id) {
        return descritions.get(id);
    }

    @Override
    public List<Descricao> getAll() {
        return descritions;
    }

    public List<Descricao> getByProduct(int id) {
        List<Descricao> desc = new ArrayList<>();
        for (Descricao d: descritions) {
            if(d.getIdProduto() == id){
                desc.add(d);
            }
        }
        return desc;
    }

    @Override
    public void save(Descricao descrition) {
        descritions.add(descrition);
    }

    @Override
    public void delete(Descricao descricao) {
        descritions.remove(descricao);
    }
}
