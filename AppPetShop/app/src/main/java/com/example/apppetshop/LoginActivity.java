package com.example.apppetshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
//        Sublinhar texto
        TextView textView = findViewById(R.id.textView17);
        SpannableString content = new SpannableString(textView.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, textView.getText().toString().length(), 0);
        textView.setText(content);
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

    public boolean validatePassword(){
        String passwordText = password.getText().toString().trim();

        if(passwordText.isEmpty()){
            password.setError("preencha o campo de senha");
            return false;
        }

        password.setError(null);
        return true;
    }

    public void enter(View view){
        String emailText = email.getText().toString().trim();
        String passwordText = password.getText().toString();
        Toast.makeText(this, String.valueOf(clienteDAO.getAll().size()), Toast.LENGTH_SHORT).show();
        if(validateEmail() && validatePassword()){
            Cliente client;
            client = clienteDAO.getByEmail(emailText);
            if(client != null && client.getSenha().equals(passwordText)){
                Intent i = new Intent( this, MainActivity.class );
                startActivity(i);
            }else if(client == null){
                Toast.makeText(this, "Email não cadastrado", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Senha incorreta", Toast.LENGTH_SHORT).show();
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
