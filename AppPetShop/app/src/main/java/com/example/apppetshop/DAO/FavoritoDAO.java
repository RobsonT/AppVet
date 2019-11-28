package com.example.apppetshop.DAO;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.apppetshop.Carrinho;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Item;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

    public void delete(final Favorito favorito) {
        FirebaseFirestore.getInstance().collection("/favoritos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Favorito fav = document.toObject(Favorito.class);
                                if (fav.getIdProduto().equals(favorito.getIdProduto()) && fav.getIdCliente().equals(favorito.getIdCliente())) {
                                    db.collection("favoritos").document(fav.getId())
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
                        } else {
                            Log.d("FavoritoDao", "Error deleting documents: ", task.getException());
                        }
                    }
                });
    }
}