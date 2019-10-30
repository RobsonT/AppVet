package com.example.apppetshop;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Cliente;
import com.example.apppetshop.model.Pet;
import com.example.apppetshop.model.ServicoCliente;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    List<ServicoCliente> serviceList;
    Context context;
    ServicoCliente servico;
    PetDAO petDAO;
    int clientId;

    public ServiceAdapter(List<ServicoCliente> serviceList) {
        this.serviceList = serviceList;
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
        Pet pet = petDAO.get(servico.getIdPet());
        holder.nomeServico.setText(servico.getNomeServico());
        holder.imgPet.setImageBitmap(pet.getImage());
        holder.dataServico.setText(servico.getData().toString());
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