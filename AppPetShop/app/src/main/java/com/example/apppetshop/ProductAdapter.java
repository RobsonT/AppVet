package com.example.apppetshop;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apppetshop.DAO.FavoritoDAO;
import com.example.apppetshop.model.Favorito;
import com.example.apppetshop.model.Pet;
import com.example.apppetshop.model.Produto;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    List<Produto> productList;
    FavoritoDAO favoritoDao;
    Context context;
    Favorito favorito;
    Produto product;

    FirebaseStorage storage;

    private FirebaseAuth auth;

    public ProductAdapter(List<Produto> productList) {
        this.productList = productList;
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        favoritoDao = FavoritoDAO.getInstance();
        final List<Favorito> favoritos = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("/favoritos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Favorito fav = document.toObject(Favorito.class);
                                if (fav.getIdCliente().equals(auth.getUid())) {
                                    favoritos.add(fav);
                                }
                            }
                        } else {
                            Log.d("ProductAdapter", "Error getting documents: ", task.getException());
                        }
                    }
                });

        product = productList.get(position);
        holder.textProduct.setText(product.getNome());

        final Bitmap[] bitmap = {null};
        final long ONE_MEGABYTE = 1024 * 1024;
        storage.getReferenceFromUrl(product.getImagem()).getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                bitmap[0] = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.imgProduct.setImageBitmap(bitmap[0]);
            }
        });

        holder.textPreco.setText("R$" + product.getPreco());

        for(Favorito f: favoritos){
            if(f.getIdProduto().equals(product.getId())){
                holder.favoriteLeft.setImageResource(R.drawable.ic_favorite_black_24dp);
                holder.favoriteLeft.setTag(product.getId()+"-true");
            }
        }
        if(!holder.favoriteLeft.getTag().toString().equals(product.getId()+"-true")){
            holder.favoriteLeft.setTag(product.getId()+"-false");
        }

        holder.favoriteLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                favorito = new Favorito();
                String[] tag = holder.favoriteLeft.getTag().toString().split("-");
                if(tag[1].equals("false")) {
                    holder.favoriteLeft.setImageResource(R.drawable.ic_favorite_black_24dp);
                    holder.favoriteLeft.setTag(tag[0]+"-true");
                    favorito.setIdProduto(tag[0]);
                    favorito.setIdCliente(auth.getUid());
                    favoritoDao.save(favorito);
                }else{
                    holder.favoriteLeft.setImageResource(R.drawable.ic_favorite);
                    favorito.setIdProduto(tag[0]);
                    favorito.setIdCliente(auth.getUid());
                    favoritoDao.delete(favorito);
                    holder.favoriteLeft.setTag(tag[0]+"-false");
                }
            }
        });

        holder.firstColumn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent( view.getContext(), ProdutoDescricao.class);
                i.putExtra( "idProduto", String.valueOf(productList.get(position).getId()));
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgProduct;
        TextView textProduct;
        TextView textPreco;
        CardView cv;
        LinearLayout firstColumn;
        ImageView favoriteLeft;

        public ViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            textProduct = itemView.findViewById(R.id.nomeProduto);
            textPreco = itemView.findViewById(R.id.precoProduto);
            cv = itemView.findViewById(R.id.cardView);
            firstColumn = itemView.findViewById(R.id.firstColumn);
            favoriteLeft = itemView.findViewById(R.id.ic_favorite1);
        }
    }
}