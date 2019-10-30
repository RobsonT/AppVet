package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;

public class RecuperacaoSenha extends AppCompatActivity {

    EditText emailE;
    Button recover;

    ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperacao_senha);

        clienteDAO = ClienteDAO.getInstance();

        emailE = findViewById(R.id.emailRecuperacao);
        recover = findViewById(R.id.recuperarSenha);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cliente cliente = clienteDAO.getByEmail(emailE.getText().toString());
                if(cliente != null){
//                Enviar senha
                }else{
                    Toast.makeText(getApplicationContext(), "Insira um email válido",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
