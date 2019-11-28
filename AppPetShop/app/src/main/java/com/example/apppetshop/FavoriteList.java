package com.example.apppetshop;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Produto;
import com.example.apppetshop.model.ServicoCliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoriteList extends Fragment {
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    List<Favorito> favoritos;
    TextView textFav;

    private FirebaseAuth auth;

    FavoritoDAO favoritoDao;
    ProdutoDAO produtoDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_produto_favoritos, container, false);

        auth = FirebaseAuth.getInstance();
        textFav = v.findViewById(R.id.textFav);

        favoritoDao = FavoritoDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        favoritos = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recyclerViewFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        FirebaseFirestore.getInstance().collection("/favoritos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Favorito fav = document.toObject(Favorito.class);
                                if (fav.getIdCliente().equals(auth.getUid())) {
                                    favoritos.add(fav);
                                }
                            }
                            if(favoritos.size() > 0)
                                textFav.setVisibility(View.GONE);
                            else
                                textFav.setVisibility(View.VISIBLE);
                            favoriteAdapter = new FavoriteAdapter(favoritos);
                            favoriteAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
                                @Override
                                public void onItemDetail(int position) {
                                    showItem(position);
                                }
                            });

                            recyclerView.setAdapter(favoriteAdapter);
                        } else {
                            Log.d("ServicosCliente", "Error getting documents: ", task.getException());
                        }
                    }
                });

        return v;
    }

    public void showItem(final int position) {
        FirebaseFirestore.getInstance().collection("/produtos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto prod = document.toObject(Produto.class);
                                if (prod.getId().equals(favoritos.get(position).getIdProduto())) {
                                    final Intent i = new Intent(getContext(), ProdutoDescricao.class);
                                    i.putExtra("idProduto", prod.getId());
                                    startActivity(i);
                                }
                            }
                        } else {
                            Log.d("favoritList", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
}