package com.example.apppetshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.model.Pet;

import java.util.List;

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.ViewHolder> {
    List<Pet> petList;
    Context context;

    public PetAdapter(List<Pet> petList) {
        this.petList = petList;
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
        holder.imgPet.setImageBitmap(pet.getImage());
        holder.breedPet.setText(null/*Raça*/);
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
