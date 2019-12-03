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
import android.widget.TextView;

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
    TextView message;
    PetAdapter petAdapter;
    List<Pet> pets;

    FirebaseAuth auth;

    PetDAO petDao;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_lista_pet, container, false);

        auth = FirebaseAuth.getInstance();

        petDao = PetDAO.getInstance();
        pets = new ArrayList<>();

        message = v.findViewById(R.id.textView15);
        recyclerView = v.findViewById(R.id.recyclerViewPet);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

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
                                    message.setVisibility(TextView.GONE);
                                }
                            }
                            if (pets.size() == 0)
                                message.setVisibility(TextView.VISIBLE);
                            petAdapter = new PetAdapter(pets);
                            petAdapter.setOnItemClickListener(new PetAdapter.OnItemClickListener() {
                                @Override
                                public void onItemDetail(int position) {
                                    showItem(position);
                                }

                                @Override
                                public void onItemDelete(int position) {
                                    removeItem(position);
                                }
                            });
                            recyclerView.setAdapter(petAdapter);
                        } else {
                            Log.d("listaPet", "Error getting documents: ", task.getException());
                        }
                    }
                });

        FloatingActionButton fab = v.findViewById(R.id.fab);

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
        if (resultCode == Activity.RESULT_OK) {
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
                                        Log.i("Funciona", "func");
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

    public void showItem(int position) {

    }

    public void removeItem(int position) {
        Pet pet = pets.get(position);
        pets.remove(position);
        petAdapter.notifyItemRemoved(position);
        PetDAO favoritoDAO = PetDAO.getInstance();
        favoritoDAO.delete(pet);
    }
}