package com.example.apppetshop.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class CompraDAO{
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private List<Compra> compras;

    private static CompraDAO instance;

    private CompraDAO() {
        compras = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    public static CompraDAO getInstance(){
        if(instance == null){
            instance = new CompraDAO();
        }
        return instance;
    }

    public void save(Compra purchase) {
        String id = purchase.getId();
        db.collection("compras")
                .document(id)
                .set(purchase)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("CompraDao", "Sucesso");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("CompraDao", e.getMessage());
                    }
                });
    }

    public void delete(Compra compra) {
        db.collection("compras").document(compra.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("compraDAO", "Sucesso");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("compraDAO", "Error deleting document", e);
                    }
                });
    }
}