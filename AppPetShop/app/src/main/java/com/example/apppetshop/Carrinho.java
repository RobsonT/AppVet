package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apppetshop.DAO.CompraDAO;
import com.example.apppetshop.DAO.ItemDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Carrinho extends Fragment {

    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    List<Item> itens;

    static TextView valorTotal;
    static LinearLayout listCart;
    static TextView warningCart;
    static Button confirm;
    static LinearLayout valueField;
    static TextView localCompra;
    Button mapaCarrinho;
    TextView moreProducts;

    private FirebaseAuth auth;

    private Compra compra;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_carrinho, container, false);

        auth = FirebaseAuth.getInstance();
        itens = new ArrayList<>();
        initialize(v);

        compra = null;

        cartAdapter = new CartAdapter(itens);

        recyclerView = v.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

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

                                    if (compra != null) {
                                        hideItens();
                                        FirebaseFirestore.getInstance().collection("/itens")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                                Item i = document.toObject(Item.class);
                                                                if (i.getIdCompra().equals(compra.getId())) {
                                                                    itens.add(i);
                                                                }
                                                            }


                                                            cartAdapter = new CartAdapter(itens);
                                                            recyclerView.setAdapter(cartAdapter);
                                                            cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
                                                                @Override
                                                                public void onItemDelete(int position) {
                                                                    removeItem(position);
                                                                }

                                                                @Override
                                                                public void onItemDetails(int position) {
                                                                    showItem(position);
                                                                }
                                                            });
                                                            final double[] value = {0};
                                                            for (final Item item : itens) {
                                                                final Produto[] p = {null};
                                                                FirebaseFirestore.getInstance().collection("/produtos")
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                                                                        Produto prod = document.toObject(Produto.class);
                                                                                        if (item.getIdProduto().equals(prod.getId())) {
                                                                                            p[0] = prod;
                                                                                        }
                                                                                    }
                                                                                    value[0] += item.getQuantidade() * p[0].getPreco();
                                                                                    valorTotal.setText(String.valueOf(value[0]));
                                                                                } else {
                                                                                    Log.d("Carrinho", "Error getting documents: ", task.getException());
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        } else {
                                                            Log.d("carrinho", "Error getting documents: ", task.getException());
                                                        }
                                                    }
                                                });


                                    } else {
                                        showItens();
                                        itens = new ArrayList<>();
                                    }

                                }
                            }
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        moreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("clientId", auth.getUid());

                LojaFragment lojaFragment = new LojaFragment();
                lojaFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, lojaFragment);
                fragmentTransaction.commit();
                MainActivity.navigationView.setCheckedItem(R.id.navHome);
            }
        });
        return v;
    }

    public void hideItens() {
        listCart.setVisibility(View.VISIBLE);
        itens = new ArrayList<>();
        warningCart.setVisibility(View.GONE);
        valueField.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
    }

    public void showItens() {
        listCart.setVisibility(View.GONE);
        warningCart.setVisibility(View.VISIBLE);
        valueField.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
    }

    public void initialize(View v) {

        itemDao = ItemDAO.getInstance();
        compraDAO = CompraDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        listCart = v.findViewById(R.id.listCart);
        warningCart = v.findViewById(R.id.warningCart);
        moreProducts = v.findViewById(R.id.moreProducts);
        confirm = v.findViewById(R.id.confirm);
        valorTotal = v.findViewById(R.id.valorTotal);
        valueField = v.findViewById(R.id.valorCarrinho);
        mapaCarrinho = v.findViewById(R.id.mapaCarrinho);
        localCompra = v.findViewById(R.id.localCarrinho);
        mapaCarrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MapaServico.class);
                intent.putExtra("required", "compra");
                startActivity(intent);
            }
        });
    }

    public void removeItem(int position) {
        Item item = itens.get(position);
        itens.remove(position);
        cartAdapter.notifyItemRemoved(position);
        ItemDAO itemDAO = ItemDAO.getInstance();
        itemDAO.delete(item);
        final CompraDAO compraDAO = CompraDAO.getInstance();
        itens = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("/itens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item i = document.toObject(Item.class);
                                if (i.getIdCompra().equals(compra.getId())) {
                                    itens.add(i);
                                }
                            }
                            if (itens.size() == 0) {
                                compraDAO.delete(compra);
                                Carrinho.listCart.setVisibility(View.GONE);
                                Carrinho.warningCart.setVisibility(View.VISIBLE);
                                Carrinho.valueField.setVisibility(View.GONE);
                                Carrinho.confirm.setVisibility(View.GONE);
                            } else {
                                Carrinho.valorTotal.setText(String.valueOf(getTotalValue()));
                            }
                        } else {
                            Log.d("carrinho", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void showItem(final int position) {
        FirebaseFirestore.getInstance().collection("/produtos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto prod = document.toObject(Produto.class);
                                if (prod.getId().equals(itens.get(position).getIdProduto())) {
                                    final Intent i = new Intent(getContext(), ProdutoDescricao.class);
                                    i.putExtra("idProduto", prod.getId());
                                    startActivity(i);
                                }
                            }
                        } else {
                            Log.d("Carrinho", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public double getTotalValue() {
        final double[] value = {0};
        for (final Item i : this.itens) {
            final Produto[] p = {null};

            FirebaseFirestore.getInstance().collection("/produtos")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Produto prod = document.toObject(Produto.class);
                                    if (i.getIdProduto().equals(prod.getId())) {
                                        p[0] = prod;
                                    }
                                }
                                value[0] += i.getQuantidade() * p[0].getPreco();
                            } else {
                                Log.d("Loja fragment", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        return value[0];
    }
}