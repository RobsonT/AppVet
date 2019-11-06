package com.example.apppetshop;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Produto> productList;
    FavoritoDAO favoritoDao;
    Context context;
    Favorito favorito;
    Produto product;
    int clientId;

    public ProductAdapter(List<Produto> productList, int clientId) {
        this.productList = productList;
        this.clientId = clientId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        favoritoDao = FavoritoDAO.getInstance();
        List<Favorito> favoritos = favoritoDao.getByClient(clientId);

        product = productList.get(position);
        holder.textProduct.setText(product.getNome());
        holder.imgProduct.setImageResource(product.getImagem());
        holder.textPreco.setText("R$" + String.valueOf(product.getPreco()));

        for(Favorito f: favoritos){
            if(f.getIdProduto() == product.getId()){
                holder.favoriteLeft.setImageResource(R.drawable.ic_favorite_black_24dp);
                holder.favoriteLeft.setTag(product.getId()+"-true");
            }
        }
        if(!holder.favoriteLeft.getTag().toString().equals(product.getId()+"-true")){
            holder.favoriteLeft.setTag(product.getId()+"-false");
        }

        holder.favoriteLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                favorito = new Favorito();
                String[] tag = holder.favoriteLeft.getTag().toString().split("-");
                if(tag[1].equals("false")) {
                    holder.favoriteLeft.setImageResource(R.drawable.ic_favorite_black_24dp);
                    holder.favoriteLeft.setTag(tag[0]+"-true");
                    favorito.setIdProduto(Integer.parseInt(tag[0]));
                    Log.i("ERROOOO", String.valueOf(favorito.getIdProduto()));
                    favorito.setIdCliente(clientId);
                    favoritoDao.save(favorito);
                }else{
                    holder.favoriteLeft.setImageResource(R.drawable.ic_favorite);
                    favorito.setIdProduto(Integer.parseInt(tag[0]));
                    favorito.setIdCliente(clientId);
                    favoritoDao.delete(favorito);
                    holder.favoriteLeft.setTag(tag[0]+"-false");
                }
            }
        });

        holder.firstColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( view.getContext(), ProdutoDescricao.class);
                i.putExtra( "idProduto", String.valueOf(productList.get(position).getId()));
                i.putExtra("idCliente", String.valueOf(clientId));
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView textProduct;
        TextView textPreco;
        CardView cv;
        LinearLayout firstColumn;
        ImageView favoriteLeft;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textProduct = itemView.findViewById(R.id.nomeProduto);
            textPreco = itemView.findViewById(R.id.precoProduto);
            cv = itemView.findViewById(R.id.cardView);
            firstColumn = itemView.findViewById(R.id.firstColumn);
            favoriteLeft = itemView.findViewById(R.id.ic_favorite1);
        }
    }
}