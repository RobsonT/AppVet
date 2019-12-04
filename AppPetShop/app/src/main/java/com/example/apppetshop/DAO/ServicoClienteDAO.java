package com.example.apppetshop.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apppetshop.model.ServicoCliente;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class ServicoClienteDAO {

    private static ServicoClienteDAO instance;
    private FirebaseFirestore db;

    private ServicoClienteDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public static ServicoClienteDAO getInstance(){
        if(instance == null){
            instance = new ServicoClienteDAO();
        }
        return instance;
    }

    public void save(ServicoCliente serviceClient) {
        String id = serviceClient.getId();
        db.collection("servicosCliente")
                .document(id)
                .set(serviceClient)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("servicoClienteDao", "Sucesso");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("serviceClienteDao", e.getMessage());
                    }
                });
    }
}