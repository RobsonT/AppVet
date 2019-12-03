package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.CompraDAO;
import com.example.apppetshop.model.Compra;
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

        recyclerView = v.findViewById(R.id.recyclerViewPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
                            pedidoAdapter = new PedidoAdapter(pedidos);
                            pedidoAdapter.setOnItemClickListener(new PedidoAdapter.OnItemClickListener() {
                                @Override
                                public void onPedidoDetail(int position) {
                                    showItem(position);
                                }
                            });
                            recyclerView.setAdapter(pedidoAdapter);
                        } else {
                            Log.d("PedidoList", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return v;
    }

    public void showItem(final int position) {
        final Intent i = new Intent(getContext(), ListItemPedido.class);
        i.putExtra("idPedido", pedidos.get(position).getId());
        startActivity(i);
    }
}