package com.example.apppetshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Produto;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Produto> productList1;
    List<Produto> productList2;
    FavoritoDAO favoritoDao;
    Context context;
    Favorito favorito;
    Produto product;

    public ProductAdapter(List<Produto> productList1, List<Produto> productList2) {
        this.productList1 = productList1;
        this.productList2 = productList2;
        favorito = new Favorito();
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
       product = productList1.get(position);
        holder.textProduct.setText(product.getNome());
        holder.imgProduct.setImageResource(product.getImagem());
        holder.textPreco.setText("R$" + String.valueOf(product.getPreco()));
        favoritoDao = FavoritoDAO.getInstance();
        if (position < productList2.size()) {
            product = productList2.get(position);
            holder.textProduct1.setText(product.getNome());
            holder.imgProduct1.setImageResource(product.getImagem());
            holder.textPreco1.setText("R$" + String.valueOf(product.getPreco()));
        }else{
            holder.secondColumn.setVisibility(View.INVISIBLE);
        }
        holder.favoriteLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(holder.favoriteLeft.getTag().equals("false")) {
                    holder.favoriteLeft.setImageResource(R.drawable.ic_favorite_black_24dp);
                    holder.favoriteLeft.setTag("true");
                    favorito.setIdProduto(product.getId());
                    favorito.setIdCliente(0);
                    favoritoDao.save(favorito);
                }else{
                    holder.favoriteLeft.setImageResource(R.drawable.ic_favorite);
                    favorito.setIdProduto(product.getId());
                    favorito.setIdCliente(0);
                    favoritoDao.delete(favorito);
                    holder.favoriteLeft.setTag("false");
                }
            }
        });

        holder.favoriteRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(holder.favoriteRight.getTag().equals("false")) {
                    holder.favoriteRight.setImageResource(R.drawable.ic_favorite_black_24dp);
                    holder.favoriteRight.setTag("true");
                    favorito.setIdProduto(product.getId());
                    favorito.setIdCliente(0);
                    favoritoDao.save(favorito);
                }else{
                    holder.favoriteRight.setImageResource(R.drawable.ic_favorite);
                    holder.favoriteRight.setTag("false");
                    favorito.setIdProduto(product.getId());
                    favorito.setIdCliente(0);
                    favoritoDao.delete(favorito);
                }
            }
        });

        holder.secondColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( view.getContext(), ProdutoDescricao.class);
                i.putExtra( "idProduto", String.valueOf(productList1.get(position).getId()));
                view.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return productList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView textProduct;
        TextView textPreco;
        ImageView imgProduct1;
        TextView textProduct1;
        TextView textPreco1;
        CardView cv;
        LinearLayout secondColumn;
        ImageView favoriteLeft;
        ImageView favoriteRight;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textProduct = itemView.findViewById(R.id.nomeProduto);
            textPreco = itemView.findViewById(R.id.precoProduto);
            imgProduct1 = itemView.findViewById(R.id.imgProduct1);
            textProduct1 = itemView.findViewById(R.id.nomeProduto1);
            textPreco1 = itemView.findViewById(R.id.precoProduto1);
            cv = itemView.findViewById(R.id.cardView);
            secondColumn = itemView.findViewById(R.id.secondColumn);
            favoriteLeft = itemView.findViewById(R.id.ic_favorite1);
            favoriteRight = itemView.findViewById(R.id.ic_favorite);
        }
    }
}