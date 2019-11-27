package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Pet;
import com.example.apppetshop.model.ServicoCliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListaPet extends Fragment {

    RecyclerView recyclerView;
    PetAdapter petAdapter;
    List<Pet> pets;

    FirebaseAuth auth;

    PetDAO petDao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lista_pet,  container, false);

        auth = FirebaseAuth.getInstance();

        petDao = PetDAO.getInstance();
        pets = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/pets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Pet pet = document.toObject(Pet.class);
                                if (pet.getIdCliente().equals(auth.getUid())) {
                                    pets.add(pet);
                                }
                            }
                        } else {
                            Log.d("ServicosCliente", "Error getting documents: ", task.getException());
                        }
                    }
                });

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
                startActivityForResult(i, 0);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            pets = new ArrayList<>();

            FirebaseFirestore.getInstance().collection("/pets")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Pet pet = document.toObject(Pet.class);
                                    if (pet.getIdCliente().equals(auth.getUid())) {
                                        pets.add(pet);
                                    }
                                }
                            } else {
                                Log.d("ServicosCliente", "Error getting documents: ", task.getException());
                            }
                        }
                    });
            petAdapter = new PetAdapter(pets);
            recyclerView.setAdapter(petAdapter);
        }
    }
}