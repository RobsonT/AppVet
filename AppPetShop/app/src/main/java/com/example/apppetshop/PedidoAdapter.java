package com.example.apppetshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.ItemDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Pet;
import com.example.apppetshop.model.Produto;

import java.text.SimpleDateFormat;
import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {
    List<Compra> pedidoList;
    Context context;

    ItemDAO itemDAO;
    ProdutoDAO produtoDAO;

    public PedidoAdapter(List<Compra> pedidoList) {
        this.pedidoList = pedidoList;
    }

    @Override
    public PedidoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pedido_list, parent, false);
        PedidoAdapter.ViewHolder viewHolder = new PedidoAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PedidoAdapter.ViewHolder holder, final int position) {

        itemDAO = ItemDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        Compra pedido = pedidoList.get(position);
        holder.idPedido.setText(String.valueOf(pedido.getId()));
        double valor = 0;
        for (Item i: itemDAO.getByCompra(pedido.getId())) {
            Produto p = produtoDAO.get(i.getIdProduto());
            valor += i.getQuantidade() * p.getPreco();
        }
        holder.precoPedido.setText(String.valueOf(valor));
        SimpleDateFormat spf=new SimpleDateFormat("dd/MM/yyyy");
        String date = spf.format(pedido.getData());
        holder.dataPedido.setText(date);
        holder.localPedido.setText("Ainda não implementado");
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

        public ViewHolder(View itemView) {
            super(itemView);
            idPedido= itemView.findViewById(R.id.id);
            precoPedido = itemView.findViewById(R.id.precoPedido);
            localPedido= itemView.findViewById(R.id.localPedido);
            dataPedido = itemView.findViewById(R.id.dataPedido);
            cv = itemView.findViewById(R.id.cvPedido);
        }

    }
}