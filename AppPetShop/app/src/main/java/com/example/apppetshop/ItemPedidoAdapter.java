package com.example.apppetshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.ItemDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ItemPedidoAdapter extends RecyclerView.Adapter<ItemPedidoAdapter.ViewHolder> {
    List<Item> itemList;
    Context context;

    private OnItemClickListener itemListener;

    public ItemPedidoAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    public interface OnItemClickListener {
        void onItemDetail(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        itemListener = listener;
    }

    @Override
    public ItemPedidoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pedido_list, parent, false);
        ItemPedidoAdapter.ViewHolder viewHolder = new ItemPedidoAdapter.ViewHolder(view, itemListener);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemPedidoAdapter.ViewHolder holder, final int position) {

        double valor = 0;

        FirebaseFirestore.getInstance().collection("produtos")
                .whereEqualTo("id", itemList.get(position).getIdProduto())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Produto prod = document.toObject(Produto.class);
                            holder.nomePedido.setText(prod.getNome());
                            holder.precoPedido.setText(String.valueOf(prod.getPreco()));
                            holder.quantidadePedido.setText(String.valueOf(itemList.get(position).getQuantidade()));
                            holder.valorPedido.setText(String.valueOf(prod.getPreco() * itemList.get(position).getQuantidade()));

                            final long ONE_MEGABYTE = 1024 * 1024 * 6;
                            FirebaseStorage.getInstance().getReferenceFromUrl(prod.getImagem()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                @Override
                                public void onSuccess(byte[] bytes) {
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                    holder.imgPedido.setImageBitmap(bitmap);
                                }
                            });
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nomePedido;
        TextView quantidadePedido;
        TextView precoPedido;
        TextView valorPedido;
        ImageView imgPedido;
        CardView cv;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            nomePedido = itemView.findViewById(R.id.nomeItemPedido);
            precoPedido = itemView.findViewById(R.id.precoItemPedido);
            quantidadePedido= itemView.findViewById(R.id.qntItemPedido);
            valorPedido = itemView.findViewById(R.id.valorItemPedido);
            imgPedido = itemView.findViewById(R.id.imgItemPedido);
            cv = itemView.findViewById(R.id.cvItemPedido);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemDetail(position);
                    }
                }
            });
        }
    }
}