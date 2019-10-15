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

import com.example.apppetshop.model.Produto;

import java.util.ArrayList;

public class LojaFragment extends Fragment {

    RecyclerView recyclerView;
    ProductAdapter productAdapter;
    ArrayList<Produto> products1;
    ArrayList<Produto> products2;

    public static final String[] names = {"Breaking Bad" ,"Rick and Morty","Ricardo", "Pedro"};
    public static final int[] images = {R.drawable.icongato ,R.drawable.iconcachorro,R.drawable.icongato, R.drawable.iconcachorro};

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_loja,  container, false);

        products1= new ArrayList<>();
        products2= new ArrayList<>();

        for (int i = 0; i < names.length; i++) {
            Produto product = new Produto();

            product.setName(names[i]);
            product.setImage(images[i]);
            if(i % 2 == 0)
                products1.add(product);
            else
                products2.add(product);
        }


        productAdapter = new ProductAdapter(products1, products2);

        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(productAdapter);

        return v;
    }
}
