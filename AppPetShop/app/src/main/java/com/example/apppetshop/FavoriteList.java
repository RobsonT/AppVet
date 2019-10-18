package com.example.apppetshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Pet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class FavoriteList extends Fragment {
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    List<Favorito> favoritos;

    FavoritoDAO favoritoDao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_produto_favoritos,  container, false);

        int clientId = Integer.parseInt(getArguments().getString("clientId"));

        favoritoDao = FavoritoDAO.getInstance();
        favoritos = favoritoDao.getByClient(clientId);

        favoriteAdapter = new FavoriteAdapter(favoritos);

        recyclerView = v.findViewById(R.id.recyclerViewFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favoriteAdapter);

        return v;
    }
}
