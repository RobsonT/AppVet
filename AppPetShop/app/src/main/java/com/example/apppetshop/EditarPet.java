package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.model.Pet;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class
EditarPet extends AppCompatActivity {

    EditText nomePet;
    EditText nascimentoPet;
    EditText racaPet;
    EditText pesoPet;
    ImageView caoPet;
    ImageView gatoPet;
    ImageView machoPet;
    ImageView femeaPet;
    ImageView simPet;
    ImageView naoPet;
    ImageView pequenoPet;
    ImageView medioPet;
    ImageView grandePet;
    Button confirmar;
    Button cancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pet);

        nomePet = findViewById(R.id.nomePet);
        nascimentoPet = findViewById(R.id.nascimentoPet);
        racaPet = findViewById(R.id.racaPet);
        pesoPet = findViewById(R.id.pesoPet);
        caoPet = findViewById(R.id.caoPet);
        gatoPet = findViewById(R.id.gatoPet);
        machoPet = findViewById(R.id.machoPet);
        femeaPet = findViewById(R.id.femeaPet);
        simPet = findViewById(R.id.simPet);
        naoPet = findViewById(R.id.naoPet);
        pequenoPet = findViewById(R.id.pequenoPet);
        medioPet = findViewById(R.id.medioPet);
        grandePet = findViewById(R.id.grandePet);
        confirmar = findViewById(R.id.confirmarPet);
        cancelar = findViewById(R.id.cancelarPet);

        final String idPet = getIntent().getExtras().getString("idPet");

        setClicklistener();

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent retorno = new Intent();

                setResult(Activity.RESULT_CANCELED, retorno);
                finish();
            }
        });

        FirebaseFirestore.getInstance().collection("/pets")
                .whereEqualTo("id", idPet)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                final Pet pet = document.toObject(Pet.class);
                                nomePet.setText(pet.getNome());
                                racaPet.setText(pet.getRaca());
                                pesoPet.setText(String.valueOf(pet.getPeso()));

                                SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                                final String date = spf.format(pet.getNascimento());
                                nascimentoPet.setText(date);

                                if (pet.getTipo().equals("cachorro"))
                                    caoPet.setImageResource(R.drawable.iconcachorroselected);
                                else
                                    gatoPet.setImageResource(R.drawable.icongatoselected);

                                if (pet.getSexo().equals("macho"))
                                    machoPet.setImageResource(R.drawable.iconmasculinoselected);
                                else
                                    femeaPet.setImageResource(R.drawable.iconfemininoselected);

                                if (pet.isCastrado())
                                    simPet.setImageResource(R.drawable.positivoselected);
                                else
                                    naoPet.setImageResource(R.drawable.negativoselected);

                                if (pet.getPorte().equals("pequeno"))
                                    pequenoPet.setImageResource(R.drawable.iconportepequenoselected);
                                else if (pet.getPorte().equals("medio"))
                                    medioPet.setImageResource(R.drawable.iconportemedioselected);
                                else
                                    grandePet.setImageResource(R.drawable.iconportegrandeselected);

                                confirmar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Pet p = new Pet();
                                        p.setId(idPet);
                                        p.setIdCliente(FirebaseAuth.getInstance().getUid());
                                        p.setImage(pet.getImage());
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        Date date = pet.getNascimento();
                                        try {
                                            date = sdf.parse(nascimentoPet.getText().toString());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        p.setNascimento(date);
                                        p.setNome(nomePet.getText().toString());
                                        p.setPeso(Double.parseDouble(pesoPet.getText().toString()));
                                        p.setRaca(racaPet.getText().toString());

                                        if(gatoPet.getTag().equals("true"))
                                            p.setTipo("gato");
                                        else
                                            p.setTipo("cachorro");

                                        if(machoPet.getTag().equals("true"))
                                            p.setSexo("macho");
                                        else
                                            p.setSexo("femea");

                                        if(simPet.getTag().equals("true"))
                                            p.setCastrado(true);
                                        else
                                            p.setCastrado(false);

                                        if(pequenoPet.getTag().equals("true"))
                                            p.setPorte("pequeno");
                                        else if(medioPet.getTag().equals("truee"))
                                            p.setPorte("medio");
                                        else
                                            p.setPorte("grande");

                                        PetDAO petDAO = PetDAO.getInstance();
                                        petDAO.save(p, null);
                                        Intent retorno = new Intent();

                                        setResult(Activity.RESULT_OK, retorno);
                                        finish();
                                    }
                                });

                            }

                        } else {
                            Log.d("EditarPet", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

            public void setClicklistener() {
                caoPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        caoPet.setImageResource(R.drawable.iconcachorroselected);
                        gatoPet.setImageResource(R.drawable.icongato);
                    }
                });

                gatoPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gatoPet.setImageResource(R.drawable.icongatoselected);
                        caoPet.setImageResource(R.drawable.iconcachorro);
                    }
                });

                machoPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        machoPet.setImageResource(R.drawable.iconmasculinoselected);
                        femeaPet.setImageResource(R.drawable.iconfeminino);
                    }
                });

                femeaPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        femeaPet.setImageResource(R.drawable.iconfemininoselected);
                        machoPet.setImageResource(R.drawable.iconmasculino);
                    }
                });

                simPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        simPet.setImageResource(R.drawable.positivoselected);
                        naoPet.setImageResource(R.drawable.negativo);
                    }
                });

                naoPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        naoPet.setImageResource(R.drawable.negativoselected);
                        simPet.setImageResource(R.drawable.positivo);
                    }
                });

                pequenoPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pequenoPet.setImageResource(R.drawable.iconportepequenoselected);
                        medioPet.setImageResource(R.drawable.iconportemedio);
                        grandePet.setImageResource(R.drawable.iconportegrande);
                    }
                });

                medioPet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        medioPet.setImageResource(R.drawable.iconportemedioselected);
                        pequenoPet.setImageResource(R.drawable.iconportepequeno);
                        grandePet.setImageResource(R.drawable.iconportegrande);
                    }
                });

                grandePet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        grandePet.setImageResource(R.drawable.iconportegrandeselected);
                        medioPet.setImageResource(R.drawable.iconportemedio);
                        pequenoPet.setImageResource(R.drawable.iconportepequeno);
                    }
                });
            }
        }
