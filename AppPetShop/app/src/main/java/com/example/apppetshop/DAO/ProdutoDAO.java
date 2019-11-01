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
        produto.setNome("Ração para Cachorro");
        produto.setImagem(R.drawable.produtoracaocachorro);
        produto.setPreco(18);
        produto.setDescricao("Ração que contem comida gostosa e faz seu cachorro se sentir feliz");
        products.add(produto);

        Produto produto1 = new Produto();
        produto1.setId(1);
        produto1.setNome("Ração para Gato");
        produto1.setImagem(R.drawable.produto1racaogato);
        produto1.setPreco(15);
        produto1.setDescricao("Ração que contem comida gostosa e faz seu gato se sentir feliz");
        products.add(produto1);

        Produto produto2 = new Produto();
        produto2.setId(2);
        produto2.setNome("Boneco");
        produto2.setImagem(R.drawable.produto2boneco);
        produto2.setPreco(10);
        produto2.setDescricao("Boneco para animar a vida de seu pet");
        products.add(produto2);

        Produto produto3 = new Produto();
        produto3.setId(3);
        produto3.setNome("Casa para Gato");
        produto3.setImagem(R.drawable.produto3casagato);
        produto3.setPreco(35);
        produto3.setDescricao("Uma casa que pode tornar seu gato mais contente");
        products.add(produto3);

        Produto produto4 = new Produto();
        produto4.setId(4);
        produto4.setNome("Corda de brinquedo");
        produto4.setImagem(R.drawable.produto4corda);
        produto4.setPreco(5);
        produto4.setDescricao("Uma corda que seu cachorro irá adorar morder");
        products.add(produto4);

        Produto produto5 = new Produto();
        produto5.setId(5);
        produto5.setNome("Galinha pra fazer raiva");
        produto5.setImagem(R.drawable.produto5galinha);
        produto5.setPreco(35);
        produto5.setDescricao("Galinha que faz sons estresantes que ninguem gosta de ouvir");
        products.add(produto5);

        Produto produto6 = new Produto();
        produto6.setId(6);
        produto6.setNome("Banho para cachorro");
        produto6.setImagem(R.drawable.produto6shampoo);
        produto6.setPreco(30);
        produto6.setDescricao("Um conjunto dos melhores produtos para da um banho no seu cachorro");
        products.add(produto6);

        Produto produto7 = new Produto();
        produto7.setId(7);
        produto7.setNome("Shanpoo para gato");
        produto7.setImagem(R.drawable.produto7shappogato);
        produto7.setPreco(20);
        produto7.setDescricao("Um shampoo para manter seu gato limpo");
        products.add(produto7);

        Produto produto8 = new Produto();
        produto8.setId(8);
        produto8.setNome("Escova para gato");
        produto8.setImagem(R.drawable.produto8escovagato);
        produto8.setPreco(15);
        produto8.setDescricao("Uma escova para manter seu gato sem pelos");
        products.add(produto8);

        Produto produto9 = new Produto();
        produto9.setId(9);
        produto9.setNome("Escova para cachorro");
        produto9.setImagem(R.drawable.produto9escovacachorro);
        produto9.setPreco(15);
        produto9.setDescricao("Uma escova para manter seu cachorro sem pelos");
        products.add(produto9);

        Produto produto10 = new Produto();
        produto10.setId(10);
        produto10.setNome("Casa gato");
        produto10.setImagem(R.drawable.produto10casagato);
        produto10.setPreco(150);
        produto10.setDescricao("Uma casa em formato de gato para o seu gato");
        products.add(produto10);

        Produto produto11 = new Produto();
        produto11.setId(11);
        produto11.setNome("Cama cachorro");
        produto11.setImagem(R.drawable.produto11casacachorro);
        produto11.setPreco(130);
        produto11.setDescricao("Uma cama para o conforto de seu cachorro");
        products.add(produto11);

        Produto produto12 = new Produto();
        produto12.setId(12);
        produto12.setNome("Limpeza ouvido de cachorro");
        produto12.setImagem(R.drawable.produto12limpezaouvido);
        produto12.setPreco(13);
        produto12.setDescricao("Um remedio para limpar ouvido de seu cachorro");
        products.add(produto12);

        Produto produto13 = new Produto();
        produto13.setId(13);
        produto13.setNome("Corrente para cachorro");
        produto13.setImagem(R.drawable.produto13corrent);
        produto13.setPreco(20);
        produto13.setDescricao("Coleira para passear com seu cachorro");
        products.add(produto13);

        Produto produto14 = new Produto();
        produto14.setId(14);
        produto14.setNome("Gravata para pet");
        produto14.setImagem(R.drawable.produto14gravata);
        produto14.setPreco(15);
        produto14.setDescricao("Gravata para seus pets");
        products.add(produto14);

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