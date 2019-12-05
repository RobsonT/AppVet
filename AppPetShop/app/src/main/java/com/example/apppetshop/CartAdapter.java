package com.example.apppetshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.CompraDAO;
import com.example.apppetshop.DAO.ItemDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    List<Item> itemList;
    Context context;
    Item item;

    CompraDAO compraDAO;
    ItemDAO itemDAO;
    ProdutoDAO produtoDAO;

    private FirebaseAuth auth;
    private OnItemClickListener cartListener;

    FirebaseStorage storage;

    public CartAdapter(List<Item> itemList) {
        this.itemList = itemList;
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public interface OnItemClickListener {
        void onItemDelete(int position);
        void onItemDetails(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
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
        final Produto[] product = {null};

        final Bitmap[] bitmap = {null};
        final long ONE_MEGABYTE = 1024 * 1024;

        FirebaseFirestore.getInstance().collection("/produtos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            item = itemList.get(position);
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto prod = document.toObject(Produto.class);
                                if (item.getIdProduto().equals(prod.getId())) {
                                    holder.name.setText(prod.getNome());
                                    holder.price.setText("R$" + String.valueOf(prod.getPreco()));
                                    holder.quantity.setText(String.valueOf(item.getQuantidade()));
                                    holder.close.setTag(String.valueOf(position));
                                    holder.valorText.setText("R$" + prod.getPreco() * item.getQuantidade());
                                    storage.getReferenceFromUrl(prod.getImagem()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            holder.imgCart.setImageBitmap(bitmap[0]);
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

        holder.increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
                holder.quantity.setText(String.valueOf(currentQuantity + 1));
                item = itemList.get(holder.getAdapterPosition());
                item.setQuantidade(item.getQuantidade() + 1);
                itemDAO.save(item);

                FirebaseFirestore.getInstance().collection("produtos")
                        .whereEqualTo("id", item.getIdProduto())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Produto prod = document.toObject(Produto.class);
                                        holder.valorText.setText("R$" + prod.getPreco() * item.getQuantidade());
                                            double currentValue = Double.parseDouble(Carrinho.valorTotal.getText().toString());
                                            Carrinho.valorTotal.setText(String.valueOf(currentValue + prod.getPreco()));
                                    }
                                } else {
                                    Log.d("cartAdapter", "Error getting documents: ", task.getException());
                                }
                            }
                        });
            }
        });

        holder.decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentQuantity = Integer.parseInt(holder.quantity.getText().toString());
                item = itemList.get(holder.getAdapterPosition());
                if (item.getQuantidade() > 1) {
                    holder.quantity.setText(String.valueOf(currentQuantity - 1));
                    item.setQuantidade(item.getQuantidade() - 1);
                    itemDAO.save(item);

                    FirebaseFirestore.getInstance().collection("produtos")
                            .whereEqualTo("id", item.getIdProduto())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            Produto prod = document.toObject(Produto.class);
                                            holder.valorText.setText("R$" + prod.getPreco() * item.getQuantidade());
                                            double currentValue = Double.parseDouble(Carrinho.valorTotal.getText().toString());
                                            Carrinho.valorTotal.setText(String.valueOf(currentValue - prod.getPreco()));
                                        }
                                    } else {
                                        Log.d("cartAdapter", "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                } else {
                    Toast.makeText(context, "Quantidade minima alcançada", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
        TextView valorText;

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
            valorText = itemView.findViewById(R.id.valorTotalItem);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        Log.i("testep", String.valueOf(position));
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemDetails(position);
                        }
                    }
                }
            });

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        Log.i("testep", String.valueOf(position));
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemDelete(position);
                        }
                    }
                }
            });
        }
    }
}