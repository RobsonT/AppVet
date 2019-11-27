package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    TextView moreProducts;

    private ItemDAO itemDao;
    private CompraDAO compraDAO;
    private ProdutoDAO produtoDAO;

    private FirebaseAuth auth;

    private Compra compra;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_carrinho, container, false);

        auth = FirebaseAuth.getInstance();
        itens = new ArrayList<>();
        initialize(v);

        compra = null;

        FirebaseFirestore.getInstance().collection("/compras")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Compra c = document.toObject(Compra.class);
                                if (!c.isConfirmado() && c.getId().equals(auth.getUid())) {
                                    compra = c;
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
                            } else {
                                Log.d("carrinho", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            double value = 0;
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
                                } else {
                                    Log.d("Carrinho", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                value += item.getQuantidade() * p[0].getPreco();
            }
            valorTotal.setText(String.valueOf(value));
        } else {
            showItens();
            itens = new ArrayList<>();
        }
        cartAdapter = new CartAdapter(itens);

        recyclerView = v.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                removeItem(position);
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
    }

    public void removeItem(int position) {
        Item item = itens.get(position);
        itens.remove(position);
        cartAdapter.notifyItemRemoved(position);
        ItemDAO itemDAO = ItemDAO.getInstance();
        itemDAO.delete(item);
        CompraDAO compraDAO = CompraDAO.getInstance();
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
                        } else {
                            Log.d("carrinho", "Error getting documents: ", task.getException());
                        }
                    }
                });
        if (itens.size() == 0) {
            compraDAO.delete(compra);
            Carrinho.listCart.setVisibility(View.GONE);
            Carrinho.warningCart.setVisibility(View.VISIBLE);
            Carrinho.valueField.setVisibility(View.GONE);
            Carrinho.confirm.setVisibility(View.GONE);
        } else {

            Carrinho.valorTotal.setText(String.valueOf(this.getTotalValue()));
        }
    }

    public double getTotalValue() {
        double value = 0;
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
                            } else {
                                Log.d("Loja fragment", "Error getting documents: ", task.getException());
                            }
                        }
                    });

            value += i.getQuantidade() * p[0].getPreco();
        }
        return value;
    }
}