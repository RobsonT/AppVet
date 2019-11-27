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
    Compra compra;

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

        FirebaseFirestore.getInstance().collection("/compras")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Compra c = document.toObject(Compra.class);
                                if (!c.isConfirmado() && c.getId().equals(auth.getUid())) {
                                    compra = c;
                                }
                            }
                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });

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
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto prod = document.toObject(Produto.class);
                                if (item.getIdProduto().equals(prod.getId())) {
                                    product[0] = prod;
                                    holder.name.setText(product[0].getNome());
                                    holder.price.setText("R$" + String.valueOf(product[0].getPreco()));
                                    holder.quantity.setText(String.valueOf(item.getQuantidade()));
                                    holder.close.setTag(String.valueOf(position));
                                    storage.getReferenceFromUrl(product[0].getImagem()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
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

                updateTotalValue();
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

                    updateTotalValue();
                } else {
                    Toast.makeText(context, "Quantidade minima alcançada", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateTotalValue() {
        final List<Item> itens = new ArrayList<>();

        final double[] value = {0};
        FirebaseFirestore.getInstance().collection("/itens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item i = document.toObject(Item.class);
                                if (i.getIdCompra().equals(compra.getId())) {
                                    final Produto[] p = {null};

                                    FirebaseFirestore.getInstance().collection("/produtos")
                                            .get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            Produto prod = document.toObject(Produto.class);
                                                            if (item.getIdProduto().equals(prod.getId())) {
                                                                p[0] = prod;
                                                            }
                                                        }
                                                    } else {
                                                        Log.d("cartAdapter", "Error getting documents: ", task.getException());
                                                    }
                                                }
                                            });

                                    value[0] += item.getQuantidade() * p[0].getPreco();
                                }
                            }

                            Carrinho.valorTotal.setText(String.valueOf(value[0]));
                        } else {
                            Log.d("cartAdapter", "Error getting documents: ", task.getException());
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

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemDelete(position);
                    }
                }
            });
        }
    }
}