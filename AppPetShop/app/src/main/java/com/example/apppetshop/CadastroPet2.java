package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.apppetshop.model.Pet;

public class CadastroPet2 extends AppCompatActivity {

    private EditText pesoPet;
    private double peso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet2);

        pesoPet = findViewById(R.id.pesoPet);

    }

    public boolean validatePeso(){
        if(pesoPet.getText().toString().isEmpty()){
            pesoPet.setError("Preencha o campo de peso");
            return false;
        }
        pesoPet.setError(null);
        return true;
    }

    public void register(View view){
        peso = Double.parseDouble(pesoPet.getText().toString());
        Pet pet = new Pet();
        if(validatePeso()){
            pet.setPeso(peso);
        }

        Intent intent = new Intent(this, CadastroPet3.class);
        startActivity(intent) ;
    }
}
