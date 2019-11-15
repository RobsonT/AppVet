package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Bundle bundle;

    TextView name, email;

    private FirebaseUser dbUser;

    static NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbUser = FirebaseAuth.getInstance().getCurrentUser();

        bundle = new Bundle();
        bundle.putString("clientId", dbUser.getUid());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);

        ClienteDAO clienteDAO = ClienteDAO.getInstance();
        Cliente cliente = clienteDAO.get(dbUser.getUid());

        navigationView = findViewById(R.id.navView);

        name = navigationView.getHeaderView(0).findViewById(R.id.nomeUsuario);
        email = navigationView.getHeaderView(0).findViewById(R.id.emailUsuario);

        name.setText(cliente.getNome());
        email.setText(cliente.getEmail());

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(getIntent().getExtras().getString("compra") != null){
            Carrinho carrinho = new Carrinho();
            carrinho.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, carrinho).commit();
            navigationView.setCheckedItem(R.id.navCart);
        }else {
            if (savedInstanceState == null) {
                LojaFragment lojaFragment = new LojaFragment();
                lojaFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, lojaFragment).commit();
                navigationView.setCheckedItem(R.id.navHome);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.navHome:
                LojaFragment lojaFragment = new LojaFragment();
                lojaFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, lojaFragment).commit();
                break;
            case R.id.navCart:
                Carrinho carrinho = new Carrinho();
                carrinho.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, carrinho).commit();
                break;
            case R.id.navFavorite:
                FavoriteList favoriteList = new FavoriteList();
                favoriteList.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, favoriteList).commit();
                break;
            case R.id.navPet:
                ListaPet listaPet = new ListaPet();
                listaPet.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, listaPet).commit();
                break;
            case R.id.navService:
                ServicosCliente servicosCliente = new ServicosCliente();
                servicosCliente.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, servicosCliente).commit();
                break;
            case R.id.navRequest:
                PedidoList pedidoList = new PedidoList();
                pedidoList.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, pedidoList).commit();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}