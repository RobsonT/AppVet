package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.ServicoClienteDAO;
import com.example.apppetshop.model.ServicoCliente;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ServicosCliente extends Fragment {

    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    List<ServicoCliente> servicos;

    int clientId;

    ServicoClienteDAO servicoClienteDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_servicos_cliente,  container, false);

        clientId = Integer.parseInt(getArguments().getString("clientId"));

        servicoClienteDAO = ServicoClienteDAO.getInstance();
        servicos = servicoClienteDAO.getByClient(clientId);

        serviceAdapter = new ServiceAdapter(servicos);

        FloatingActionButton fab = v.findViewById(R.id.fabService);

        recyclerView = v.findViewById(R.id.recyclerViewService);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(serviceAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),Servicos.class);
                i.putExtra("clientId", String.valueOf(clientId));
                startActivityForResult(i, 0);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            servicos = servicoClienteDAO.getByClient(clientId);
            serviceAdapter = new ServiceAdapter(servicos);
            recyclerView.setAdapter(serviceAdapter);
        }
    }
}
