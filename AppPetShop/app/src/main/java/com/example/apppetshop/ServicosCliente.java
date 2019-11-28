package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apppetshop.DAO.ServicoClienteDAO;
import com.example.apppetshop.model.Produto;
import com.example.apppetshop.model.ServicoCliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ServicosCliente extends Fragment {

    RecyclerView recyclerView;
    ServiceAdapter serviceAdapter;
    List<ServicoCliente> servicos;

    int clientId;
    TextView message;

    ServicoClienteDAO servicoClienteDAO;

    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_servicos_cliente,  container, false);;

        servicoClienteDAO = ServicoClienteDAO.getInstance();
        message = v.findViewById(R.id.nenhumServico);

        auth = FirebaseAuth.getInstance();

        servicos = new ArrayList<>();

        recyclerView = v.findViewById(R.id.recyclerViewService);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseFirestore.getInstance().collection("/servicosCliente")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                ServicoCliente serv = document.toObject(ServicoCliente.class);
                                if (serv.getIdCliente().equals(auth.getUid())) {
                                    servicos.add(serv);
                                }
                            }
                            if(servicos.size() != 0){
                                message.setVisibility(View.GONE);
                            }else {
                                message.setVisibility(View.VISIBLE);
                            }
                            serviceAdapter = new ServiceAdapter(servicos);
                            recyclerView.setAdapter(serviceAdapter);
                        } else {
                            Log.d("ServicosCliente", "Error getting documents: ", task.getException());
                        }
                    }
                });

        FloatingActionButton fab = v.findViewById(R.id.fabService);


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
            servicos = new ArrayList<>();

            FirebaseFirestore.getInstance().collection("/servicosCliente")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    ServicoCliente serv = document.toObject(ServicoCliente.class);
                                    if (serv.getIdCliente().equals(auth.getUid())) {
                                        servicos.add(serv);
                                    }
                                }

                                serviceAdapter = new ServiceAdapter(servicos);
                                recyclerView.setAdapter(serviceAdapter);
                            } else {
                                Log.d("ServicosCliente", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
    }
}
