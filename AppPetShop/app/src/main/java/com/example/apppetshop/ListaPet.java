package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class ListaPet extends Fragment {

    RecyclerView recyclerView;
    PetAdapter petAdapter;
    List<Pet> pets;

    PetDAO petDao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lista_pet,  container, false);

        petDao = PetDAO.getInstance();
        pets = petDao.getAll();

        petAdapter = new PetAdapter(pets);

        recyclerView = v.findViewById(R.id.recyclerViewPet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(petAdapter);

        return v;
    }
}
