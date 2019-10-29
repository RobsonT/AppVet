package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apppetshop.model.Pet;
import com.github.rtoshiro.util.format.MaskFormatter;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CadastroPet extends AppCompatActivity {

    private EditText namePet;
    private EditText birthDatePet;
    private ImageView dog;
    private ImageView cat;
    private ImageView sexM;
    private ImageView sexF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);

        namePet = findViewById(R.id.nomePet);
        birthDatePet = findViewById(R.id.nascPet);
        dog = findViewById(R.id.cachorroPet);
        cat = findViewById(R.id.gatoPet);
        sexM = findViewById(R.id.machoPet);
        sexF = findViewById(R.id.femeaPet);

        //teste mascara de data
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(birthDatePet,smf);
        birthDatePet.addTextChangedListener(mtw);

        dog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dog.setTag("true");
                cat.setTag("false");
                dog.setImageResource(R.drawable.iconcachorroselected);
                cat.setImageResource(R.drawable.icongato);

            }
        });

        cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cat.setTag("true");
                dog.setTag("false");
                cat.setImageResource(R.drawable.icongatoselected);
                dog.setImageResource(R.drawable.iconcachorro);
            }
        });

        sexM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexM.setTag("true");
                sexF.setTag("false");
                sexM.setImageResource(R.drawable.iconmasculinoselected);
                sexF.setImageResource(R.drawable.iconfeminino);
            }
        });

        sexF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sexM.setTag("false");
                sexF.setTag("true");
                sexM.setImageResource(R.drawable.iconmasculino);
                sexF.setImageResource(R.drawable.iconfemininoselected);
            }
        });

    }

    public boolean validateName() {
        String name = namePet.getText().toString();
        if (name.isEmpty()) {
            namePet.setError("Preencha o campo nome");
            return false;
        }
        namePet.setError(null);
        return true;
    }

    public boolean validateBirthDate() {
        String birthDate = birthDatePet.getText().toString();
        if (birthDate.isEmpty()) {
            birthDatePet.setError("Preencha a data de nascimento");
            return false;
        }
        birthDatePet.setError(null);
        return true;
    }

    public boolean validateSex(){
        if(sexM.getTag().equals("false") && sexF.getTag().equals("false")){
            Toast.makeText(this, "É necessario escolher um sexo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validateType(){
        if(dog.getTag().equals("false") && cat.getTag().equals("false")){
            Toast.makeText(this, "É necessario escolher um tipo", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void register(View view) {
        if (validateName() && validateBirthDate() && validateSex() && validateType()) {
            String name = namePet.getText().toString();
            String sex = (sexM.getTag().equals("true"))? "macho" : "femea";
            String type = (dog.getTag().equals("true"))? "cachorro" : "gato";

            SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
            Date birthDate = null;
            try {
                birthDate = sdf.parse(birthDatePet.getText().toString());
            } catch (ParseException ex) {
                Log.v("Exception", ex.getLocalizedMessage());
            }

            Pet pet = new Pet();
            pet.setNome(name);
            pet.setNascimento(birthDate);
            pet.setSexo(sex);
            pet.setTipo(type);

            Intent i = new Intent(this, CadastroPet2.class);
            i.putExtra("pet", pet);
            startActivity(i);
        }
    }
}
