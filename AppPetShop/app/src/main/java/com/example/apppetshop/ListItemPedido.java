package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListItemPedido extends AppCompatActivity {

    RecyclerView recyclerView;
    ItemPedidoAdapter itemPedidoAdapter;
    TextView totalValue;
    List<Item> itens;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list__item__pedido);

        auth = FirebaseAuth.getInstance();
        totalValue = findViewById(R.id.valorTotalItemPedido);

        itens = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerViewItemPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String idPedido = getIntent().getExtras().getString("idPedido");

        FirebaseFirestore.getInstance().collection("/itens")
                .whereEqualTo("idCompra", idPedido)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            double value = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Item item = document.toObject(Item.class);
                                itens.add(item);

                                FirebaseFirestore.getInstance().collection("/produtos")
                                        .whereEqualTo("id", item.getIdProduto())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    double value = 0;
                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                        Produto prod = document.toObject(Produto.class);
                                                        value += prod.getPreco() * item.getQuantidade();
                                                        double currentValue = Double.parseDouble(totalValue.getText().toString());
                                                        totalValue.setText(String.valueOf(value + currentValue));
                                                    }
                                                }
                                            }
                                        });

                            }
                            itemPedidoAdapter = new ItemPedidoAdapter(itens);
                            itemPedidoAdapter.setOnItemClickListener(new ItemPedidoAdapter.OnItemClickListener() {
                                @Override
                                public void onItemDetail(int position) {
                                    showItem(position);
                                }
                            });
                            recyclerView.setAdapter(itemPedidoAdapter);
                        } else {
                            Log.d("ItemPedidoList", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void showItem(final int position) {
        FirebaseFirestore.getInstance().collection("/produtos")
                .whereEqualTo("id", itens.get(position).getIdProduto())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto prod = document.toObject(Produto.class);
                                final Intent i = new Intent(getApplicationContext(), ProdutoDescricao.class);
                                i.putExtra("idProduto", prod.getId());
                                startActivity(i);
                            }
                        } else {
                            Log.d("ItemPedidoList", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

}
