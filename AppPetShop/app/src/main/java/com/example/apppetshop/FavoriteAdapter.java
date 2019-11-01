package com.example.apppetshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Produto;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    List<Favorito> favoriteList;
    Context context;

    public FavoriteAdapter(List<Favorito> favoriteList) {
        this.favoriteList = favoriteList;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list, parent, false);
        FavoriteAdapter.ViewHolder viewHolder = new FavoriteAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoriteAdapter.ViewHolder holder, final int position) {
        Favorito favorite = favoriteList.get(position);
        ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
        Produto produto = produtoDAO.get(favorite.getIdProduto());
        holder.nameFavorite.setText(produto.getNome());
        holder.imgFavorite.setImageResource(produto.getImagem());
        holder.priceFavorite.setText(String.valueOf(produto.getPreco()));
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFavorite;
        TextView nameFavorite;
        TextView priceFavorite;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            nameFavorite = itemView.findViewById(R.id.nameFavorite);
            priceFavorite = itemView.findViewById(R.id.priceFavorite);
            cv = itemView.findViewById(R.id.cardViewFavorite);
        }

    }
}