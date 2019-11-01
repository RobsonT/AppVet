package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Pet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ListaPet extends Fragment {

    RecyclerView recyclerView;
    PetAdapter petAdapter;
    List<Pet> pets;
    int clientId;

    PetDAO petDao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lista_pet,  container, false);

        clientId = Integer.parseInt(getArguments().getString("clientId"));

        petDao = PetDAO.getInstance();
        pets = petDao.getByClient(clientId);

        petAdapter = new PetAdapter(pets);

        FloatingActionButton fab = v.findViewById(R.id.fab);

        recyclerView = v.findViewById(R.id.recyclerViewPet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(petAdapter);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), CadastroPet.class);
                i.putExtra("clientId", String.valueOf(clientId));
                startActivityForResult(i, 0);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            pets = petDao.getByClient(clientId);
            petAdapter = new PetAdapter(pets);
            recyclerView.setAdapter(petAdapter);
        }
    }
}