package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Descricao;
import java.util.ArrayList;
import java.util.List;


public class DescricaoDAO {

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


    public Descricao get(int id) {
        return descritions.get(id);
    }


    public List<Descricao> getAll() {
        return descritions;
    }


    public void save(Descricao descrition) {
        descritions.add(descrition);
    }


    public void delete(Descricao descricao) {
        descritions.remove(descricao);
    }
}