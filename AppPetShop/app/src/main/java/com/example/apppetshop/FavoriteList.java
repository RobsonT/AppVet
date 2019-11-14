package com.example.apppetshop;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Produto;

import java.util.List;

public class FavoriteList extends Fragment {
    RecyclerView recyclerView;
    FavoriteAdapter favoriteAdapter;
    List<Favorito> favoritos;

    FavoritoDAO favoritoDao;
    ProdutoDAO produtoDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_produto_favoritos,  container, false);

        int clientId = Integer.parseInt(getArguments().getString("clientId"));

        favoritoDao = FavoritoDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        favoritos = favoritoDao.getByClient(clientId);

        favoriteAdapter = new FavoriteAdapter(favoritos);

        recyclerView = v.findViewById(R.id.recyclerViewFavorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(favoriteAdapter);

        favoriteAdapter.setOnItemClickListener(new FavoriteAdapter.OnItemClickListener() {
            @Override
            public void onItemDetail(int position) {
                showItem(position);
            }
        });

        return v;
    }

    public void showItem(int position) {
        Intent i = new Intent( getContext(), ProdutoDescricao.class);
        Produto produto = produtoDAO.get(favoritos.get(position).getIdProduto());
        i.putExtra( "idProduto", String.valueOf( produto.getId()));
        i.putExtra("idCliente", String.valueOf(favoritos.get(position).getIdCliente()));
        startActivity(i);
    }
}