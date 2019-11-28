package com.example.apppetshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.model.Pet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    List<Pet> petList;
    Context context;
    FirebaseStorage storage;

    public PetAdapter(List<Pet> petList) {
        this.petList = petList;
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public PetAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_list, parent, false);
        PetAdapter.ViewHolder viewHolder = new PetAdapter.ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PetAdapter.ViewHolder holder, final int position) {
        Pet pet = petList.get(position);
        holder.namePet.setText(pet.getNome());
        final long ONE_MEGABYTE = 1024 * 1024 * 6;
        final Bitmap[] bitmap = {null};
        storage.getReferenceFromUrl(pet.getImage()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imgPet.setImageBitmap(bitmap[0]);
            }
        });
        holder.breedPet.setText(pet.getRaca());
    }

    @Override
    public int getItemCount() {
        return petList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPet;
        TextView namePet;
        TextView breedPet;
        CardView cv;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPet = itemView.findViewById(R.id.imgPet);
            namePet = itemView.findViewById(R.id.namePet);
            breedPet = itemView.findViewById(R.id.breedPet);
            cv = itemView.findViewById(R.id.cardView);
        }

    }
}
