package com.example.apppetshop.DAO;

import com.example.apppetshop.R;
import com.example.apppetshop.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetDAO implements Dao<Pet>{

    private List<Pet> pets;

    private static PetDAO instance;

    private PetDAO() {
        pets = new ArrayList<>();
        Pet pet = new Pet();
        pet.setNome("a");
        pet.setImage(R.drawable.iconcachorro);
        pet.setIdRaca(0);
        pets.add(pet);
    }

    public static PetDAO getInstance(){
        if(instance == null){
            instance = new PetDAO();
        }

        return instance;
    }

    @Override
    public Pet get(int id) {
        return pets.get(id);
    }

    @Override
    public List<Pet> getAll() {
        return pets;
    }

    @Override
    public void save(Pet pet) {
        pets.add(pet);
    }

    @Override
    public void delete(Pet pet) {
        pets.remove(pet);
    }
}
