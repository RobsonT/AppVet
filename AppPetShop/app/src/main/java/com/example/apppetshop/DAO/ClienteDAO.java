package com.example.apppetshop.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apppetshop.model.Cliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ClienteDAO{
//    private List<Cliente> clients;
    private FirebaseFirestore db;
    private static ClienteDAO instance;

    private ClienteDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public static ClienteDAO getInstance(){
        if(instance == null){
            instance = new ClienteDAO();
        }

        return instance;
    }

    public Cliente get(String id) {
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.i("teste1", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.i("Teste2", "Error getting documents.", task.getException());
                        }
                    }
                });
        return null;
    }

    public List<Cliente> getAll() {

        return null;
    }

    public void save(Cliente client) {
        db.collection("clientes")
                .add(client)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("Erro client", e.getMessage());
                    }
                });
    }

    public void delete(Cliente client) {

//        clients.remove(client);
    }

    public Cliente getByEmail(String email) {
//        for (Cliente c : clients) {
//            if (c.getEmail().equals(email)) {
//                return c;
//            }
//        }
        return null;
    }
}