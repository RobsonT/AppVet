package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.model.Favorito;

import java.util.List;

public class ProdutoFavoritos extends Fragment {

    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    List<Favorito> favoritos;

    FavoritoDAO favoritoDao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_produto_favoritos,  container, false);

        favoritoDao = FavoritoDAO.getInstance();
        favoritos = favoritoDao.getAll();

        favoriteAdapter = new FavoriteAdapter(favoritos);

        recyclerView = v.findViewById(R.id.recyclerViewFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favoriteAdapter);

        return v;
    }
}