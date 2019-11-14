package com.example.apppetshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.CompraDAO;
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
    int compraId;

    CompraDAO compraDAO;
    ItemDAO itemDAO;
    ProdutoDAO produtoDAO;

    private OnItemClickListener cartListener;

    public CartAdapter(List<Item> itemList, int clientId) {
        this.itemList = itemList;
        this.clientId = clientId;
    }

    public interface OnItemClickListener{
        void onItemDelete(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        cartListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, cartListener);

        compraDAO = CompraDAO.getInstance();
        itemDAO = ItemDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        item = itemList.get(position);
        compraId = compraDAO.getUnconfirmed(clientId).getId();
        final Produto product = produtoDAO.get(item.getIdProduto());

        holder.name.setText(product.getNome());
        holder.imgCart.setImageResource(product.getImagem());
        holder.price.setText("R$" + String.valueOf(product.getPreco()));
        holder.quantity.setText(String.valueOf(item.getQuantidade()));
        holder.close.setTag(String.valueOf(position));
        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
                holder.quantity.setText(String.valueOf(currentQuantity + 1));
                item = itemDAO.get(holder.getAdapterPosition());
                item.setQuantidade(item.getQuantidade() + 1);
                itemDAO.updateQuantity(item);

                updateTotalValue();

            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
                item = itemDAO.get(holder.getAdapterPosition());
                if (item.getQuantidade() > 1) {
                    holder.quantity.setText(String.valueOf(currentQuantity - 1));
                    item.setQuantidade(item.getQuantidade() - 1);
                    itemDAO.updateQuantity(item);

                    updateTotalValue();
                }else{
                    Toast.makeText(context,"Quantidade minima alcançada", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateTotalValue(){
        List<Item> itens = itemDAO.getByCompra(compraId);
        double value = 0;
        for (Item item: itens) {
            Produto p = produtoDAO.get(item.getIdProduto());
            value += item.getQuantidade() * p.getPreco();
        }
        Carrinho.valorTotal.setText(String.valueOf(value));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCart;
        TextView price;
        TextView name;
        TextView close;
        CardView cv;
        TextView quantity;
        Button increment;
        Button decrement;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgCart = itemView.findViewById(R.id.imageCart);
            price = itemView.findViewById(R.id.priceCart);
            name = itemView.findViewById(R.id.nameCart);
            close = itemView.findViewById(R.id.close);
            cv = itemView.findViewById(R.id.cvCart);
            quantity = itemView.findViewById(R.id.quantity);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemDelete(position);
                    }
                }
            });
        }
    }
}