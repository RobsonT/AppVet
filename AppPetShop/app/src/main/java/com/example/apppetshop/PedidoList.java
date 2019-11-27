package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.CompraDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PedidoList extends Fragment {

    RecyclerView recyclerView;
    PedidoAdapter pedidoAdapter;
    List<Compra> pedidos;

    private FirebaseAuth auth;

    CompraDAO compraDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lista_pedido, container, false);

        auth = FirebaseAuth.getInstance();

        compraDAO = CompraDAO.getInstance();
        pedidos = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/compras")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Compra compra = document.toObject(Compra.class);
                                if (auth.getUid().equals(compra.getIdCliente())) {
                                    pedidos.add(compra);
                                }
                            }
                        } else {
                            Log.d("PedidoList", "Error getting documents: ", task.getException());
                        }
                    }
                });

        pedidoAdapter = new PedidoAdapter(pedidos);

        recyclerView = v.findViewById(R.id.recyclerViewPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pedidoAdapter);

        return v;
    }
}