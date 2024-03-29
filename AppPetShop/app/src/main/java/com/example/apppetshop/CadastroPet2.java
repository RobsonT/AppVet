package com.example.apppetshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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


public class CadastroPet2 extends AppCompatActivity {

    private EditText pesoPet;
    private EditText racaPet;
    private ImageView castradoS;
    private ImageView castradoN;
    private ImageView porteP;
    private ImageView porteM;
    private ImageView porteG;
    private Pet pet;
    private Button button;

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
        button = findViewById(R.id.voltarPet2);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retorno = new Intent();

                setResult(Activity.RESULT_CANCELED, retorno);
                finish();
            }
        });


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
        String[] racas;
        if(pet.getTipo().equals("gato")) {
            racas = getResources().getStringArray(R.array.gatos);
        }else{
            racas = getResources().getStringArray(R.array.cachorros);
        }

        AutoCompleteTextView edittext = findViewById(R.id.racaPet);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,racas);
        edittext.setAdapter(adapter);
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
            startActivityForResult(i, 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Intent retorno = new Intent();

            setResult(Activity.RESULT_OK, retorno);
            finish();
        }
    }
}