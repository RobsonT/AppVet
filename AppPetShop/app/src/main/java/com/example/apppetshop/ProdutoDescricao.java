package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private String productId;
    private Produto produto;
    ItemDAO itemDAO;
    CompraDAO compraDAO;
    ProdutoDAO produtoDAO;
    FavoritoDAO favoritoDAO;
    DescricaoDAO descricaoDAO;
    Compra compra;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_descricao);

        auth = FirebaseAuth.getInstance();

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

        productId =getIntent().getExtras().getString("idProduto");

        FirebaseFirestore.getInstance().collection("/produtos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto prod = document.toObject(Produto.class);
                                if (prod.getId().equals(productId)) {
                                    produto = prod;
                                }
                            }
                        } else {
                            Log.d("ProdutoDescricao", "Error getting documents: ", task.getException());
                        }
                    }
                });

        FirebaseFirestore.getInstance().collection("/compras")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Compra c = document.toObject(Compra.class);
                                if (!c.isConfirmado() && c.getIdCliente().equals(auth.getUid())) {
                                    compra = c;
                                }
                            }
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        nomeProduto.setText(produto.getNome());
        imagemProduto.setImageResource(produto.getImagem());
        DecimalFormat df = new DecimalFormat("0.00");
        String preco = "R$ " + df.format(produto.getPreco());
        precoProduto.setText(preco);
        descricaoProduto.setText(produto.getDescricao());

        ArrayList<String> listaDescricoes = new ArrayList<>();
        String descricao;
        final List<Descricao> descricoes = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/descricoes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Descricao desc = document.toObject(Descricao.class);
                                if (desc.getIdProduto().equals(productId)) {
                                    descricoes.add(desc);
                                }
                            }
                        } else {
                            Log.d("ProdutoDescricao", "Error getting documents: ", task.getException());
                        }
                    }
                });

        for (Descricao d : descricoes) {
            descricao = d.getAtributo() + ": " + d.getValor();
            listaDescricoes.add(descricao);
        }
        arrayAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1,
                listaDescricoes);

        lstDescricao.setAdapter(arrayAdapter);

        final List<Favorito> favoritos = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/favoritos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Favorito fav = document.toObject(Favorito.class);
                                if (auth.getUid().equals(fav.getIdCliente())) {
                                    favoritos.add(fav);
                                }
                            }
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        if (favoritos.size() > 0) {
            for (Favorito f : favoritos) {
                if (f.getIdProduto().equals(productId)) {
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
                    favorito.setIdCliente(auth.getUid());
                    favorito.setIdProduto(productId); //product.getId()
                    favoritoDAO.save(favorito);
                } else {
                    favorito.setText("adicionar favoritos");
                    Favorito favorito = new Favorito();
                    favorito.setIdCliente(auth.getUid());
                    favorito.setIdProduto(productId);
                    favoritoDAO.delete(favorito);
                }
            }
        });
    }

    public void addProduct(View view) {
        if (compra == null) {
            compra = new Compra();
            compra.setConfirmado(false);
            compra.setIdCliente(auth.getUid());
            Date currentTime = Calendar.getInstance().getTime();
            compra.setData(currentTime);
            compraDAO.save(compra);

        }

        final List<Item> itens = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/itens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item i = document.toObject(Item.class);
                                if (compra.getId().equals(i.getIdCompra())) {
                                    itens.add(i);
                                }
                            }
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        Item item = null;

        boolean findItem = false;
        for (Item i : itens) {
            if (productId == i.getIdProduto()) {
                item = i;
                item.setQuantidade(item.getQuantidade() + 1);
                findItem = true;
                itemDAO.save(item);
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
        i.putExtra("compra", "compra");
        startActivity(i);
    }
}
