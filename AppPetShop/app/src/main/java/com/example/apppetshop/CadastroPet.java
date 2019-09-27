package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CadastroPet extends AppCompatActivity {

    //botao testePet
    private Button pet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet);

        //Testar tela Pets
        pet = findViewById(R.id.cadastroPet1);
        pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testePet();
            }
        });

    }

    public void testePet(){
        Intent intent = new Intent(this, CadastroPet2.class);
        startActivity(intent) ;
    }
}
