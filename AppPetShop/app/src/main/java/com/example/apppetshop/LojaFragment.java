package com.example.apppetshop;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Produto;

import java.util.ArrayList;
import java.util.List;

public class LojaFragment extends Fragment {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Produto> products1;
    ArrayList<Produto> products2;

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_loja,  container, false);

        int clientId = Integer.parseInt(getArguments().getString("clientId"));

        products1= new ArrayList<>();
        products2= new ArrayList<>();

        ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
        List<Produto> products = produtoDAO.getAll();
        for (int i = 0; i < products.size(); i++) {
            Produto product = products.get(i);
            if(i % 2 == 0)
                products1.add(product);
            else
                products2.add(product);
        }


        productAdapter = new ProductAdapter(products1, products2, clientId);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAdapter);

        return v;
    }
}
