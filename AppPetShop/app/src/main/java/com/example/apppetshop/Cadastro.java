package com.example.apppetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apppetshop.DAO.ClienteDAO;
import com.example.apppetshop.model.Cliente;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Cadastro extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText cpf;
    private EditText password;
    private EditText confPassword;

    private FirebaseAuth dbAuth;

    ClienteDAO clienteDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        dbAuth = FirebaseAuth.getInstance();

        clienteDAO = ClienteDAO.getInstance();

        name = findViewById(R.id.nomeUsuario);
        email = findViewById(R.id.emailUsuario);
        cpf = findViewById(R.id.cpfUsuario);
        password = findViewById(R.id.senhaUsuario);
        confPassword = findViewById(R.id.senhaConfUsuario);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher mtw = new MaskTextWatcher(cpf, smf);
        cpf.addTextChangedListener(mtw);

    }

    public boolean validateName() {
        String nameText = name.getText().toString();

        if (nameText.isEmpty()) {
            name.setError("Preencha o campo nome");
            return false;
        }

        name.setError(null);
        return true;
    }

    public boolean validatePassword() {
        String passwordText = password.getText().toString();
        String confpasswordText = confPassword.getText().toString();

        if (passwordText.isEmpty()) {
            password.setError("Preencha o campo senha");
            return false;
        }
        if (!passwordText.equals(confpasswordText)) {
            confPassword.setError("Senhas não conferem");
            return false;
        }

        password.setError(null);
        confPassword.setError(null);
        return true;
    }

    public boolean validateCPF() {
        String cpfText = email.getText().toString();
        if (cpfText.isEmpty()) {
            cpf.setError("Preencha o cpf");
            return false;
        }

        cpf.setError(null);
        return true;
    }

    public boolean validateEmail() {
        String emailText = email.getText().toString().trim();

        if (emailText.isEmpty()) {
            email.setError("preencha o campo de email");
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            email.setError("Insira um endereço de email válido");
            return false;
        }

        email.setError(null);
        return true;
    }

    public void register(View view) {

        final String nameClient = name.getText().toString();
        final String emailClient = email.getText().toString();
        final String cpfClient = cpf.getText().toString();
        final String passwordClient = password.getText().toString();

        if (validateName() && validateEmail() && validateCPF() && validatePassword()) {
            dbAuth.createUserWithEmailAndPassword(emailClient, passwordClient)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String uid = dbAuth.getUid();
                                Cliente cliente = new Cliente(uid, nameClient, cpfClient, emailClient);
                                FirebaseFirestore.getInstance()
                                        .document(uid)
                                        .set(cliente);
                                Toast.makeText(getApplicationContext(), "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Erro ao cadastrar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }
}