package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class PetDAO implements Dao<Pet>{

    private List<Pet> pets = new ArrayList<>();


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
