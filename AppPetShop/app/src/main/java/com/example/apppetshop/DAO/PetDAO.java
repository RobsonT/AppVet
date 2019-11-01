package com.example.apppetshop.DAO;

import com.example.apppetshop.model.Pet;
import java.util.ArrayList;
import java.util.List;

public class PetDAO implements Dao<Pet>{

    private List<Pet> pets;

    private static PetDAO instance;

    private PetDAO() {
        pets = new ArrayList<>();
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

    public List<Pet> getByClient(int idClient){
        List<Pet> petsClient = new ArrayList<>();
        for (Pet pet : pets) {
            if (pet.getIdCliente() == idClient)
                petsClient.add(pet);
        }
        return petsClient;
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