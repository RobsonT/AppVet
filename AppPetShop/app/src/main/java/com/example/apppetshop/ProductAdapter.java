package com.example.apppetshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.model.Produto;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Produto> productList1;
    List<Produto> productList2;
    Context context;

    public ProductAdapter(List<Produto> productList1, List<Produto> productList2) {
        this.productList1 = productList1;
        this.productList2 = productList2;
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
        Produto product = productList1.get(position);
        holder.textProduct.setText(product.getName());
        holder.imgProduct.setImageResource(product.getImage());
        if (position < productList2.size()) {
            product = productList2.get(position);
            holder.textProduct1.setText(product.getName());
            holder.imgProduct1.setImageResource(product.getImage());
        }else{
            holder.secondColumn.setVisibility(View.INVISIBLE);
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView textProduct;
        ImageView imgProduct1;
        TextView textProduct1;
        CardView cv;
        LinearLayout secondColumn;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textProduct = itemView.findViewById(R.id.textProduct);
            imgProduct1 = itemView.findViewById(R.id.imgProduct1);
            textProduct1 = itemView.findViewById(R.id.textProduct1);
            cv = itemView.findViewById(R.id.cardView);
            secondColumn = itemView.findViewById(R.id.secondColumn);
        }

    }
}