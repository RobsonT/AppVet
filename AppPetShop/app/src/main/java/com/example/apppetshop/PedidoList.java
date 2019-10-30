package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.CompraDAO;
import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Pet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PedidoList extends Fragment {

    RecyclerView recyclerView;
    PedidoAdapter pedidoAdapter;
    List<Compra> pedidos;
    int clientId;

    CompraDAO compraDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lista_pedido, container, false);

        clientId = Integer.parseInt(getArguments().getString("clientId"));

        compraDAO = CompraDAO.getInstance();
        pedidos = compraDAO.getByClient(clientId);

        pedidoAdapter = new PedidoAdapter(pedidos);

        recyclerView = v.findViewById(R.id.recyclerViewPedido);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pedidoAdapter);

        return v;
    }
}
