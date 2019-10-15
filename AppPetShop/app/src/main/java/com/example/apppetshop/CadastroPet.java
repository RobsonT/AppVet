package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apppetshop.model.Pet;

public class CadastroPet extends AppCompatActivity {

    private EditText namePet;
    private EditText birthDatePet;

    private String sex;
    private String type;
    private String name;
    private String birthDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);

        namePet = findViewById(R.id.nomePet);
        birthDatePet = findViewById(R.id.nascPet);

    }

    public boolean validateName(){
        if(name.isEmpty()){
            namePet.setError("Preencha o campo nome");
            return false;
        }
        namePet.setError(null);
        return true;
    }

    public boolean validateBirthDate(){
        if(birthDate.isEmpty()) {
            birthDatePet.setError("Preencha a data de nascimento");
            return false;
        }
        birthDatePet.setError(null);
        return true;
    }

    public void register(View view){
        name = namePet.getText().toString();
        birthDate = birthDatePet.getText().toString();
        Pet pet = new Pet();
        if(validateName()){
            pet.setNome(name);
        }

        Intent intent = new Intent(this, CadastroPet2.class);
        startActivity(intent) ;
    }
}
