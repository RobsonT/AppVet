package com.example.apppetshop.DAO;

import com.example.apppetshop.R;
import com.example.apppetshop.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements Dao<Produto>{

    private List<Produto> products = new ArrayList<>();

    private static ProdutoDAO instance;

    private ProdutoDAO() {
        products = new ArrayList<>();
        Produto produto = new Produto();
        produto.setId(0);
        produto.setNome("Ração que contem comida gostosa e faz seu cachorro se sentir feliz");
        produto.setImagem(R.drawable.iconcachorro);
        produto.setPreco(18);
        produto.setDescricao("Isso é a descrição do produto");
        products.add(produto);
    }

    public static ProdutoDAO getInstance(){
        if(instance == null){
            instance = new ProdutoDAO();
        }

        return instance;
    }

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
