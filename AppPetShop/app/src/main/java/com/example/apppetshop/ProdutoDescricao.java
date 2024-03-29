package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProdutoDescricao extends AppCompatActivity {

    TextView nomeProduto;
    ImageView imagemProduto;
    TextView precoProduto;
    Button addCarrinho, button;
    Button favorito;
    TextView descricaoProduto;

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

    FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_descricao);

        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();

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

        button = findViewById(R.id.cancelarProduto);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retorno = new Intent();

                setResult(Activity.RESULT_CANCELED, retorno);
                finish();
            }
        });

        productId = getIntent().getExtras().getString("idProduto");

        final Bitmap[] bitmap = {null};
        final long ONE_MEGABYTE = 1024 * 1024;
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
                                    nomeProduto.setText(produto.getNome());
                                    storage.getReferenceFromUrl(produto.getImagem()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            imagemProduto.setImageBitmap(bitmap[0]);
                                        }
                                    });
                                    DecimalFormat df = new DecimalFormat("0.00");
                                    String preco = "R$ " + df.format(produto.getPreco());
                                    precoProduto.setText(preco);
                                    descricaoProduto.setText(produto.getDescricao());
                                }
                            }
                        } else {
                            Log.d("ProdutoDescricao", "Error getting documents: ", task.getException());
                        }
                    }
                });

        final ArrayList<String> listaDescricoes = new ArrayList<>();
        final String[] descricao = new String[1];
        final List<Descricao> descricoes = new ArrayList<>();

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaDescricoes);
        FirebaseFirestore.getInstance().collection("/descricoes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Descricao desc = document.toObject(Descricao.class);
                                if (desc.getIdProduto().equals(productId)) {
                                    descricao[0] = desc.getAtributo() + ": " + desc.getValor();
                                    listaDescricoes.add(descricao[0]);
                                }

                            }
                        } else {
                            Log.d("ProdutoDescricao", "Error getting documents: ", task.getException());
                        }
                    }
                });

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
                                        Favorito fav = new Favorito();
                                        fav.setIdCliente(auth.getUid());
                                        fav.setIdProduto(productId); //product.getId()
                                        favoritoDAO.save(fav);
                                    } else {
                                        favorito.setText("adicionar favoritos");
                                        Favorito fav = new Favorito();
                                        fav.setIdCliente(auth.getUid());
                                        fav.setIdProduto(productId);
                                        favoritoDAO.delete(fav);
                                    }
                                }
                            });
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void addProduct(View view) {

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
                            if (compra == null) {
                                compra = new Compra();
                                String id = FirebaseFirestore.getInstance().collection("compras").document().getId();
                                compra.setId(id);
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

                                                Item item = null;

                                                boolean findItem = false;
                                                for (Item i : itens) {
                                                    if (productId.equals(i.getIdProduto())) {
                                                        item = i;
                                                        item.setQuantidade(item.getQuantidade() + 1);
                                                        findItem = true;
                                                        itemDAO.delete(item);
                                                        itemDAO.save(item);
                                                        break;
                                                    }
                                                }
                                                if (!findItem) {
                                                    String id = FirebaseFirestore.getInstance().collection("itens").document().getId();
                                                    item = new Item();
                                                    item.setId(id);
                                                    item.setIdCompra(compra.getId());
                                                    item.setIdProduto(productId);
                                                    item.setQuantidade(1);
                                                    itemDAO.save(item);
                                                }

                                            } else {
                                                Log.d("Loja fragment", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    });

                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("compra", "compra");
                            startActivity(i);
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}
