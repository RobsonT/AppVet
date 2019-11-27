package com.example.apppetshop.DAO;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.apppetshop.model.Pet;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PetDAO {

    private static PetDAO instance;
    private FirebaseFirestore db;

    private PetDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public static PetDAO getInstance() {
        if (instance == null) {
            instance = new PetDAO();
        }
        return instance;
    }

    public void save(final Pet pet, Uri uriPet) {
        final String id = db.collection("pets").document().getId();
        pet.setId(id);
        String filename = id;
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);
        ref.putFile(uriPet).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        pet.setImage(uri.toString());
                        db.collection("clientes")
                                .document(id)
                                .set(pet)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i("petDao", "Sucesso");

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.i("petDao", e.getMessage());
                                    }
                                });
                    }
                });
            }
        });
    }

    public void delete(Pet pet) {
        db.collection("pets").document(pet.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("petDAO", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("petDAO", "Error deleting document", e);
                    }
                });
    }
}