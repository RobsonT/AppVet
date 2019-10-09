package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import com.example.apppetshop.DAO.ClienteDAO;

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
        clienteDAO = new ClienteDAO();
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

        if(validateEmail() )
    }

    public void newUser(View view){

    }

    public void recoverPasssword(View view){

    }
}
