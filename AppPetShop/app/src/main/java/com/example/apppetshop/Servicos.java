package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apppetshop.DAO.PetDAO;
import com.example.apppetshop.DAO.ServicoClienteDAO;
import com.example.apppetshop.model.Compra;
import com.example.apppetshop.model.Pet;
import com.example.apppetshop.model.ServicoCliente;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Servicos extends AppCompatActivity {
    private static final String TAG = "Servicos";
    private static  final int ERROR_DIALOG_REQUEST = 9001;


    static TextView localServico;

    ImageView servicoBanho, servicoTosa, servicoHospedagem, servicoAdestrar, servicoCastrar, servicoGeral;
    Spinner horario, pet;
    EditText data;
    Button addServico, button;
    String servico;

    PetDAO petDAO;
    ServicoClienteDAO servicoClienteDAO;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicos);

        localServico = findViewById(R.id.localServico);

        if (isServicesOK()) {
            init();
        }

        auth = FirebaseAuth.getInstance();

        petDAO = PetDAO.getInstance();
        servicoClienteDAO = ServicoClienteDAO.getInstance();

        servico = "";

        servicoBanho = findViewById(R.id.servicoBanho);
        servicoTosa = findViewById(R.id.sevicoTosa);
        servicoHospedagem = findViewById(R.id.servicoHospedagem);
        servicoAdestrar = findViewById(R.id.servicoAdestrar);
        servicoCastrar = findViewById(R.id.servicoCastrar);
        servicoGeral = findViewById(R.id.servicoGeral);

        horario = findViewById(R.id.spinner);
        pet = findViewById(R.id.spinner2);
        data = findViewById(R.id.dataServico);
        addServico = findViewById(R.id.addServico);

        if(getIntent().getExtras() != null){
            fillFields();
        }

        button = findViewById(R.id.cancelarServico);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent retorno = new Intent();

                setResult(Activity.RESULT_CANCELED, retorno);
                finish();
            }
        });


        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(data, smf);
        data.addTextChangedListener(mtw);


        final List<Pet> pets = new ArrayList<>();

        final ArrayAdapter<String>[] spinnerArrayAdapter = new ArrayAdapter[]{null};

        FirebaseFirestore.getInstance().collection("/pets")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Pet pet = document.toObject(Pet.class);
                                if (pet.getIdCliente().equals(auth.getUid())) {
                                    pets.add(pet);
                                }
                            }
                            final String[] spinnerArray = new String[pets.size()];
                            for (int i = 0; i < pets.size(); i++) {
                                spinnerArray[i] = pets.get(i).getNome();
                            }

                            spinnerArrayAdapter[0] = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, spinnerArray);
                            pet.setAdapter(spinnerArrayAdapter[0]);

                            pet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    int position = pet.getSelectedItemPosition();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            addServico.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
                                    Date dataServico = null;
                                    try {
                                        String hora = horario.getSelectedItem().toString();
                                        String date = data.getText().toString() + " " + hora.split(" - ")[0];
                                        dataServico = sdf.parse(date);
                                    } catch (ParseException ex) {
                                        Log.v("Exception", ex.getLocalizedMessage());
                                    }
                                    ServicoCliente servicoCliente = new ServicoCliente();
                                    servicoCliente.setData(dataServico);
                                    servicoCliente.setIdCliente(auth.getUid());

                                    servicoCliente.setIdPet(pets.get(pet.getSelectedItemPosition()).getId());
                                    servicoCliente.setNomeServico(servico.replace("servico", ""));
                                    servicoCliente.setLocal(localServico.getText().toString());
                                    if (getIntent().getExtras() == null)
                                        servicoCliente.setId(FirebaseFirestore.getInstance().collection("servicosCliente").document().getId());
                                    else
                                        servicoCliente.setId(getIntent().getExtras().getString("idServico"));
                                    servicoClienteDAO.save(servicoCliente);
                                    Intent retorno = new Intent();

                                    setResult(Activity.RESULT_OK, retorno);
                                    finish();
                                }
                            });

                        } else {
                            Log.d("Loja fragment", "Error getting documents: ", task.getException());
                        }
                    }
                });


        String[] spinnerArray2 = {"08:00 - 10:00", "10:00 - 12:00", "13:00 - 15:00", "15:00 - 17:00"};

        ArrayAdapter<String> spinnerArrayAdapter2 = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item,
                        spinnerArray2);
        horario.setAdapter(spinnerArrayAdapter2);

        horario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int position = horario.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        servicoBanho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicoBanho.setImageResource(R.drawable.servicobanhoselected);
                servicoTosa.setImageResource(R.drawable.servicotosa);
                servicoHospedagem.setImageResource(R.drawable.servicohospedagem);
                servicoAdestrar.setImageResource(R.drawable.servicoadestrar);
                servicoCastrar.setImageResource(R.drawable.servicocastrar);
                servicoGeral.setImageResource(R.drawable.servicogeral);

                servico = "servicoBanho";
            }
        });

        servicoTosa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicoBanho.setImageResource(R.drawable.servicobanho);
                servicoTosa.setImageResource(R.drawable.servicotosaselected);
                servicoHospedagem.setImageResource(R.drawable.servicohospedagem);
                servicoAdestrar.setImageResource(R.drawable.servicoadestrar);
                servicoCastrar.setImageResource(R.drawable.servicocastrar);
                servicoGeral.setImageResource(R.drawable.servicogeral);

                servico = "servicoTosa";
            }
        });

        servicoHospedagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicoBanho.setImageResource(R.drawable.servicobanho);
                servicoTosa.setImageResource(R.drawable.servicotosa);
                servicoHospedagem.setImageResource(R.drawable.servicohospedagemselected);
                servicoAdestrar.setImageResource(R.drawable.servicoadestrar);
                servicoCastrar.setImageResource(R.drawable.servicocastrar);
                servicoGeral.setImageResource(R.drawable.servicogeral);

                servico = "servicoHospedagem";
            }
        });

        servicoAdestrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicoBanho.setImageResource(R.drawable.servicobanho);
                servicoTosa.setImageResource(R.drawable.servicotosa);
                servicoHospedagem.setImageResource(R.drawable.servicohospedagem);
                servicoAdestrar.setImageResource(R.drawable.servicoadestrarselected);
                servicoCastrar.setImageResource(R.drawable.servicocastrar);
                servicoGeral.setImageResource(R.drawable.servicogeral);

                servico = "servicoAdestrar";
            }
        });

        servicoCastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicoBanho.setImageResource(R.drawable.servicobanho);
                servicoTosa.setImageResource(R.drawable.servicotosa);
                servicoHospedagem.setImageResource(R.drawable.servicohospedagem);
                servicoAdestrar.setImageResource(R.drawable.servicoadestrar);
                servicoCastrar.setImageResource(R.drawable.servicocastrarselected);
                servicoGeral.setImageResource(R.drawable.servicogeral);

                servico = "servicoCastrar";
            }
        });

        servicoGeral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                servicoBanho.setImageResource(R.drawable.servicobanho);
                servicoTosa.setImageResource(R.drawable.servicotosa);
                servicoHospedagem.setImageResource(R.drawable.servicohospedagem);
                servicoAdestrar.setImageResource(R.drawable.servicoadestrar);
                servicoCastrar.setImageResource(R.drawable.servicocastrar);
                servicoGeral.setImageResource(R.drawable.servicogeralselected);
                servico = "servicoGeral";
            }
        });

    }

    private void fillFields() {
        FirebaseFirestore.getInstance().collection("servicosCliente")
                .whereEqualTo("id", getIntent().getExtras().getString("idServico"))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document: task.getResult()){
                            ServicoCliente servico = document.toObject(ServicoCliente.class);
                            switch (servico.getNomeServico()){
                                case "Banho":
                                    servicoBanho.setImageResource(R.drawable.servicobanhoselected);
                                    servicoBanho.setTag("true");
                                    break;
                                case "Tosa":
                                    servicoTosa.setImageResource(R.drawable.servicotosaselected);
                                    servicoTosa.setTag("true");
                                    break;
                                case "Hospedagem":
                                    servicoHospedagem.setImageResource(R.drawable.servicohospedagemselected);
                                    servicoHospedagem.setTag("true");
                                    break;
                                case "Adestrar":
                                    servicoAdestrar.setImageResource(R.drawable.servicoadestrarselected);
                                    servicoAdestrar.setTag("true");
                                    break;
                                case "Castrar":
                                    servicoCastrar.setImageResource(R.drawable.servicocastrarselected);
                                    servicoBanho.setTag("true");
                                    break;
                                case "Geral":
                                    servicoGeral.setImageResource(R.drawable.servicogeralselected);
                                    servicoGeral.setTag("true");
                                    break;
                            }
                            SimpleDateFormat spf = new SimpleDateFormat("dd/MM/yyyy");
                            final String date = spf.format(servico.getData());
                            data.setText(date);
                        }
                    }
                });
    }

    private void init() {
        Button btnMap = findViewById(R.id.mapaServico);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Servicos.this, MapaServico.class);
                intent.putExtra("required", "servicos");
                startActivity(intent);
            }
        });
    }

    //  Verificando se o serviço funciona
    public boolean isServicesOK(){
        Log.d(TAG,"isServiceOK: cheking google services version");

        int availabre = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(Servicos.this);

        if(availabre == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map request
            Log.d(TAG,"isServiceOK: Google Play Services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(availabre)){
            //an error accured but we can resolve it
            Log.d(TAG,"isServiceOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(Servicos.this,availabre, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else {
            Toast.makeText(this, "You can't make map request", Toast.LENGTH_SHORT).show();
        }
        return false;
    }
}
