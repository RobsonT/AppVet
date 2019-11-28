package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    TextView name, email;

    private FirebaseAuth auth;

    static NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        verifyAuthentication();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);

        name = navigationView.getHeaderView(0).findViewById(R.id.nomeUsuario);
        email = navigationView.getHeaderView(0).findViewById(R.id.emailUsuario);

        FirebaseFirestore.getInstance().collection("/clientes")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.toObject(Cliente.class) != null) {
                            name.setText(documentSnapshot.toObject(Cliente.class).getNome());
                            email.setText(documentSnapshot.toObject(Cliente.class).getEmail());
                        }
                    }
                });

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(getIntent().getExtras() != null){
            Carrinho carrinho = new Carrinho();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, carrinho).commit();
            navigationView.setCheckedItem(R.id.navCart);
        }else {
            if (savedInstanceState == null) {
                LojaFragment lojaFragment = new LojaFragment();
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, lojaFragment).commit();
                break;
            case R.id.navCart:
                Carrinho carrinho = new Carrinho();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, carrinho).commit();
                break;
            case R.id.navFavorite:
                FavoriteList favoriteList = new FavoriteList();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, favoriteList).commit();
                break;
            case R.id.navPet:
                ListaPet listaPet = new ListaPet();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, listaPet).commit();
                break;
            case R.id.navService:
                ServicosCliente servicosCliente = new ServicosCliente();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, servicosCliente).commit();
                break;
            case R.id.navRequest:
                PedidoList pedidoList = new PedidoList();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, pedidoList).commit();
                break;
            case R.id.navSair:
                FirebaseAuth.getInstance().signOut();
                verifyAuthentication();
                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void verifyAuthentication(){
        Log.i("teste", auth.getUid());
        if(auth.getUid() == null){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
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