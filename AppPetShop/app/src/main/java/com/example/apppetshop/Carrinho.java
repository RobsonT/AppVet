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
    static LinearLayout listCart;
    static TextView warningCart;
    static Button confirm;
    static LinearLayout valueField;
    TextView moreProducts;

    private ItemDAO itemDao;
    private CompraDAO compraDAO;
    private ProdutoDAO produtoDAO;

    int clientId;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_carrinho,  container, false);

        initialize(v);

        Compra compra = compraDAO.getUnconfirmed(clientId);

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
                MainActivity.navigationView.setCheckedItem(R.id.navHome);
            }
        });

        if(compra != null){
            hideItens();
            itens = itemDao.getByCompra(compra.getId());
            double value = 0;
            for (Item item: itens) {
                Produto p = produtoDAO.get(item.getIdProduto());
                value += item.getQuantidade() * p.getPreco();
            }
            valorTotal.setText(String.valueOf(value));
        }else{
            showItens();
            itens = new ArrayList<>();
        }
        cartAdapter = new CartAdapter(itens, clientId);

        recyclerView = v.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnItemClickListener(new CartAdapter.OnItemClickListener() {
            @Override
            public void onItemDelete(int position) {
                removeItem(position);
            }
        });
        return v;
    }

    public void hideItens(){
        listCart.setVisibility(View.VISIBLE);
        itens = new ArrayList<>();
        warningCart.setVisibility(View.GONE);
        valueField.setVisibility(View.VISIBLE);
        confirm.setVisibility(View.VISIBLE);
    }

    public void showItens(){
        listCart.setVisibility(View.GONE);
        warningCart.setVisibility(View.VISIBLE);
        valueField.setVisibility(View.GONE);
        confirm.setVisibility(View.GONE);
    }

    public void initialize(View v){
        clientId = Integer.parseInt(getArguments().getString("clientId"));

        itemDao = ItemDAO.getInstance();
        compraDAO = CompraDAO.getInstance();
        produtoDAO = ProdutoDAO.getInstance();

        listCart = v.findViewById(R.id.listCart);
        warningCart = v.findViewById(R.id.warningCart);
        moreProducts = v.findViewById(R.id.moreProducts);
        confirm = v.findViewById(R.id.confirm);
        valorTotal = v.findViewById(R.id.valorTotal);
        valueField = v.findViewById(R.id.valorCarrinho);
    }

    public void removeItem(int position){
        Item item = itens.get(position);
        itens.remove(position);
        cartAdapter.notifyItemRemoved(position);
        ItemDAO itemDAO = ItemDAO.getInstance();
        itemDAO.delete(item);
        CompraDAO compraDAO = CompraDAO.getInstance();
        int compraId = compraDAO.getUnconfirmed(clientId).getId();
        List<Item> itens = itemDAO.getByCompra(compraId);
        if(itens.size() == 0){
            compraDAO.delete(compraDAO.get(compraId));
            Carrinho.listCart.setVisibility(View.GONE);
            Carrinho.warningCart.setVisibility(View.VISIBLE);
            Carrinho.valueField.setVisibility(View.GONE);
            Carrinho.confirm.setVisibility(View.GONE);
        }else {

            Carrinho.valorTotal.setText(String.valueOf(this.getTotalValue()));
        }
    }

    public double getTotalValue(){
        double value = 0;
        ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
        for (Item i : this.itens) {
            Produto p = produtoDAO.get(i.getIdProduto());
            value += i.getQuantidade() * p.getPreco();
        }
        return value;
    }
}