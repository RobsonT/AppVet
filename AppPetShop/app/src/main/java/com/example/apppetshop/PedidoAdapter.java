package com.example.apppetshop;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {
    List<Compra> pedidoList;
    Context context;

    ItemDAO itemDAO;
    ProdutoDAO produtoDAO;

    private OnItemClickListener pedidoListener;

    public PedidoAdapter(List<Compra> pedidoList) {
        this.pedidoList = pedidoList;
    }

    public interface OnItemClickListener {
        void onPedidoDetail(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        pedidoListener = listener;
    }

    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_list, parent, false);
        PedidoAdapter.ViewHolder viewHolder = new PedidoAdapter.ViewHolder(view, pedidoListener);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PedidoAdapter.ViewHolder holder, final int position) {

        itemDAO = ItemDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        final Compra pedido = pedidoList.get(position);
        holder.idPedido.setText(String.valueOf(pedido.getId()));
        final double[] valor = {0};

        final List<Item> itens = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/itens")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Item item = document.toObject(Item.class);
                                if (item.getIdCompra().equals(pedido.getId())) {
                                    itens.add(item);
                                }
                            }
                        } else {
                            Log.d("PedidoAdapter", "Error getting documents: ", task.getException());
                        }
                    }
                });

        for (final Item i: itens) {
            final Produto[] p = {null};

            FirebaseFirestore.getInstance().collection("/produtos")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Produto prod = document.toObject(Produto.class);
                                    if (i.getIdProduto().equals(prod.getId())) {
                                        p[0] = prod;
                                        valor[0] += i.getQuantidade() * p[0].getPreco();
                                        holder.precoPedido.setText(String.valueOf(valor[0]));
                                    }
                                }
                            } else {
                                Log.d("Loja fragment", "Error getting documents: ", task.getException());
                            }
                        }
                    });
        }
        SimpleDateFormat spf=new SimpleDateFormat("dd/MM/yyyy");
        String date = spf.format(pedido.getData());
        holder.dataPedido.setText(date);
        holder.localPedido.setText(pedido.getLocal());
    }

    @Override
    public int getItemCount() {
        return pedidoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView idPedido;
        TextView precoPedido;
        TextView localPedido;
        TextView dataPedido;
        CardView cv;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            idPedido= itemView.findViewById(R.id.id);
            precoPedido = itemView.findViewById(R.id.precoPedido);
            localPedido= itemView.findViewById(R.id.localPedido);
            dataPedido = itemView.findViewById(R.id.dataPedido);
            cv = itemView.findViewById(R.id.cvPedido);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onPedidoDetail(position);
                    }
                }
            });
        }
    }
}