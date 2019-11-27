package com.example.apppetshop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.ProdutoDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Produto;
import com.example.apppetshop.model.ServicoCliente;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    List<Favorito> favoriteList;
    Context context;

    FirebaseStorage storage;

    private OnItemClickListener favoriteListener;

    public FavoriteAdapter(List<Favorito> favoriteList) {
        this.favoriteList = favoriteList;
        storage = FirebaseStorage.getInstance();
    }

    public interface OnItemClickListener {
        void onItemDetail(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        favoriteListener = listener;
    }

    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favorite_list, parent, false);
        FavoriteAdapter.ViewHolder viewHolder = new FavoriteAdapter.ViewHolder(view, favoriteListener);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoriteAdapter.ViewHolder holder, final int position) {
        final Favorito favorite = favoriteList.get(position);
        ProdutoDAO produtoDAO = ProdutoDAO.getInstance();
        final Produto[] produto = {null};

        FirebaseFirestore.getInstance().collection("/produtos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Produto prod = document.toObject(Produto.class);
                                if (prod.getId().equals(favorite.getIdProduto())) {
                                    produto[0] = prod;
                                }
                            }
                        } else {
                            Log.d("ServicosCliente", "Error getting documents: ", task.getException());
                        }
                    }
                });

        holder.nameFavorite.setText(produto[0].getNome());

        final Bitmap[] bitmap = {null};
        final long ONE_MEGABYTE = 1024 * 1024;
        storage.getReferenceFromUrl(produto[0].getImagem()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        });

        holder.imgFavorite.setImageBitmap(bitmap[0]);
        holder.priceFavorite.setText(String.valueOf(produto[0].getPreco()));
    }

    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFavorite;
        TextView nameFavorite;
        TextView priceFavorite;
        CardView cv;

        public ViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            nameFavorite = itemView.findViewById(R.id.nameFavorite);
            priceFavorite = itemView.findViewById(R.id.priceFavorite);
            cv = itemView.findViewById(R.id.cardViewFavorite);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                            listener.onItemDetail(position);
                    }
                }
            });
        }

    }
}