package com.example.apppetshop;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Produto;

import java.util.List;

public class LojaFragment extends Fragment {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_loja,  container, false);

        int clientId = Integer.parseInt(getArguments().getString("clientId"));

        ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
        List<Produto> products = produtoDAO.getAll();

        productAdapter = new ProductAdapter(products, clientId);
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAdapter);

        return v;
    }
}
