package com.example.apppetshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.ItemDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Produto;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<Item> itemList;
    int clientId;
    Context context;
    Item item;
    double valor = 0;

    public CartAdapter(List<Item> itemList, int clientId) {
        this.itemList = itemList;
        this.clientId = clientId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        valor = 15;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            item = itemList.get(position);

            final ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
            final Produto product = produtoDAO.get(item.getIdProduto());

        Log.v("valor", String.valueOf(valor));
            holder.name.setText(product.getNome());
            holder.imgCart.setImageResource(product.getImagem());
            holder.price.setText("R$" + String.valueOf(product.getPreco()));
            holder.quantity.setText(String.valueOf(item.getQuantidade()));
            holder.increment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
                    holder.quantity.setText(String.valueOf(currentQuantity + 1));
                    item.setQuantidade(item.getQuantidade() + 1);
                    ItemDAO itemDAO = ItemDAO.getInstance();
                    itemDAO.updateQuantity(item);

                    valor = product.getPreco() * currentQuantity;
                }
            });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
                holder.quantity.setText(String.valueOf(currentQuantity - 1));
                item.setQuantidade(item.getQuantidade() - 1);
                ItemDAO itemDAO = ItemDAO.getInstance();
                itemDAO.updateQuantity(item);
                valor = produtoDAO.get(item.getIdProduto()).getPreco() * currentQuantity;
            }
        });

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDAO itemDAO = ItemDAO.getInstance();
                itemDAO.delete(item);
                holder.cv.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgCart;
        TextView price;
        TextView name;
        TextView close;
        CardView cv;
        TextView quantity;
        Button increment;
        Button decrement;

        public ViewHolder(View itemView) {
            super(itemView);
            imgCart = itemView.findViewById(R.id.imageCart);
            price = itemView.findViewById(R.id.priceCart);
            name = itemView.findViewById(R.id.nameCart);
            close = itemView.findViewById(R.id.close);
            cv = itemView.findViewById(R.id.cvCart);
            quantity = itemView.findViewById(R.id.quantity);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);
        }
    }

    public double getValue(){
        return this.valor;
    }
}