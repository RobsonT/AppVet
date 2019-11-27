package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.model.Favorito;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProdutoFavoritos extends Fragment {

    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    List<Favorito> favoritos;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_produto_favoritos,  container, false);

        favoritos = new ArrayList<>();

        final List<Favorito> favoritos = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/favoritos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Favorito fav = document.toObject(Favorito.class);
                                favoritos.add(fav);
                            }
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        favoriteAdapter = new FavoriteAdapter(favoritos);

        recyclerView = v.findViewById(R.id.recyclerViewFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favoriteAdapter);

        return v;
    }
}