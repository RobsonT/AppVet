package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apppetshop.DAO.CompraDAO;
import com.example.apppetshop.DAO.DescricaoDAO;
import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.DAO.ItemDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Descricao;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Produto;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDescricao extends AppCompatActivity {

    TextView nomeProduto;
    ImageView imagemProduto;
    TextView precoProduto;
    Button addCarrinho,button;
    Button favorito;
    TextView descricaoProduto;
    ListView lstDescricao;

    ArrayAdapter<String> arrayAdapter;

    int productId;
    int clientId;

    ItemDAO itemDAO;
    CompraDAO compraDAO;
    ProdutoDAO produtoDAO;
    FavoritoDAO favoritoDAO;
    DescricaoDAO descricaoDAO;
    Compra compra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_descricao);

        produtoDAO = ProdutoDAO.getInstance();
        compraDAO = CompraDAO.getInstance();
        itemDAO = ItemDAO.getInstance();
        favoritoDAO = FavoritoDAO.getInstance();
        descricaoDAO = DescricaoDAO.getInstance();

        nomeProduto = findViewById(R.id.nomeProduto);
        imagemProduto = findViewById(R.id.imagemProduto);
        precoProduto = findViewById(R.id.precoProduto);
        addCarrinho = findViewById(R.id.addCarrinho);
        favorito = findViewById(R.id.addFavoritos);
        descricaoProduto = findViewById(R.id.descricaoProduto);
        lstDescricao = findViewById(R.id.lstDescricao);

        button = findViewById(R.id.cancelarProduto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retorno = new Intent();

                setResult(Activity.RESULT_CANCELED, retorno);
                finish();
            }
        });

        productId = Integer.parseInt(getIntent().getExtras().getString("idProduto"));
        clientId = Integer.parseInt(getIntent().getExtras().getString("idCliente"));

        Produto produto = produtoDAO.get(productId);
        compra = compraDAO.getUnconfirmed(clientId);

        nomeProduto.setText(produto.getNome());
        imagemProduto.setImageResource(produto.getImagem());
        DecimalFormat df = new DecimalFormat("0.00");
        String preco = "R$ " + df.format(produto.getPreco());
        precoProduto.setText(preco);
        descricaoProduto.setText(produto.getDescricao());

        ArrayList<String> listaDescricoes = new ArrayList<>();
        String descricao;
        List<Descricao> descricoes = descricaoDAO.getByProduct(productId);
        for (Descricao d : descricoes) {
            descricao = d.getAtributo() + ": " + d.getValor();
            listaDescricoes.add(descricao);
        }
        arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,
                listaDescricoes);

        lstDescricao.setAdapter(arrayAdapter);

        if (favoritoDAO.getByClient(clientId).size() > 0) {
            for (Favorito f : favoritoDAO.getByClient(clientId)) {
                if (f.getIdProduto() == productId) {
                    favorito.setText("remover favoritos");
                    break;
                }
            }
        }

        favorito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (favorito.getText().toString().equals("adicionar favoritos")) {
                    favorito.setText("remover favoritos");
                    Favorito favorito = new Favorito();
                    favorito.setIdCliente(clientId);
                    favorito.setIdProduto(productId);
                    favoritoDAO.save(favorito);
                } else {
                    favorito.setText("adicionar favoritos");
                    Favorito favorito = new Favorito();
                    favorito.setIdCliente(clientId);
                    favorito.setIdProduto(productId);
                    favoritoDAO.delete(favorito);
                }
            }
        });
    }

    public void addProduct(View view) {
        if (compra == null) {
            compra = new Compra();
            compra.setId(compraDAO.getAll().size());
            compra.setConfirmado(false);
            compra.setIdCliente(clientId);
            compraDAO.save(compra);
        }

        List<Item> itens = itemDAO.getByCompra(compra.getId());
        Item item = null;

        boolean findItem = false;
        for (Item i : itens) {
            if (productId == i.getIdProduto()) {
                item = i;
                item.setQuantidade(item.getQuantidade() + 1);
                findItem = true;
                itemDAO.updateQuantity(item);
                break;
            }
        }
        if (!findItem) {
            item = new Item();
            item.setIdCompra(compra.getId());
            item.setIdProduto(productId);
            item.setQuantidade(1);
            itemDAO.save(item);
        }


        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("clientId", String.valueOf(clientId));
        i.putExtra("compra", "compra");
        startActivity(i);
    }
}
