package com.example.apppetshop.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apppetshop.model.Item;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private static ItemDAO instance;

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private ItemDAO() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    public static ItemDAO getInstance(){
        if(instance == null){
            instance = new ItemDAO();
        }
        return instance;
    }

    public void save(Item item) {
        String id = db.collection("itens").document().getId();
        item.setId(id);
        db.collection("itens")
                .document(id)
                .set(item)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("itemDao", "Sucesso");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("itemDao","Erro", e);
                    }
                });
    }

    public void delete(Item item) {
        db.collection("itens").document(item.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("itemDAO", "Sucesso");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("itemDAO", "Error deleting document", e);
                    }
                });
    }
}