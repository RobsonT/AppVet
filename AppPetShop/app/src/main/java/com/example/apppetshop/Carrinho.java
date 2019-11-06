package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apppetshop.DAO.CompraDAO;
import com.example.apppetshop.DAO.ItemDAO;
import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Item;
import com.example.apppetshop.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class Carrinho extends Fragment {

    RecyclerView recyclerView;
    CartAdapter cartAdapter;
    List<Item> itens;
    static TextView valorTotal;

    ItemDAO itemDao;
    CompraDAO compraDAO;
    ProdutoDAO produtoDAO;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_carrinho,  container, false);
        final int clientId = Integer.parseInt(getArguments().getString("clientId"));

        itemDao = ItemDAO.getInstance();
        compraDAO = CompraDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        Compra compra = compraDAO.getUnconfirmed(clientId);

        LinearLayout listCart = v.findViewById(R.id.listCart);
        TextView warningCart = v.findViewById(R.id.warningCart);
        TextView moreProducts = v.findViewById(R.id.moreProducts);
        Button confirm = v.findViewById(R.id.confirm);
        valorTotal = v.findViewById(R.id.valorTotal);

        moreProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("clientId", String.valueOf(clientId));

                LojaFragment lojaFragment = new LojaFragment();
                lojaFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainer, lojaFragment);
                fragmentTransaction.commit();
            }
        });

        if(compra != null){
            listCart.setVisibility(View.VISIBLE);
            itens = new ArrayList<>();
            warningCart.setVisibility(View.GONE);
            valorTotal.setVisibility(View.VISIBLE);
            confirm.setVisibility(View.VISIBLE);
            itens = itemDao.getByCompra(compra.getId());
            warningCart.setVisibility(View.GONE);
            double value = 0;
            for (Item item: itens) {
                Produto p = produtoDAO.get(item.getIdProduto());
                value += item.getQuantidade() * p.getPreco();
            }
            valorTotal.setText(String.valueOf(value));
        }else{
            listCart.setVisibility(View.GONE);
            itens = new ArrayList<>();
            warningCart.setVisibility(View.VISIBLE);
            valorTotal.setVisibility(View.GONE);
            confirm.setVisibility(View.GONE);

        }
        cartAdapter = new CartAdapter(itens, clientId);

        recyclerView = v.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);
        return v;
    }
}