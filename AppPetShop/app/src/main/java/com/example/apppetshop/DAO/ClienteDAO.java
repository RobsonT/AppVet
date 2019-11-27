package com.example.apppetshop.DAO;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apppetshop.model.Cliente;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class ClienteDAO {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private static ClienteDAO instance;
    private ClienteDAO() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static ClienteDAO getInstance() {
        if (instance == null) {
            instance = new ClienteDAO();
        }

        return instance;
    }

    public void save(Cliente client) {
        db.collection("clientes")
                .document(auth.getUid())
                .set(client)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("ClientDao", "Sucesso");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("ClientDao", e.getMessage());
                    }
                });
    }
}