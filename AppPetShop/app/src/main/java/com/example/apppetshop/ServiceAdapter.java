package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Pet;
import com.example.apppetshop.model.ServicoCliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    List<ServicoCliente> serviceList;
    Context context;
    ServicoCliente servico;
    PetDAO petDAO;
    FirebaseStorage storage;

    public ServiceAdapter(List<ServicoCliente> serviceList) {
        this.serviceList = serviceList;
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_lista_servico, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        petDAO = PetDAO.getInstance();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        servico = serviceList.get(position);
        final Pet[] pet = {null};

        FirebaseFirestore.getInstance().collection("/pets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Pet p = document.toObject(Pet.class);
                                if (p.getId().equals(servico.getIdPet())) {
                                    pet[0] = p;

                                    final long ONE_MEGABYTE = 1024 * 1024 * 6;
                                    final Bitmap[] bitmap = {null};
                                    storage.getReferenceFromUrl(pet[0].getImage()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                        @Override
                                        public void onSuccess(byte[] bytes) {
                                            bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                            holder.imgPet.setImageBitmap(bitmap[0]);
                                        }
                                    });
                                }
                            }
                        } else {
                            Log.d("ServicosCliente", "Error getting documents: ", task.getException());
                        }
                    }
                });

        holder.nomeServico.setText(servico.getNomeServico());

        SimpleDateFormat spf=new SimpleDateFormat("dd/MM/yyyy hh:mm");
        String date = spf.format(servico.getData());
        holder.dataServico.setText(date);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPet;
        TextView dataServico;
        TextView nomeServico;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPet = itemView.findViewById(R.id.imgService);
            dataServico = itemView.findViewById(R.id.dataService);
            nomeServico = itemView.findViewById(R.id.nomeService);
        }
    }
}