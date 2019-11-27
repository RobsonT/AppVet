package com.example.apppetshop.DAO;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apppetshop.model.Favorito;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class FavoritoDAO {

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    private static FavoritoDAO instance;

    private FavoritoDAO() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }


    public static FavoritoDAO getInstance() {
        if (instance == null) {
            instance = new FavoritoDAO();
        }
        return instance;
    }

    public void save(Favorito favorito) {
        String id = db.collection("favoritos").document().getId();
        favorito.setId(id);
        db.collection("favoritos")
                .document(id)
                .set(favorito)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i("favoritoDao", "Sucesso");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i("favoritoDao", e.getMessage());
                    }
                });
    }

    public void delete(Favorito favorito) {
        db.collection("favoritos").document(favorito.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("favoritoDAO", "Sucesso");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("favoritoDAO", "Error deleting document", e);
                    }
                });
    }
}