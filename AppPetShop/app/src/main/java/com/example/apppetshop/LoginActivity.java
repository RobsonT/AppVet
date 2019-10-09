package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.emailUsuario);
        password = findViewById(R.id.senhaUsuario);
        clienteDAO = ClienteDAO.getInstance();
    }

    public boolean validateEmail(){
        String emailText = email.getText().toString().trim();

        if(emailText.isEmpty()){
            email.setError("preencha o campo de email");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()){
            email.setError("Insira um endereço de email válido");
            return false;
        }

        email.setError(null);
        return true;
    }

    public void enter(View view){
        String emailText = email.getText().toString().trim();
        String passwordText = email.getText().toString();

        if(validateEmail()){
            Cliente client = new Cliente();
            client = clienteDAO.getByEmail(emailText);
            if(!client.equals(null) && client.getPassword() == passwordText){
                Intent i = new Intent( this, Loja.class );
                startActivity(i);
            }
        }
    }

    public void newUser(View view){
        Intent i = new Intent( this, Cadastro.class );
        startActivity(i);
    }

    public void recoverPasssword(View view){

    }
}
