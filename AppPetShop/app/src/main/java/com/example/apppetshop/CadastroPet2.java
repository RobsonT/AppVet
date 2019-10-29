package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apppetshop.model.Pet;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.Serializable;

public class CadastroPet2 extends AppCompatActivity {

    private EditText pesoPet;
    private EditText racaPet;
    private ImageView castradoS;
    private ImageView castradoN;
    private ImageView porteP;
    private ImageView porteM;
    private ImageView porteG;
    private Pet pet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pet2);


        pesoPet = findViewById(R.id.pesoPet);
        racaPet = findViewById(R.id.racaPet);
        castradoS = findViewById(R.id.castradoSim);
        castradoN = findViewById(R.id.castradoNao);
        porteP = findViewById(R.id.portePequenoPet);
        porteM = findViewById(R.id.porteMedioPet);
        porteG = findViewById(R.id.porteGrandePet);


        //teste mascara de data
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN");
        MaskTextWatcher mtw = new MaskTextWatcher(pesoPet,smf);
        pesoPet.addTextChangedListener(mtw);

        porteP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                porteP.setImageResource(R.drawable.iconportepequenoselected);
                porteP.setTag("true");
                porteM.setImageResource(R.drawable.iconportemedio);
                porteM.setTag("false");
                porteG.setImageResource(R.drawable.iconportegrande);
                porteG.setTag("false");
            }
        });

        porteM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                porteP.setImageResource(R.drawable.iconportepequeno);
                porteP.setTag("false");
                porteM.setImageResource(R.drawable.iconportemedioselected);
                porteM.setTag("true");
                porteG.setImageResource(R.drawable.iconportegrande);
                porteG.setTag("false");
            }
        });

        porteG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                porteP.setImageResource(R.drawable.iconportepequeno);
                porteP.setTag("false");
                porteM.setImageResource(R.drawable.iconportemedio);
                porteM.setTag("false");
                porteG.setImageResource(R.drawable.iconportegrandeselected);
                porteG.setTag("true");
            }
        });

        castradoS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                castradoS.setImageResource(R.drawable.positivoselected);
                castradoS.setTag("true");
                castradoN.setImageResource(R.drawable.negativo);
                castradoN.setTag("false");
            }
        });

        castradoN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                castradoS.setImageResource(R.drawable.positivo);
                castradoS.setTag("false");
                castradoN.setImageResource(R.drawable.negativoselected);
                castradoN.setTag("true");
            }
        });

        pet = (Pet) getIntent().getSerializableExtra("pet");


        //teste autocomplete
        String[] cachorros = getResources().getStringArray(R.array.gatos);

        AutoCompleteTextView edittext = findViewById(R.id.racaPet);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cachorros);
        edittext.setAdapter(adapter);
    }

    //teste do return button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.return_button, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.buttonreturn){
            Toast.makeText(this,"teste com sucesso",Toast.LENGTH_SHORT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean validatePeso() {
        if (pesoPet.getText().toString().isEmpty()) {
            pesoPet.setError("Preencha o campo de peso");
            return false;
        }
        pesoPet.setError(null);
        return true;
    }

    public boolean validateRaca() {
        if (racaPet.getText().toString().isEmpty()) {
            racaPet.setError("Preencha o campo de raça");
            return false;
        }
        racaPet.setError(null);
        return true;
    }

    public boolean validateCastrado() {
        if (castradoS.getTag().equals("false") && castradoN.getTag().equals("false")) {
            Toast.makeText(this, "Selecione uma opção para castrado", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public boolean validatePorte() {
        if (porteP.getTag().equals("false") && porteM.getTag().equals("false") && porteG.getTag().equals("false")) {
            Toast.makeText(this, "Selecione uma opção para porte", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public void register(View view) {
        if (validatePeso() && validateRaca() && validateCastrado() && validatePorte()) {
            double peso = Double.parseDouble(pesoPet.getText().toString());
            boolean castrado = castradoS.getTag().equals("true");
            String raca = racaPet.getText().toString();
            String porte;
            if (porteP.getTag().equals("true")) {
                porte = "pequeno";
            } else if (porteM.getTag().equals("true")) {
                porte = "medio";
            } else {
                porte = "grande";
            }

            pet.setPeso(peso);
            pet.setRaca(raca);
            pet.setCastrado(castrado);
            pet.setPorte(porte);

            Intent i = new Intent(this, CadastroPet3.class);
            i.putExtra("pet", pet);
            startActivity(i);
        }
    }
}
